package com.example.testdb.database

import androidx.lifecycle.LiveData
import com.example.testdb.modelsDb.CategoryDb
import com.example.testdb.modelsDb.InfoDb
import com.example.testdb.modelsDb.WorkerDb

interface DatabaseRepository {
    val allWorkers: LiveData<List<WorkerDb>>
    suspend fun insertWorker(worker: WorkerDb, onSuccess:()->Unit)
    suspend fun deleteWorker(worker: WorkerDb, onSuccess:()->Unit)
    suspend fun updateWorkerRules(id:String, isAdmin: Boolean, pos: String)
    suspend fun changeWorkerIds(idClickedWorker: String, idNextWorker: String,
                          idFSClickedWorker: Int, idFSNextWorker: Int, onSuccess:()->Unit)

    val allCategories: LiveData<List<CategoryDb>>
    suspend fun insertCategory(cat: CategoryDb, onSuccess:()->Unit)
    suspend fun deleteCategory(cat: CategoryDb, onSuccess:()->Unit)

    fun getCurrCostOrInc(cat:String, incomeOrCost:String, date: String):LiveData<Double>
    fun getMutCurrCostOrInc(cat:String, mutIncomeOrCost:String, date: String):LiveData<Double>

    fun getCostOrInc(incomeOrCost:String, startAt:String, id:String): LiveData<Map<String, Map<String, Long>>>
    suspend fun insertCost(cat: String, num: Float, date: String, onSuccess:()->Unit)
    suspend fun insertInc(cat: String, num: Float, date: String, onSuccess:()->Unit)

    suspend fun insertMutCost(cat: String, num: Float, date: String)
    suspend fun insertMutInc(cat: String, num: Float, date: String)

    fun getInfo(from: String):LiveData<List<InfoDb>>
    suspend fun insertInfo(info: InfoDb, timestamp: Long = -1)

    fun getStaticCostOrIncByDay(incomeOrCost:String, day:String, id:String, onSuccess: (res: Map<String, Long>) -> Unit)

    fun registerToDatabase(onSuccess: () -> Unit,onFail: (String) -> Unit) {}
    fun authorizeToDatabase(onSuccess: () -> Unit,onFail: (String) -> Unit) {}
    fun signOut(){}
}