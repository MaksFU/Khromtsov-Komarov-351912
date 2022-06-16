package com.example.testdb.firstScreen.firstSubScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.testdb.modelsDb.InfoDb
import com.example.testdb.utils.REPOSITORY
import com.example.testdb.utils.exactDate
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class InfoCostOrIncViewModel: ViewModel(){
    var spinnerPos: MutableLiveData<Int> = MutableLiveData(0)
    var day: MutableLiveData<Int> = MutableLiveData(0)
    var lastCh: MutableLiveData<String> = MutableLiveData("Конкретный день")
    var newCh: MutableLiveData<String> = MutableLiveData("Конкретный день")

    val infos: LiveData<List<InfoDb>> = Transformations.switchMap(spinnerPos) {
        val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        var from: String = ""
        when(it) {
            0-> from = exactDate(day.value!!)
            1-> from = exactDate(7)
            2-> from = exactDate(30)
            3-> from = exactDate(360)
        }
        val date: Date = formatter.parse(from) as Date
        REPOSITORY.getInfo(date.time.toString())
    }
}