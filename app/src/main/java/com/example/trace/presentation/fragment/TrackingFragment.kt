package com.example.trace.presentation.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.trace.R
import com.example.trace.common.Constants.ACTION_PAUSE
import com.example.trace.common.Constants.ACTION_START_OR_RESUME
import com.example.trace.common.Constants.ACTION_STOP
import com.example.trace.common.TrackingUtility
import com.example.trace.db.Run
import com.example.trace.presentation.viewModels.MainViewModel
import com.example.trace.services.TrackingService
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_tracking.*
import java.util.*
import javax.inject.Inject
import kotlin.math.round

@AndroidEntryPoint
class TrackingFragment : Fragment(R.layout.fragment_tracking) {
    private val mainViewModel: MainViewModel by viewModels()

    private var isTracking = false
    private var pathPoints = mutableListOf<MutableList<LatLng>>()

    private var map: GoogleMap? = null

    private var currentTimeInMillis = 0L

    private var menu:Menu? = null

    @set:Inject
    var weight = 80f

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        runnerMap.onCreate(savedInstanceState)
        runnerMap.getMapAsync {
            map = it
            addAllPolyline()
        }

        toggleBtn.setOnClickListener {
            toggleRun()
        }

        finishBtn.setOnClickListener{
            zoomToSeeWholeTrack()
            endRunAndSaveToDB()
        }

        subscribeToObserver()
    }

    private fun subscribeToObserver(){
        TrackingService.isTracking.observe(viewLifecycleOwner, Observer {
            updateTracking(it)
        })

        TrackingService.pathPoints.observe(viewLifecycleOwner, Observer {
            pathPoints= it
            addLatestPolyline()
            moveCameraToUser()
        })

        TrackingService.timeRunInMillis.observe(viewLifecycleOwner, Observer {
            currentTimeInMillis = it
            val formattedTime = TrackingUtility.getFormattedStopWatchTime(currentTimeInMillis, true)
            timer.text = formattedTime
        })
    }

    private fun toggleRun(){
        if(isTracking){
            sendCommandToService(ACTION_PAUSE)
            menu?.getItem(0)?.isVisible = true
        }else{
            sendCommandToService(ACTION_START_OR_RESUME)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_tracking_menu,  menu)
        this.menu = menu
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        if(currentTimeInMillis > 0L){
            this.menu?.getItem(0)?.isVisible = true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.cancelTracking -> {
                showCancelDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showCancelDialog(){
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Cancel the run?")
            .setMessage("Are you sure to cancel the current run and delete all its details.")
            .setIcon(R.drawable.run_icon)
            .setPositiveButton("Yes") {_,_ ->
                stopRun()
            }
            .setNegativeButton("No") {dialogInterface,_ ->
                dialogInterface.cancel()
            }
            .create()

        dialog.show()
    }

    private fun stopRun(){
        timer.text = "00:00:00:00"
        sendCommandToService(ACTION_STOP)
        findNavController().navigate(R.id.action_trackingFragment_to_runFragment)
    }

    private fun updateTracking(isTracking:Boolean){
        this.isTracking = isTracking
        if(!isTracking && currentTimeInMillis > 0L){
            toggleBtn.text = "start"
            finishBtn.visibility = View.VISIBLE
        }else if(isTracking){
            toggleBtn.text = "stop"
            menu?.getItem(0)?.isVisible = true
            finishBtn.visibility = View.INVISIBLE
        }
    }

    private fun addLatestPolyline() {
        if (pathPoints.isNotEmpty() && pathPoints.last().size > 1) {
            val preLastLatLng = pathPoints.last()[pathPoints.last().size - 2]
            val lastLatLng = pathPoints.last().last()
            val polylineOptions = PolylineOptions()
                .color(Color.RED)
                .width(8f)
                .add(preLastLatLng)
                .add(lastLatLng)

            map?.addPolyline(polylineOptions)
        }
    }

    private fun addAllPolyline() {
        for (pl in pathPoints) {
            val polylineOptions = PolylineOptions()
                .color(Color.RED)
                .width(8f)
                .addAll(pl)
            map?.addPolyline(polylineOptions)
        }
    }

    private fun moveCameraToUser() {
        if (pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()) {
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pathPoints.last().last(),
                    20f
                )
            )
        }
    }

    private fun zoomToSeeWholeTrack() {
        val bounds = LatLngBounds.Builder()
        for(polyline in pathPoints) {
            for(pos in polyline) {
                bounds.include(pos)
            }
        }

        map?.moveCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds.build(),
                runnerMap.width,
                runnerMap.height,
                (runnerMap.height * 0.05f).toInt()
            )
        )
    }

    private fun endRunAndSaveToDB(){
        map?.snapshot {
            var distanceInMeters = 0
            for(polyline in pathPoints){
                distanceInMeters += TrackingUtility.calculatePolylineDistance(polyline).toInt()
            }
            val avgSpeed = round((distanceInMeters / 1000f) / (currentTimeInMillis / 1000f / 60 / 60) * 10) / 10f
            val dateTimeStamp = Calendar.getInstance().timeInMillis
            val caloriesBurnt = ((distanceInMeters/1000)*weight).toInt()
            val run = Run(it, dateTimeStamp, avgSpeed , currentTimeInMillis, distanceInMeters, caloriesBurnt)
            mainViewModel.insertRun(run)
            Snackbar.make(
                requireActivity().findViewById(R.id.rootView),
                "Run Saved",
                Snackbar.LENGTH_LONG
            ).show()
            stopRun()
        }
    }

    private fun sendCommandToService(command: String) {
        Intent(requireContext(), TrackingService::class.java).also {
            it.action = command
            requireContext().startService(it)
        }
    }

    override fun onResume() {
        super.onResume()
        runnerMap?.onResume()
    }

    override fun onStart() {
        super.onStart()
        runnerMap?.onStart()
    }

    override fun onStop() {
        super.onStop()
        runnerMap?.onStop()
    }

    override fun onPause() {
        super.onPause()
        runnerMap?.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        runnerMap?.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        runnerMap.onSaveInstanceState(outState)
    }

}