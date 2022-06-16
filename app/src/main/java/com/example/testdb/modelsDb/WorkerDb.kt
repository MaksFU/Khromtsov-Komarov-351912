package com.example.testdb.modelsDb

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WorkerDb(
    var admin: Boolean = false,
    var id: String = "",
    val idForSort: Int  = -1,
    val fName: String = "",
    val sName: String = "",
    val tName: String = "",
    val position: String = "",
    val email: String = "",
    val password: String = "",
    val firstPass: String = "",
    var bossId: String = ""
): Parcelable