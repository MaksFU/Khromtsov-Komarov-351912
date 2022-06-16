package com.example.testdb.secondScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.testdb.databinding.FragmentAddCategoryBinding
import com.example.testdb.modelsDb.CategoryDb

class AddCategory  : Fragment() {
    private lateinit var binding: FragmentAddCategoryBinding
    private lateinit var viewModel: AddCategoryViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAddCategoryBinding.inflate(layoutInflater, container, false)
        initViewModel()
        initListeners()
        return binding.root
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(AddCategoryViewModel :: class.java)

        viewModel.allCategories.observe(viewLifecycleOwner){
            var str = ""
            for (cat in it)
                str += "${cat.name}, "
            binding.curCats.text = str.dropLast(2)
        }
    }

    private fun initListeners() {
        binding.buttonApply.setOnClickListener {
            if((viewModel.allCategories.value?.size ?: 0) >= 10){
                Toast.makeText(requireContext(), "Достигнуто максимальное количество категорий", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val name = binding.inputCatName.text.toString()

            if (name.isEmpty()) {
                Toast.makeText(requireContext(), "Заполните поле", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            for (cat in viewModel.allCategories.value!!)
                if (name == cat.name){
                    Toast.makeText(requireContext(), "Категория $name уже существует", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }

            viewModel.insert(
                CategoryDb(idForSort = viewModel.allCategories.value?.size ?:0, name = name)
            ) {
                Toast.makeText(requireContext(), "Категория добавлена", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }

        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}