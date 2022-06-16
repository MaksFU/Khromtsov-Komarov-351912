package com.example.testdb.secondScreen

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.testdb.R
import com.example.testdb.databinding.FragmentIncomeOrCostBinding
import com.example.testdb.utils.*
import java.text.SimpleDateFormat
import java.util.*

class IncomeAndCost  : Fragment() {
    private lateinit var binding: FragmentIncomeOrCostBinding
    private lateinit var viewModel : IncomeAndCostViewModel
    private val args: IncomeAndCostArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentIncomeOrCostBinding.inflate(layoutInflater, container, false)
        initViewModel()
        initView()
        initListeners()
        return binding.root
    }

    private fun initViewModel() {
        viewModel=ViewModelProvider(this, IncomeAndCostViewModelFactory(args.catName, args.incomeOrCost))
            .get(IncomeAndCostViewModel::class.java)

        if (USER.admin)
            viewModel.mutCurInOrCost.observe(viewLifecycleOwner){
                binding.curCostOrIncInCatV.text = it.toString()
            }
        else
            viewModel.curInOrCost.observe(viewLifecycleOwner){
                binding.curCostOrIncInCatV.text = it.toString()
            }

        viewModel.date.observe(viewLifecycleOwner){
            binding.chosenDate.text = it
        }
    }

    private fun initView() {
        binding.chosenCatV.text = viewModel.cat
        if (USER.admin)
            binding.curCostOrIncInCat.text = "Текущее общее значение:"
        if (args.incomeOrCost == INCOME) return
        else with(binding.title){
            text = "Ваш расход"
            setTextColor(
                ContextCompat.getColor(requireContext(), R.color.red)
            )
            binding.newCostOrInc.text = "Новый расход:"
        }
        binding.chosenDate.text = viewModel.date.value
    }

    private fun initListeners() {
        binding.buttonApply.setOnClickListener {
            if (binding.enterValue.text.isEmpty())
            {
                Toast.makeText(requireContext(), "Введите число", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val date = binding.chosenDate.text.toString()
            val timestamp : Long = if (date == currDate())
                -1
            else
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(date)?.time ?: -1L

            Log.d("ув2",timestamp.toString())
            viewModel.insertCostOrInc(binding.enterValue.text.toString().toFloat(), timestamp,
                binding.noteValue.text.toString()) {
                val str = if (args.incomeOrCost == INCOME)  "Доходы добавлены" else "Расходы добавлены"
                Toast.makeText(requireContext(), str, Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }

        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
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
            if (str > exactDate()) {
                Toast.makeText(
                    requireContext(),
                    "Вы не можете добавлять траты/расходы в будущее",
                    Toast.LENGTH_LONG
                ).show()
                return@OnDateSetListener
            }
            viewModel.date.value = str
        }, year, month, day)
        dpd.show()
    }
}