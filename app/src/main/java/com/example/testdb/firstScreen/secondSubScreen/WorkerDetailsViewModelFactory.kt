package com.example.testdb.firstScreen.secondSubScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class WorkerDetailsViewModelFactory (private val workerID: String): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(WorkerDetailsViewModel::class.java)){
            return WorkerDetailsViewModel(workerID) as T
        }
        throw IllegalArgumentException()
    }
}