package com.example.testdb.thirdScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.testdb.R
import com.example.testdb.databinding.FragmentExactDayBinding
import android.app.DatePickerDialog
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import java.util.Calendar

class ExactDay : Fragment() {
    private lateinit var binding: FragmentExactDayBinding
    private lateinit var viewModel: ExactDayViewModel
    lateinit var pieChart: PieChart

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentExactDayBinding.inflate(layoutInflater, container, false)
        initViewModel()
        initPieChart()
        initListeners()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.exactDayGraph.visibility = View.VISIBLE
        pieChart.animateY(1400, Easing.EaseInOutQuad)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.exactDayGraph.visibility = View.INVISIBLE
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(ExactDayViewModel :: class.java)

        viewModel.costOrInc.observe(viewLifecycleOwner){
            pieChart.centerText = viewModel.changeType()
            if (it)
                binding.changeG.setImageResource(R.drawable.c)
            else
                binding.changeG.setImageResource(R.drawable.i)
        }

        viewModel.sum.observe(viewLifecycleOwner){
            binding.sumV.text = it.toString()
        }

        viewModel.date.observe(viewLifecycleOwner){
            binding.dateTx.text = it
        }

        viewModel.day.observe(viewLifecycleOwner){
            viewModel.loadPieChartData(pieChart, resources.getIntArray(R.array.d_colors))
        }
    }

    private fun initPieChart() {
        pieChart = binding.exactDayGraph
        pieChart = viewModel.setupPieChart(pieChart)
    }

    private fun initListeners() {
        binding.imageView.setOnClickListener {
            setDate()
        }

        binding.changeG.setOnClickListener{
            viewModel.costOrInc.value = !viewModel.costOrInc.value!!
            pieChart.animateY(1400, Easing.EaseInOutQuad)
        }
    }

    private fun setDate() {
        val c = Calendar.getInstance()
        //TODO Сделать вчерашнюю дату
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener
        { view, yearC, monthC, dayC ->
            var str = "$yearC-"
            str += if (monthC<10) "0${monthC+1}-" else "${monthC+1}-"
            str += if (dayC<10) "0${dayC}" else "$dayC"
            viewModel.changeType(str)
            pieChart.animateY(1400, Easing.EaseInOutQuad)
            viewModel.date.value = str
        }, year, month, day)
        dpd.show()
    }
}