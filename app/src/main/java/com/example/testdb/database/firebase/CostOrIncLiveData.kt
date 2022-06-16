package com.example.testdb.database.firebase

import androidx.lifecycle.LiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception

class CostOrIncLiveData(incomeOrCost:String, startAt: String, id:String)
    : LiveData<Map<String, Map<String, Long>>>(){

    private val todayCostRef = FirebaseDatabase.getInstance().reference.child(id)
        .child(incomeOrCost).orderByKey().startAt(startAt)

    private val listener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            value = try {
                snapshot.value as Map<String, Map<String, Long>>
            } catch (ex: Exception){
                mapOf<String, Map<String, Long>>("Дата" to mapOf<String, Long>("EMPTY" to 0))
            }
        }

        override fun onCancelled(error: DatabaseError) {}
    }

    override fun onActive() {
        todayCostRef.addValueEventListener(listener)
        super.onActive()
    }

    override fun onInactive() {
        todayCostRef.removeEventListener(listener)
        super.onInactive()
    }
}