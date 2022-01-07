package com.example.trace.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.trace.R
import com.example.trace.common.TrackingUtility
import com.example.trace.db.Run
import kotlinx.android.synthetic.main.item_run.view.*
import java.text.SimpleDateFormat
import java.util.*

class RunAdapter : RecyclerView.Adapter<RunAdapter.RunViewHolder>() {

    val diffCallback = object : DiffUtil.ItemCallback<Run>() {
        override fun areItemsTheSame(oldItem: Run, newItem: Run): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Run, newItem: Run): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(runList: List<Run>) = differ.submitList(runList)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_run, parent, false)
        return RunViewHolder(view)
    }

    override fun onBindViewHolder(holder: RunViewHolder, position: Int) {
        val currentRun = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this)
                .load(currentRun.img)
                .into(runImage)

            val calendar = Calendar.getInstance().apply {
                timeInMillis = currentRun.timeStamp
            }

            val dateFormat =  SimpleDateFormat("dd:MM:yy", Locale.getDefault())
            date.text = dateFormat.format(calendar.time)
            val avgSpeed = "${ currentRun.avgSpeedInKMH }KM/H"
            speed.text = avgSpeed
            val distanceInKm = "${currentRun.distanceInMeters / 1000f}km"
            distance.text = distanceInKm
            time.text = TrackingUtility.getFormattedStopWatchTime(currentRun.timeInMillis)

        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class RunViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}