package com.example.testdb.firstScreen.secondSubScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testdb.modelsDb.WorkerDb
import com.example.testdb.utils.REPOSITORY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WorkersViewModel: ViewModel(){
     val allWorkers = REPOSITORY.allWorkers

     fun delete(worker: WorkerDb, onSuccess: () -> Unit) =
        viewModelScope.launch(Dispatchers.IO){
            REPOSITORY.deleteWorker(worker){onSuccess()}
        }

    fun moveUser(idClickedWorker: String, idNextWorker: String, idFSClickedWorker: Int, idFSNextWorker: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            REPOSITORY.changeWorkerIds(idClickedWorker, idNextWorker, idFSClickedWorker, idFSNextWorker){}
        }
    }
}