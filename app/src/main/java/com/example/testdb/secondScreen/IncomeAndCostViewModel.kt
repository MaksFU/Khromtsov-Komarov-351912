package com.example.testdb.secondScreen

import androidx.lifecycle.*
import com.example.testdb.modelsDb.InfoDb
import com.example.testdb.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class IncomeAndCostViewModel(val cat: String, private val costOrIncome: String): ViewModel(){
    var date: MutableLiveData<String> = MutableLiveData(exactDate())
    val curInOrCost: LiveData<Double> = Transformations.switchMap(date){
        REPOSITORY.getCurrCostOrInc(cat, costOrIncome, it)
    }
    val mutCurInOrCost: LiveData<Double> = Transformations.switchMap(date){
        REPOSITORY.getMutCurrCostOrInc(cat, costOrIncome, it)
    }

    fun insertCostOrInc(num:Float, timestamp: Long, note: String , onSuccess: () -> Unit){
        val valueCur: Float = curInOrCost.value?.toFloat() ?: 0.0f
        val mutValueCur: Float = mutCurInOrCost.value?.toFloat() ?: 0.0f
        if (costOrIncome == INCOME)
            viewModelScope.launch(Dispatchers.IO) {
                REPOSITORY.insertInc(cat, num + valueCur, date.value!!){onSuccess()}
                REPOSITORY.insertMutInc(cat, num + mutValueCur, date.value!!)
                REPOSITORY.insertInfo(InfoDb(
                    type = INCOME,
                    cat = cat,
                    number = num.toInt() + valueCur.toInt(),
                    note = note,
                    fName = USER.fName,
                    sName = USER.sName,
                    tName = USER.tName,
                    position = USER.position
                ), timestamp)
            }
        else
            viewModelScope.launch(Dispatchers.IO) {
                REPOSITORY.insertCost(cat, num + valueCur, date.value!!){onSuccess()}
                REPOSITORY.insertMutCost(cat, num + mutValueCur, date.value!!)
                REPOSITORY.insertInfo(InfoDb(
                    type = COST,
                    cat = cat,
                    number = num.toInt() + valueCur.toInt(),
                    note = note,
                    fName = USER.fName,
                    sName = USER.sName,
                    tName = USER.tName,
                    position = USER.position
                ), timestamp)
            }
    }
}




