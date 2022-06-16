package com.example.testdb.secondScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testdb.R
import com.example.testdb.databinding.FragmentSecondFBinding
import com.example.testdb.modelsDb.CategoryDb
import com.example.testdb.utils.BUTTOM_NAV
import com.example.testdb.utils.COST
import com.example.testdb.utils.INCOME
import com.example.testdb.utils.USER
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart

class SecondF : Fragment() {
    lateinit var binding: FragmentSecondFBinding
    lateinit var pieChart: PieChart
    private lateinit var adapter: CategoriesAdapter
    private lateinit var viewModel: SecondFViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSecondFBinding.inflate(layoutInflater, container, false)
        initViewModel()
        initPieChart()
        initRvAdapter()
        initListeners()
        if(!USER.admin)
            binding.addCategory.visibility = View.GONE
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        requireActivity().window.navigationBarColor = ContextCompat.getColor(requireContext(),
            R.color.button_color)
        BUTTOM_NAV.visibility = View.VISIBLE
        binding.graphSec.visibility = View.VISIBLE
        pieChart.animateY(1400, Easing.EaseInOutQuad)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.graphSec.visibility = View.INVISIBLE
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(SecondFViewModel::class.java)

        viewModel.allCategories.observe(viewLifecycleOwner){
            adapter.categories = it
            pieChart = viewModel.loadPieChartData(pieChart, resources.getIntArray(R.array.d_colors))
        }

        viewModel.sum.observe(viewLifecycleOwner){
            binding.textView2.text = "$it руб."
        }

        viewModel.selectedPos.observe(viewLifecycleOwner){
            if (it != null) {
                binding.category.text = adapter.categories[it].name
                adapter.selectedPos = it
            }
            else{
                binding.category.text = ""
            }
        }

        viewModel.isCostsOrIncomes.observe(viewLifecycleOwner){
            if (it)
                pieChart.centerText = "Траты"
            else
                pieChart.centerText = "Доходы"
            pieChart.animateY(1400, Easing.EaseInOutQuad)
        }

        viewModel.todayCostsOrIncomes.observe(viewLifecycleOwner){
            pieChart = viewModel.loadPieChartData(pieChart, resources.getIntArray(R.array.d_colors))
            binding.graphSec.visibility = View.VISIBLE
        }
    }

    private fun initPieChart() {
        pieChart = binding.graphSec
        pieChart = viewModel.setupPieChart(pieChart)
    }

    private fun initListeners(){
        binding.addCategory.setOnClickListener{
            findNavController().navigate(SecondFDirections.actionSecondF2ToAddCategory2(adapter.categories.size))
        }

        binding.up.setOnClickListener{
            if (binding.category.text.isEmpty())
                Toast.makeText(requireContext(), "Выберите категорию", Toast.LENGTH_LONG).show()
            else
                findNavController().navigate(SecondFDirections.actionSecondF2ToIncomeAndCost(INCOME, binding.category.text.toString()))
        }

        binding.down.setOnClickListener{
            if (binding.category.text.isEmpty())
                Toast.makeText(requireContext(), "Выберите категорию", Toast.LENGTH_LONG).show()
            else
                findNavController().navigate(SecondFDirections.actionSecondF2ToIncomeAndCost(COST, binding.category.text.toString()))
        }

        binding.changeButt.setOnClickListener{
            viewModel.isCostsOrIncomes.value = !viewModel.isCostsOrIncomes.value!!
        }
    }

    private fun initRvAdapter(){
        adapter = CategoriesAdapter(object : CategoryActionListener {
            override fun onCategoryDelete(category: CategoryDb) {
                viewModel.delete(category){
                    Toast.makeText(requireContext(), "Категория удалена", Toast.LENGTH_LONG).show()
                }
                viewModel.selectedPos.value = null
            }

            override fun onCategoryClick(category: CategoryDb) {
                viewModel.selectedPos.value = category.idForSort
            }
        })
        binding.recyclerView2.layoutManager = LinearLayoutManager(context)
        binding.recyclerView2.adapter = adapter
    }
}