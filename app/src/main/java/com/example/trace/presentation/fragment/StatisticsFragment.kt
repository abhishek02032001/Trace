package com.example.trace.presentation.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.trace.R
import com.example.trace.common.TrackingUtility
import com.example.trace.presentation.viewModels.StatisticViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_statistics.*
import java.lang.Math.round

@AndroidEntryPoint
class StatisticsFragment : Fragment(R.layout.fragment_statistics) {
    private val statisticsViewModel: StatisticViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObserver()
    }

    private fun subscribeToObserver(){
        statisticsViewModel.totalTimeRun.observe(viewLifecycleOwner, Observer {
            it?.let {
                val totalTime = TrackingUtility.getFormattedStopWatchTime(it)
                tvTotalTime.text = totalTime
            }
        })

        statisticsViewModel.totalDistance.observe(viewLifecycleOwner, Observer {
            it?.let {
                val km = it / 1000f
                val totalDistance = round(km*10f)/10f
                tvTotalDistance.text = totalDistance.toString() + "km"
            }
        })

        statisticsViewModel.totalAvgSpeed.observe(viewLifecycleOwner, Observer {
            it?.let {
                val avgSpeed = round(it * 10f)/10f
                tvAverageSpeed.text = avgSpeed.toString() + "km/h"
            }
        })

        statisticsViewModel.count.observe(viewLifecycleOwner, Observer {
            it.let {
                travelCount.text = it.toString()
            }
        })
    }
}