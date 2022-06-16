package com.example.testdb.firstScreen.secondSubScreen

import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.testdb.R
import com.example.testdb.databinding.ItemWorkerBinding
import com.example.testdb.modelsDb.WorkerDb
import com.example.testdb.utils.USER

interface WorkerActionListener {
    fun onWorkerMove(idClickedWorker: String, idNextWorker: String,
                     idFSClickedWorker: Int, idFSNextWorker: Int)
    fun onWorkerDelete(worker: WorkerDb)
    fun onWorkerDetails(worker: WorkerDb)
    fun onWorkerChange(worker: WorkerDb)
}

class CitesDiffUtilsCallback(
    private val oldList: List<WorkerDb>,
    private val newList: List<WorkerDb>
):DiffUtil.Callback(){
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]
}

class WorkersAdapter(private val actionListener: WorkerActionListener):
    RecyclerView.Adapter<WorkersAdapter.WorkersViewHolder>(), View.OnClickListener {
    var workers: List<WorkerDb> = emptyList()
        set(newValue) {
            val diffRez = DiffUtil.calculateDiff(CitesDiffUtilsCallback(field, newValue))
            field = newValue
            diffRez.dispatchUpdatesTo(this)
        }

    override fun onClick(v: View) {
        val worker = v.tag as WorkerDb
        when (v.id) {
            R.id.moreImageViewButton -> showPopupMenu(v)
            else -> actionListener.onWorkerDetails(worker)
        }
    }

    override fun getItemCount(): Int = workers.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemWorkerBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)
        binding.moreImageViewButton.setOnClickListener(this)
        return WorkersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkersViewHolder, position: Int) {
        val myWorker = workers[position]
        val fulName = myWorker.sName + " " + myWorker.fName +" "+ myWorker.tName
        with(holder.binding) {
            holder.itemView.tag = myWorker
            moreImageViewButton.tag = myWorker
            name.text = fulName
            email.text = myWorker.email
            if(myWorker.admin)
                isAdm.visibility = View.VISIBLE
            else
                isAdm.visibility = View.GONE
        }
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(view.context, view)
        val worker = view.tag as WorkerDb
        val position = workers.indexOfFirst { it.id == worker.id }

        popupMenu.menu.add(0, ID_MOVE_UP, Menu.NONE, "Вверх").apply {
            isEnabled = position > 0
        }
        popupMenu.menu.add(0, ID_MOVE_DOWN, Menu.NONE, "Вниз").apply {
            isEnabled = position < workers.size - 1
        }

        if ((worker.admin and (USER.id == USER.bossId)) or !worker.admin) {
            popupMenu.menu.add(0, CHANGE, Menu.NONE, "Изменить")
            if (worker.id != USER.bossId)
            popupMenu.menu.add(0, ID_REMOVE, Menu.NONE, "Удалить")
        }

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                ID_MOVE_UP -> {
                    val nextWorker = workers[worker.idForSort-1]
                    actionListener.onWorkerMove(worker.id, nextWorker.id,
                        worker.idForSort - 1, nextWorker.idForSort + 1)
                }
                ID_MOVE_DOWN -> {
                    val nextWorker = workers[worker.idForSort+1]
                    actionListener.onWorkerMove(worker.id, nextWorker.id,
                        worker.idForSort + 1, nextWorker.idForSort - 1)
                }
                ID_REMOVE -> actionListener.onWorkerDelete(worker)
                CHANGE -> actionListener.onWorkerChange(worker)
            }
            return@setOnMenuItemClickListener true
        }
        popupMenu.show()
    }

    class WorkersViewHolder(
        val binding: ItemWorkerBinding
    ) : RecyclerView.ViewHolder(binding.root)

    companion object {
        private const val ID_MOVE_UP = 1
        private const val ID_MOVE_DOWN = 2
        private const val ID_REMOVE = 3
        private const val CHANGE = 4
    }
}