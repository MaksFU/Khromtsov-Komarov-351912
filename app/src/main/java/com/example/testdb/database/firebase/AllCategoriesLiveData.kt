package com.example.testdb.database.firebase

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.testdb.modelsDb.CategoryDb
import com.example.testdb.utils.CATEGORIES
import com.example.testdb.utils.REF_DATABASE
import com.example.testdb.utils.USER
import com.example.testdb.utils.W_ID_FOR_SORT
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class AllCategoriesLiveData: LiveData<List<CategoryDb>>(){
    private val categoriesRef = REF_DATABASE.child(USER.bossId).child(CATEGORIES)
        .orderByChild(W_ID_FOR_SORT)

    private val listener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            value = snapshot.children.map {
                it.getValue(CategoryDb::class.java) ?: CategoryDb()
            }
        }

        override fun onCancelled(error: DatabaseError) {}
    }

    override fun onActive() {
        categoriesRef.addValueEventListener(listener)
        super.onActive()
    }

    override fun onInactive() {
        categoriesRef.removeEventListener(listener)
        super.onInactive()
    }
}