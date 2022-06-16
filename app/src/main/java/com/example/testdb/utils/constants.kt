package com.example.testdb.utils

import android.graphics.Color
import com.example.testdb.database.DatabaseRepository
import com.example.testdb.modelsDb.WorkerDb
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import org.joda.time.LocalDate

lateinit var AUTH: FirebaseAuth
lateinit var CURRENT_ID:String
lateinit var REF_DATABASE: DatabaseReference
lateinit var BUTTOM_NAV: BottomNavigationView
lateinit var REPOSITORY: DatabaseRepository
lateinit var USER: WorkerDb


const val TYPE_FIREBASE = "type_database"

const val K_WEEK = "WEEK"
const val K_MONTH = "MONTH"
const val K_YEAR = "YEAR"

const val W_ID = "id"
const val W_ID_FOR_SORT= "idForSort"
const val W_BOSS_ID = "bossId"
const val W_EMAIL = "email"
const val W_PASSWORD = "password"
const val W_F_PASSWORD = "firstPassword"
const val W_F_NAME = "fName"
const val W_S_NAME = "sName"
const val W_T_NAME = "tName"
const val W_POSITION = "position"
const val W_IS_ADMIN = "admin"
const val W_USER_INFO = "userInfo"

const val C_NAME = "name"

const val I_TIME = "time"
const val I_TYPE = "type"
const val C_CAT = "cat"
const val C_NOTE = "note"
const val C_NUMBER = "number"

const val COST = "costs"
const val INCOME = "incomes"
const val M_COST = "mutCosts"
const val M_INCOME = "mutIncomes"
const val WORKERS = "workers"
const val CATEGORIES = "categories"
const val INFO = "info"


fun currDate(): String{
    return LocalDate.now().toString()
}

fun exactDate(n:Int = 0): String{
    return LocalDate.now().minusDays(n).toString()
}

fun setupPieChartUtil(pieChart: PieChart): PieChart {
    pieChart.isDrawHoleEnabled = true
    pieChart.setUsePercentValues(false)
    pieChart.setEntryLabelTextSize(18f)
    pieChart.setDrawEntryLabels(false)
    pieChart.setEntryLabelColor(Color.BLACK)
    pieChart.centerText = "Траты"
    pieChart.setCenterTextSize(24f)
    pieChart.description?.isEnabled = false
    val l: Legend = pieChart.legend
    pieChart.extraBottomOffset = 35f
    l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
    l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
    l.textSize = 18f
    l.isWordWrapEnabled = true
    l.orientation = Legend.LegendOrientation.HORIZONTAL
    l.yOffset = 10f
    l.setDrawInside(true)
    l.mNeededHeight = 100f
    l.isEnabled = true
    return pieChart
}

fun drawEmptyCircleUtil(curPieChart: PieChart):  PieChart{
    curPieChart.data = PieData(PieDataSet(arrayListOf(PieEntry(1f, "")), "Пусто")
        .apply { colors = listOf(Color.GRAY) }).apply { setDrawValues(false) }
    curPieChart.invalidate()
    return curPieChart
}

