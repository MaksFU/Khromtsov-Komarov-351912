package com.example.testdb.firstScreen.secondSubScreen

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.testdb.R
import com.example.testdb.databinding.FragmentWorkerDetailsBinding
import com.github.mikephil.charting.charts.PieChart
import android.widget.AdapterView
import androidx.navigation.fragment.navArgs
import com.github.mikephil.charting.animation.Easing
import java.util.*

class WorkerDetails : Fragment() {
    private lateinit var binding: FragmentWorkerDetailsBinding
    private lateinit var viewModel: WorkerDetailsViewModel
    private lateinit var pieChart: PieChart
    private lateinit var observer: Observer<Map<String, Map<String, Long>>>
    private val args: WorkerDetailsArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentWorkerDetailsBinding.inflate(layoutInflater, container, false)
        initViewModel()
        initPieChart()
        initSpinnerAdapter()
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
        observer = Observer { pieChart = viewModel.loadPieChartData(pieChart) }
        viewModel =ViewModelProvider(this, WorkerDetailsViewModelFactory(args.workerId))
            .get(WorkerDetailsViewModel::class.java)

        viewModel.sum.observe(viewLifecycleOwner){ binding.sumV.text = it.toString() }
        viewModel.date.observe(viewLifecycleOwner) {
            binding.dateTxt.text = it
            viewModel.changeType()
        }
        viewModel.day.observe(viewLifecycleOwner) {
            if (!viewModel.isLiveDataOrNot.value!!)
                viewModel.loadPieChartData(pieChart)
        }
        viewModel.isLiveDataOrNot.observe(viewLifecycleOwner) {
            if (it){
                viewModel.costsOrIncomes.observe(viewLifecycleOwner, observer)
            }
            else {
                viewModel.costsOrIncomes.removeObserver(observer)
            }
        }
        viewModel.isCostsOrIncomes.observe(viewLifecycleOwner){
            if (viewModel.isLiveDataOrNot.value!!)
                viewModel.period.value = viewModel.period.value
            else {
                viewModel.changeType()
            }
            if(it){
                binding.changeG.setImageResource(R.drawable.c)
                pieChart.centerText = "Траты"
            }
            else{
                binding.changeG.setImageResource(R.drawable.i)
                pieChart.centerText = "Доходы"
            }
        }
    }

    private fun initPieChart() {
        pieChart = binding.graph1in4
        viewModel.setupPieChart(pieChart)
    }

    private fun initSpinnerAdapter() {
        val adapter = ArrayAdapter.createFromResource(requireContext(),
            R.array.periods, R.layout.simple_spinner_item)

        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter
        binding.spinner.setSelection(viewModel.spinnerPos.value!!)
    }

    private fun initListeners() {
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                when(parent.getItemAtPosition(position).toString()){
                    "Конкретный день" -> {
                        viewModel.spinnerPos.value = 0
                        viewModel.period.value = 0
                        viewModel.isLiveDataOrNot.value = false
                        viewModel.date.value = viewModel.date.value
                        binding.dateTxt.visibility = View.VISIBLE
                        binding.dateImg.visibility = View.VISIBLE
                    }
                    "Неделя" -> {
                        viewModel.spinnerPos.value = 1
                        clearDateSetPeriod(7)
                    }
                    "Месяц" -> {
                        viewModel.spinnerPos.value = 2
                        clearDateSetPeriod(30)
                    }
                    "Год" -> {
                        viewModel.spinnerPos.value = 3
                        clearDateSetPeriod(360)
                    }

                }
                pieChart.animateY(1400, Easing.EaseInOutQuad)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            private fun clearDateSetPeriod(p: Int){
                if (viewModel.isLiveDataOrNot.value == false)
                    viewModel.isLiveDataOrNot.value = true
                viewModel.period.value = p
                binding.dateTxt.visibility = View.GONE
                binding.dateImg.visibility = View.INVISIBLE
            }
        }

        binding.changeG.setOnClickListener{
            viewModel.isCostsOrIncomes.value = !viewModel.isCostsOrIncomes.value!!
            pieChart.animateY(1400, Easing.EaseInOutQuad)
        }

        binding.dateImg.setOnClickListener{
            setDate()
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
            viewModel.isLiveDataOrNot.value = false
            viewModel.date.value = str
            pieChart.animateY(1400, Easing.EaseInOutQuad)
        }, year, month, day)
        dpd.show()
    }
}