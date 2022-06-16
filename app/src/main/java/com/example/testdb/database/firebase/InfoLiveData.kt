package com.example.testdb.database.firebase

import androidx.lifecycle.LiveData
import com.example.testdb.modelsDb.InfoDb
import com.example.testdb.utils.USER
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class InfoLiveData(from: String): LiveData<List<InfoDb>>(){

    private val infoRef = FirebaseDatabase.getInstance().reference.child(USER.bossId)
        .child("info").orderByChild("time").startAt(from.toDouble())

    private val listener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot)
        {
            value = snapshot.children.map {
                it.getValue(InfoDb()::class.java) ?: InfoDb()
            }
        }
        override fun onCancelled(error: DatabaseError) {}
    }

    override fun onActive() {
        infoRef.addValueEventListener(listener)
        super.onActive()
    }

    override fun onInactive() {
        infoRef.removeEventListener(listener)
        super.onInactive()
    }
}