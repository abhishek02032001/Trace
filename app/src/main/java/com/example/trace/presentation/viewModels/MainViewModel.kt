package com.example.trace.presentation.viewModels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trace.common.SortType
import com.example.trace.db.Run
import com.example.trace.repositories.RunRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val runRepository: RunRepository
) : ViewModel(){

    private val runSortedByDate = runRepository.getAllRunsSortedByDate()
    private val runSortedByDistance = runRepository.getAllRunsSortedByDistanceInMeters()
    private val runSortedByTimeInMillis = runRepository.getAllRunsSortedByTimeInMillis()
    private val runSortedByAvgSpeed = runRepository.getALlRunsSortedByAvgSpeedInKMH()

    val runs = MediatorLiveData<List<Run>>()

    var sortType = SortType.DATE

    init {
        runs.addSource(runSortedByDate) {
            if(sortType == SortType.DATE){
                it?.let {
                    runs.value = it
                }
            }
        }
        runs.addSource(runSortedByDistance) {
            if(sortType == SortType.DISTANCE){
                it?.let {
                    runs.value = it
                }
            }
        }
        runs.addSource(runSortedByTimeInMillis) {
            if(sortType == SortType.RUNNING_TIME){
                it?.let {
                    runs.value = it
                }
            }
        }
    }

    fun sortRuns(sortType:SortType) = when(sortType){
        SortType.DATE -> runSortedByDate.value?.let { runs.value = it }
        SortType.DISTANCE -> runSortedByDistance.value?.let { runs.value = it }
        SortType.RUNNING_TIME -> runSortedByTimeInMillis.value?.let { runs.value = it }

    }.also {
        this.sortType = sortType
    }


    fun insertRun(run:Run) =
        viewModelScope.launch {
            runRepository.insertRun(run)
        }
}