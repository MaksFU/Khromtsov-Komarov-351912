package com.example.testdb.firstScreen.secondSubScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.testdb.modelsDb.WorkerDb
import com.example.testdb.utils.REPOSITORY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddWorkerViewModel(app: Application): AndroidViewModel(app){
    fun insert(worker: WorkerDb, onSuccess: () -> Unit) =
        viewModelScope.launch(Dispatchers.IO){
            REPOSITORY.insertWorker(worker){onSuccess()}
        }
}