package com.example.testdb.database.firebase

import androidx.lifecycle.LiveData
import com.example.testdb.modelsDb.WorkerDb
import com.example.testdb.utils.REF_DATABASE
import com.example.testdb.utils.USER
import com.example.testdb.utils.WORKERS
import com.example.testdb.utils.W_ID_FOR_SORT
import com.google.firebase.database.*

class AllWorkersLiveData: LiveData<List<WorkerDb>>(){
    private val workersRef = REF_DATABASE
        .child(USER.bossId).child(WORKERS).orderByChild(W_ID_FOR_SORT)

    private val listener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            value = snapshot.children.map {
                it.getValue(WorkerDb::class.java) ?: WorkerDb()
            }
        }
        override fun onCancelled(error: DatabaseError) {}
    }

    override fun onActive() {
        workersRef.addValueEventListener(listener)
        super.onActive()
    }

    override fun onInactive() {
        workersRef.removeEventListener(listener)
        super.onInactive()
    }
}