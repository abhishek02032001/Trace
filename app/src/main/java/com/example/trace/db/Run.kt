package com.example.trace.db

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "running_table")
data class Run(
    var img: Bitmap? = null,
    var timeStamp: Long = 0L,
    var avgSpeedInKMH: Float = 0f,
    var timeInMillis: Long = 0L,
    var distanceInMeters: Int = 0,
    var caloriesBurned: Int = 0,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}
