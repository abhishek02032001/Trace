package com.example.trace.repositories

import com.example.trace.db.Run
import com.example.trace.db.RunDAO
import javax.inject.Inject

class RunRepository @Inject constructor(
    private val runDao:RunDAO
) {

    suspend fun insertRun(run: Run) = runDao.insertRun(run)

    fun getAllRunsSortedByDate() = runDao.getAllRunsSortedByDate()

    fun getAllRunsSortedByTimeInMillis() = runDao.getAllRunsSortedByTimeInMillis()

    fun getAllRunsSortedByDistanceInMeters() = runDao.getAllRunsSortedByDistanceInMeters()

    fun getALlRunsSortedByAvgSpeedInKMH() = runDao.getAllRunsSortedByAvgSpeedInKMH()

    fun getTotalDistance() = runDao.getTotalDistance()

    fun getTotalAvgSpeed() = runDao.getTotalAvgSpeed()

    fun getTotalTimeInMillis() = runDao.getTotalTimeInMillis()

    fun getTravelTime() = runDao.getTravelCount()

}