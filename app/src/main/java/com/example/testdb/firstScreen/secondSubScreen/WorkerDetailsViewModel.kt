package com.example.testdb.firstScreen.secondSubScreen

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.testdb.utils.*
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate

class WorkerDetailsViewModel(private val workerId: String): ViewModel() {
    var spinnerPos: MutableLiveData<Int> = MutableLiveData(0)
    val isLiveDataOrNot: MutableLiveData<Boolean> = MutableLiveData(false)
    var date: MutableLiveData<String> = MutableLiveData(exactDate())
    val allCategories = REPOSITORY.allCategories
    var sum: MutableLiveData<Int> = MutableLiveData()
    var period: MutableLiveData<Int> =  MutableLiveData<Int>(0)
    val isCostsOrIncomes: MutableLiveData<Boolean> = MutableLiveData(true)
    val day: MutableLiveData<Map<String, Long>> by lazy {
        val d = MutableLiveData<Map<String, Long>>()
        if (isCostsOrIncomes.value!!){
            REPOSITORY.getStaticCostOrIncByDay(COST, date.value!!, workerId){
                d.value = it
            }
        }
        else{
            REPOSITORY.getStaticCostOrIncByDay(INCOME, date.value!!, workerId){
                d.value = it
            }
        }
        d
    }
    val costsOrIncomes: LiveData<Map<String, Map<String, Long>>> = Transformations.switchMap(period){
        if (isCostsOrIncomes.value!!) {
            REPOSITORY.getCostOrInc(COST, exactDate(it), workerId)
        }
        else {
            REPOSITORY.getCostOrInc(INCOME, exactDate(it), workerId)
        }
    }

    fun setupPieChart(pieChart: PieChart): PieChart {
        return setupPieChartUtil(pieChart)
    }

    fun loadPieChartData(pieChart: PieChart): PieChart {
        val newList: MutableList<Float> =
            mutableListOf<Float>(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
        var ind: Int
        val cats = allCategories.value
        val entries: ArrayList<PieEntry> = ArrayList()

        if (isLiveDataOrNot.value!!){
            val map = costsOrIncomes.value
            if(cats.isNullOrEmpty() || map == null || map["Дата"]?.get("EMPTY") != null)
                return drawEmptyCircle(pieChart).also { sum.value = 0 }

            for ((k, value) in map) {
                ind = 0
                for (cat in cats) {
                    newList[ind] += value[cat.name]?.toFloat() ?: 0.0f
                    ind++
                }
            }
        }
        else{
            val map = day.value
            if (cats.isNullOrEmpty() || map == null || map["0"] == 0L) return drawEmptyCircle(pieChart).also { sum.value = 0 }
            ind = 0
            for (cat in cats) {
                newList[ind] += map.get(cat.name)?.toFloat() ?: 0.0f
                ind++
            }
        }

        val sumH = newList.sum()
        sum.value = sumH.toInt()
        ind = 0

        for (i in cats) {
            if (newList[ind] != 0.0f) entries.add(PieEntry(newList[ind], i.name))
            ind++
        }

        if(entries.isEmpty()) return drawEmptyCircle(pieChart)

        val colors: ArrayList<Int> = ArrayList()
        for (color in ColorTemplate.MATERIAL_COLORS) {
            colors.add(color)
        }
        for (color in ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color)
        }

        val dataSet = PieDataSet(entries, "")
        dataSet.colors = colors
        val data = PieData(dataSet).apply {
            setDrawValues(true)
            setValueFormatter(PercentFormatter(pieChart))
            setValueTextSize(12f)
            setValueTextColor(Color.BLACK)
        }
        pieChart.data = data
        pieChart.invalidate()
        return pieChart
    }

    private fun drawEmptyCircle(curPieChart: PieChart):  PieChart{
        return drawEmptyCircleUtil(curPieChart)
    }

    fun changeType(dat :String = date.value!!){
        return if(isCostsOrIncomes.value!!){
            REPOSITORY.getStaticCostOrIncByDay(COST, dat, workerId){
                day.value = it
            }
        } else {
            REPOSITORY.getStaticCostOrIncByDay(INCOME, dat, workerId) {
                day.value = it
            }
        }
    }
}