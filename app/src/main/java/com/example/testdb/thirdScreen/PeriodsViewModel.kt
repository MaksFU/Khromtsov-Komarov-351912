package com.example.testdb.thirdScreen

import android.graphics.Color
import android.util.Log
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

class PeriodsViewModel(periodKey: String): ViewModel() {
    private val startAt = when (periodKey) {
        K_WEEK -> exactDate(7)
        K_MONTH -> exactDate(30)
        else -> exactDate(360)
    }
    val allCategories = REPOSITORY.allCategories
    var isCostsOrIncomes: MutableLiveData<Boolean> = MutableLiveData(true)
    var sum: MutableLiveData<Int> = MutableLiveData()
    val costsOrIncomesLD: LiveData<Map<String, Map<String, Long>>> = Transformations.switchMap(isCostsOrIncomes){
        if (it) {
            if(USER.admin)
                REPOSITORY.getCostOrInc(M_COST, startAt, USER.bossId)
            else
                REPOSITORY.getCostOrInc(COST, startAt, CURRENT_ID)
        }
        else {
            if(USER.admin)
                REPOSITORY.getCostOrInc(M_INCOME, startAt, USER.bossId)
            else
                REPOSITORY.getCostOrInc(INCOME, startAt, CURRENT_ID)
        }
    }

    fun setupPieChart(pieChart: PieChart): PieChart {
        return setupPieChartUtil(pieChart)
    }

    fun loadPieChartData(pieChart: PieChart, ar: IntArray): PieChart {
        val newList: MutableList<Float> =
            mutableListOf<Float>(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
        var ind: Int
        val map = costsOrIncomesLD.value
        val cats = allCategories.value

        if(cats.isNullOrEmpty() || map == null || map["Дата"]?.get("EMPTY") != null)
            return drawEmptyCircle(pieChart).also { sum.value = 0 }

        for ((k, value) in map) {
            ind = 0
            for (cat in cats) {
                newList[ind] += value[cat.name]?.toFloat() ?: 0.0f
                ind++
            }
        }

        val sumH = newList.sum()
        sum.value = sumH.toInt()
        val entries: ArrayList<PieEntry> = ArrayList()
        ind = 0

        for (i in cats) {
            if (newList[ind] != 0.0f) entries.add(PieEntry(newList[ind], i.name))
            ind++
        }

        if(entries.isEmpty()) return drawEmptyCircle(pieChart)

        val colors: ArrayList<Int> = ArrayList()
        for (color in ar)
            colors.add(color)

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
}