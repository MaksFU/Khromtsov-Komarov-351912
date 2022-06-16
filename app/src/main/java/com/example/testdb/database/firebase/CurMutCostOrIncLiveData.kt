package com.example.testdb.database.firebase

import androidx.lifecycle.LiveData
import com.example.testdb.utils.REF_DATABASE
import com.example.testdb.utils.USER
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.lang.Exception

class CurMutCostOrIncLiveData(category: String, mutIncomeOrCost:String, date: String) : LiveData<Double>() {
    private val mutCurCostOrIncRef = REF_DATABASE.child(USER.bossId)
        .child(mutIncomeOrCost).child(date).child(category)

    private val listener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            value = try {
                snapshot.value.toString().toDouble()
            } catch (ex: Exception){
                0.0
            }
        }

        override fun onCancelled(error: DatabaseError) {}
    }

    override fun onActive() {
        mutCurCostOrIncRef.addValueEventListener(listener)
        super.onActive()
    }

    override fun onInactive() {
        mutCurCostOrIncRef.removeEventListener(listener)
        super.onInactive()
    }
}