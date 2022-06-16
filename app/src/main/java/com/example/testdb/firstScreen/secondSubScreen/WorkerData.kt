package com.example.testdb.firstScreen.secondSubScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.testdb.databinding.FragmentWorkerDataBinding
import com.example.testdb.utils.USER

class WorkerData : Fragment() {
    private lateinit var binding: FragmentWorkerDataBinding
    private lateinit var viewModel: WorkerDataViewModel
    private val args: WorkerDataArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWorkerDataBinding.inflate(layoutInflater, container, false)
        initView()
        initViewModel()
        initListeners()
        return binding.root
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(WorkerDataViewModel :: class.java)
    }

    private fun initListeners() {
        binding.backB.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.applyB.setOnClickListener {
            viewModel.updateRules(args.workerInf.id, binding.isAdmin.isChecked, binding.posVEdit.text.toString())
            findNavController().popBackStack()
        }
    }

    private fun initView() {
        val fName = args.workerInf.sName + " " + args.workerInf.fName + " " + args.workerInf.tName
        with(binding){
            name.text = fName
            posVEdit.setText(args.workerInf.position)
            posVTxt.text = args.workerInf.position
            mailV.text = args.workerInf.email
            passV.text = args.workerInf.firstPass
            isAdmin.isChecked = args.workerInf.admin
            if ((args.workerInf.admin and (USER.id == USER.bossId)) or !args.workerInf.admin) {
                posVEdit.visibility = View.VISIBLE
                posVTxt.visibility = View.GONE
                if ((USER.id == USER.bossId) == (USER.bossId == args.workerInf.id))
                    isAdmin.isEnabled = false
            }
            else {
                isAdmin.isEnabled = false
                applyB.visibility = View.GONE
            }
        }
    }
}