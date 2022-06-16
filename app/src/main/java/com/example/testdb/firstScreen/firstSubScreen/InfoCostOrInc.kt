package com.example.testdb.firstScreen.firstSubScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testdb.R
import com.example.testdb.databinding.FragmentInfoCostOrIncBinding
import com.example.testdb.firstScreen.FirstFDirections
import com.example.testdb.modelsDb.InfoDb

class InfoCostOrInc : Fragment() {
    private lateinit var binding: FragmentInfoCostOrIncBinding
    private lateinit var spinnerAdapter: InfoAdapter
    private lateinit var viewModel: InfoCostOrIncViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentInfoCostOrIncBinding.inflate(layoutInflater, container, false)
        initViewModel()
        initRvAdapter()
        initSpinnerAdapter()
        initListeners()
        return binding.root
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(InfoCostOrIncViewModel::class.java)

        viewModel.infos.observe(viewLifecycleOwner){
            spinnerAdapter.infos = it
            if (viewModel.lastCh.value!! != viewModel.newCh.value!!) {
                binding.recyclerView.scrollToPosition(spinnerAdapter.infos.size - 1)
                viewModel.lastCh.value = viewModel.newCh.value!!
            }
        }
    }

    private fun initRvAdapter() {
        spinnerAdapter = InfoAdapter(object : InfoActionListener {
            override fun onInfoDetails(info: InfoDb) {
                findNavController().navigate(FirstFDirections.actionFirstF2ToInfoDetails(info))
            }
        })
        val mLayoutManager = LinearLayoutManager(context)
        mLayoutManager.stackFromEnd = true
        binding.recyclerView.layoutManager = mLayoutManager
        binding.recyclerView.adapter = spinnerAdapter
    }

    private fun initSpinnerAdapter() {
        val adapterS = ArrayAdapter.createFromResource(requireContext(),
            R.array.periods, R.layout.simple_spinner_item)
        adapterS.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapterS
    }

    private fun initListeners() {
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                when(parent.getItemAtPosition(position).toString()){
                    "Конкретный день" -> {
                        viewModel.spinnerPos.value = 0
                    }
                    "Неделя" -> {
                        viewModel.spinnerPos.value = 1

                    }
                    "Месяц" -> {
                        viewModel.spinnerPos.value = 2

                    }
                    "Год" -> {
                        viewModel.spinnerPos.value = 3
                    }

                }
                viewModel.lastCh.value = viewModel.newCh.value
                viewModel.newCh.value = parent.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
}