package com.example.testdb.firstScreen.secondSubScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testdb.utils.REPOSITORY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WorkerDataViewModel: ViewModel() {
    fun updateRules(id:String, isAdmin: Boolean, pos: String) =
        viewModelScope.launch(Dispatchers.IO){
            REPOSITORY.updateWorkerRules(id, isAdmin, pos)
        }
}
