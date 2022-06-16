package com.example.testdb.thirdScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.testdb.R
import com.example.testdb.databinding.FragmentPeriodsBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart

class Periods : Fragment(){
    lateinit var binding: FragmentPeriodsBinding
    lateinit var pieChart: PieChart
    private lateinit var viewModel: PeriodsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPeriodsBinding.inflate(layoutInflater, container, false)
        initViewModel()
        initPieChart()
        initListeners()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.graph1in4.visibility = View.VISIBLE
        pieChart.animateY(1400, Easing.EaseInOutQuad)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.graph1in4.visibility = View.INVISIBLE
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this, PeriodsViewModelFactory(arguments?.getString("KEY")!!))
            .get(PeriodsViewModel::class.java)

        viewModel.sum.observe(viewLifecycleOwner){
            binding.textView3.text = it.toString()
        }

        viewModel.costsOrIncomesLD.observe(viewLifecycleOwner){
            pieChart = viewModel.loadPieChartData(pieChart, resources.getIntArray(R.array.d_colors))
        }

        viewModel.isCostsOrIncomes.observe(viewLifecycleOwner){
            if (it) {
                binding.changeG.setImageResource(R.drawable.c)
                pieChart.centerText = "Траты"
            }
            else {
                binding.changeG.setImageResource(R.drawable.i)
                pieChart.centerText = "Доходы"
            }
        }
    }

    private fun initPieChart() {
        pieChart = binding.graph1in4
        pieChart = viewModel.setupPieChart(pieChart)
    }

    private fun initListeners(){
        binding.changeG.setOnClickListener{
            viewModel.isCostsOrIncomes.value = !viewModel.isCostsOrIncomes.value!!
            pieChart.animateY(1400, Easing.EaseInOutQuad)
        }
    }
}