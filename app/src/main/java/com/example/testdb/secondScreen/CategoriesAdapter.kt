package com.example.testdb.secondScreen

import android.graphics.Color
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.testdb.databinding.ItemCategoryBinding
import com.example.testdb.modelsDb.CategoryDb
import com.example.testdb.utils.USER

interface CategoryActionListener {
    fun onCategoryDelete(category: CategoryDb)
    fun onCategoryClick(category: CategoryDb)
}

class CategoriesDiffUtilsCallback(
    private val oldList: List<CategoryDb>,
    private val newList: List<CategoryDb>
): DiffUtil.Callback(){
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]
}

class CategoriesAdapter(
    private val actionListener: CategoryActionListener
) : RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>(){
    var categories: List<CategoryDb> = emptyList()
        set(newValue) {
            val diffRez = DiffUtil.calculateDiff(CategoriesDiffUtilsCallback(field, newValue))
            field = newValue
            diffRez.dispatchUpdatesTo(this)
        }

    var selectedPos = -4
    private var lastV: View? = null

    override fun getItemCount(): Int = categories.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCategoryBinding.inflate(inflater, parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
//        holder.itemView.isSelected = selectedPos == position
        val myCategory = categories[position]

        with(holder.binding) {
            holder.itemView.tag = myCategory
            moreImg.tag = myCategory
            category.text = myCategory.name

            if(selectedPos == holder.adapterPosition) //!!!!!!!!
            {
                holder.itemView.setBackgroundColor(Color.GREEN)
                lastV = holder.itemView
            }
            else
                holder.itemView.setBackgroundColor(Color.WHITE)
        }

        if (USER.admin)
            holder.binding.moreImg.setOnClickListener{
                showPopupMenu(it)
            }

        holder.binding.root.setOnClickListener {
            val cat = it.tag as CategoryDb
            lastV?.setBackgroundColor(Color.WHITE)
            it.setBackgroundColor(Color.GREEN)
            lastV = it
            selectedPos = cat.idForSort
            actionListener.onCategoryClick(cat)
        }
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(view.context, view)
        val category = view.tag as CategoryDb

        popupMenu.menu.add(0, ID_REMOVE, Menu.NONE, "Удалить").apply {
            isEnabled = true
        }

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                ID_REMOVE -> {
                    actionListener.onCategoryDelete(category)
                    if(category.idForSort < selectedPos)
                        selectedPos -= 1
                    else if(category.idForSort == selectedPos)
                        selectedPos = -3
                }
            }
            return@setOnMenuItemClickListener true
        }
        popupMenu.show()
    }

    class CategoryViewHolder(
        val binding: ItemCategoryBinding
    ) : RecyclerView.ViewHolder(binding.root)

    companion object {
        private const val ID_REMOVE = 1
    }
}