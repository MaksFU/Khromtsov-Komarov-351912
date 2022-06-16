package com.example.testdb.modelsDb

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class InfoDb(
    val id: String = "",
    val type: String = "",
    val cat: String = "",
    val number: Int = 0,
    var time: Long = 0,
    val note: String  = "",
    val fName: String = "",
    val sName: String = "",
    val tName: String = "",
    val position: String = ""
): Parcelable