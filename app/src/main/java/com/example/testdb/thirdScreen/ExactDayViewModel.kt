package com.example.testdb.thirdScreen

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testdb.utils.*
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter

class ExactDayViewModel: ViewModel() {
    var costOrInc: MutableLiveData<Boolean> = MutableLiveData(true)
    var sum: MutableLiveData<Int> = MutableLiveData()
    var date: MutableLiveData<String> = MutableLiveData()
    val allCategories = REPOSITORY.allCategories

    val day: MutableLiveData<Map<String, Long>> by lazy {
        val d = MutableLiveData<Map<String, Long>>()
        val dat = currDate()
        if(USER.admin)
            REPOSITORY.getStaticCostOrIncByDay(M_COST, dat, USER.bossId){ d.value = it }
        else
            REPOSITORY.getStaticCostOrIncByDay(COST, dat, CURRENT_ID){ d.value = it }
        date.value = dat
        d
    }

    fun setupPieChart(pieChart: PieChart): PieChart {
        return setupPieChartUtil(pieChart)
    }

    fun loadPieChartData(pieChart: PieChart, ar: IntArray): PieChart {
        val newList: MutableList<Float> =
            mutableListOf<Float>(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
        var ind: Int = 0
        val map = day.value
        val cats = allCategories.value

        if (cats.isNullOrEmpty() || map == null || map["0"] == 0L) return drawEmptyCircle(pieChart).also { sum.value = 0 }

        for (cat in cats) {
            newList[ind] += map[cat.name]?.toFloat() ?: 0.0f
            ind++
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

    fun changeType(dat :String = date.value!!): String {
        return if(costOrInc.value!!){
            if(USER.admin)
                REPOSITORY.getStaticCostOrIncByDay(M_COST, dat, USER.bossId){ day.value = it }
            else
                REPOSITORY.getStaticCostOrIncByDay(COST, dat, CURRENT_ID){ day.value = it }
            "Траты"
        } else {
            if(USER.admin)
                REPOSITORY.getStaticCostOrIncByDay(M_INCOME, dat, USER.bossId) { day.value = it }
            else
                REPOSITORY.getStaticCostOrIncByDay(INCOME, dat, CURRENT_ID) { day.value = it }
            "Доходы"
        }
    }
}