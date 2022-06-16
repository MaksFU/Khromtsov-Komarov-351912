package com.example.testdb.start

import androidx.lifecycle.ViewModel
import com.example.testdb.database.firebase.FirebaseRepository
import com.example.testdb.utils.*

class StartFragmentViewModel: ViewModel() {
    fun authorize(type:String, onSuccess:()->Unit, onFail:(String)->Unit){
        when(type){
            TYPE_FIREBASE -> {
                REPOSITORY = FirebaseRepository()
                REPOSITORY.authorizeToDatabase(
                    {
                        AppPreference.setInitUser(true, USER.email, USER.password)
                        onSuccess()
                    }, onFail)
            }
        }
    }

    fun register(type:String, onSuccess:()->Unit, onFail:(String)->Unit){
        when(type){
            TYPE_FIREBASE -> {
                REPOSITORY = FirebaseRepository()
                REPOSITORY.registerToDatabase(
                    {
                    AppPreference.setInitUser(true, USER.email, USER.password)
                    onSuccess()
                    }, onFail)
            }
        }
    }
}