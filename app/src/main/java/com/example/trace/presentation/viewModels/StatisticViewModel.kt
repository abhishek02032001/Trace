package com.example.trace.presentation.viewModels

import androidx.lifecycle.ViewModel
import com.example.trace.repositories.RunRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StatisticViewModel @Inject constructor(
    private val runRepository: RunRepository
) : ViewModel() {

    val totalTimeRun = runRepository.getTotalTimeInMillis()
    val totalDistance = runRepository.getTotalDistance()
    val totalAvgSpeed = runRepository.getTotalAvgSpeed()
    val count = runRepository.getTravelTime()
}