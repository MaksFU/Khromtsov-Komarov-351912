package com.example.testdb.thirdScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PeriodsViewModelFactory(private val periodKey: String): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(PeriodsViewModel::class.java)){
            return PeriodsViewModel(periodKey) as T
        }
        throw IllegalArgumentException()
    }
}