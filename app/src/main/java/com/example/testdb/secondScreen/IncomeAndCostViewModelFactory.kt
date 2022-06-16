package com.example.testdb.secondScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class IncomeAndCostViewModelFactory (private val cat: String, private val costOrIncome: String): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(IncomeAndCostViewModel::class.java)){
            return IncomeAndCostViewModel(cat, costOrIncome) as T
        }
        throw IllegalArgumentException()
    }
}