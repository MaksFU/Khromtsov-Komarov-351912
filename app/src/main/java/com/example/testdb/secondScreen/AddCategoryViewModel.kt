package com.example.testdb.secondScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testdb.modelsDb.CategoryDb
import com.example.testdb.utils.REPOSITORY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddCategoryViewModel: ViewModel(){
    val allCategories = REPOSITORY.allCategories

    fun insert(category: CategoryDb, onSuccess: () -> Unit) =
        viewModelScope.launch(Dispatchers.IO){
            REPOSITORY.insertCategory(category){onSuccess()}
        }
}