package com.example.testdb.secondScreen

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.*
import com.example.testdb.modelsDb.CategoryDb
import com.example.testdb.utils.*
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SecondFViewModel: ViewModel(){
    val selectedPos : MutableLiveData<Int?> by lazy {
        MutableLiveData<Int?>()
    }
    val allCategories = REPOSITORY.allCategories
    val isCostsOrIncomes: MutableLiveData<Boolean> = MutableLiveData(true)
    var sum : MutableLiveData<Int> = MutableLiveData()
    val todayCostsOrIncomes: LiveData<Map<String, Map<String, Long>>> = Transformations.switchMap(isCostsOrIncomes){
        if (it) {
            if(USER.admin)
                REPOSITORY.getCostOrInc(M_COST, currDate(), USER.bossId)
            else
                REPOSITORY.getCostOrInc(COST, currDate(), USER.id)
        }
        else {
            if(USER.admin)
                REPOSITORY.getCostOrInc(M_INCOME, currDate(),  USER.bossId)
            else
                REPOSITORY.getCostOrInc(INCOME, currDate(), USER.id)
        }
    }

    fun delete(category: CategoryDb, onSuccess: () -> Unit) =
        viewModelScope.launch(Dispatchers.IO){
            REPOSITORY.deleteCategory(category){}
            withContext(Dispatchers.Main){
                selectedPos.value = null
                onSuccess()
            }
        }

    fun setupPieChart(pieChart: PieChart): PieChart{
        pieChart.isDrawHoleEnabled = true //дырка внутри диаграммы
        pieChart.setUsePercentValues(false) //проценты а не значения
        pieChart.setEntryLabelTextSize(12f) //размер текста на диаграмме
        pieChart.setEntryLabelColor(Color.BLACK) //цвет дырки
        pieChart.centerText = "Траты" //текст дырки
        pieChart.setCenterTextSize(24f) //размер текста дырки
        pieChart.description?.isEnabled = false //описание *
        pieChart.legend.isEnabled = false
        return pieChart
    }


    fun loadPieChartData(pieChart: PieChart, ar: IntArray): PieChart {
        val newList: MutableList<Float> = mutableListOf<Float>(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
        var ind: Int
        val map = todayCostsOrIncomes.value
        val cats = allCategories.value


        Log.d("Тоня", map.toString())
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

        for(i in cats){
            if (newList[ind] != 0.0f) entries.add(PieEntry(newList[ind], i.name))
            ind++
        }

        if(entries.isEmpty()) return drawEmptyCircle(pieChart)

        val colors: ArrayList<Int> = ArrayList()
        for (color in ar) {
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

    private fun drawEmptyCircle(pieChart: PieChart): PieChart {
        return drawEmptyCircleUtil(pieChart)
    }
}