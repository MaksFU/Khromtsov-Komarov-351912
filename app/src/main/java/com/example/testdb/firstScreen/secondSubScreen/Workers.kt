package com.example.testdb.firstScreen.secondSubScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testdb.databinding.FragmentWorkersBinding
import com.example.testdb.firstScreen.FirstFDirections
import com.example.testdb.modelsDb.WorkerDb
import com.example.testdb.utils.USER


class Workers : Fragment() {
    private lateinit var binding: FragmentWorkersBinding
    private lateinit var adapter: WorkersAdapter
    private lateinit var viewModel: WorkersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWorkersBinding.inflate(layoutInflater, container, false)
        initViewModel()
        initRvAdapter()
        initListeners()
        if (!USER.admin)
            binding.addWorker.visibility = View.GONE
        return binding.root
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(WorkersViewModel::class.java)

        viewModel.allWorkers.observe(viewLifecycleOwner){
            adapter.workers = it
        }
    }

    private fun initRvAdapter() {
        adapter = WorkersAdapter(object : WorkerActionListener {
            override fun onWorkerMove(idClickedWorker: String, idNextWorker: String,
                                      idFSClickedWorker: Int, idFSNextWorker: Int) {
                viewModel.moveUser(idClickedWorker, idNextWorker, idFSClickedWorker, idFSNextWorker)
            }

            override fun onWorkerDelete(worker: WorkerDb) {
                if (USER.admin)
                viewModel.delete(worker){
                    Toast.makeText(requireContext(), "Рабочий удалён", Toast.LENGTH_LONG).show()
                }
            }

            override fun onWorkerDetails(worker: WorkerDb) {
                if (USER.admin)
                    findNavController().navigate(FirstFDirections.actionFirstF2ToWorkerDetails(worker.id))
            }

            override fun onWorkerChange(worker: WorkerDb) {
                if (USER.admin)
                    findNavController().navigate(FirstFDirections.actionFirstF2ToWorkerData(worker))
            }
        })

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
    }

    private fun initListeners() {
        if(USER.admin)
            binding.addWorker.setOnClickListener{
                findNavController().navigate(FirstFDirections.actionFirstF2ToAddWorker2(adapter.workers.size))
            }
    }
}