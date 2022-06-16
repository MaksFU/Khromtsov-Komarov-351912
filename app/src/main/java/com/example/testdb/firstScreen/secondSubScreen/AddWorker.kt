package com.example.testdb.firstScreen.secondSubScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.testdb.databinding.FragmentAddWorkerBinding
import com.example.testdb.modelsDb.WorkerDb

class AddWorker : Fragment() {
    private lateinit var binding: FragmentAddWorkerBinding
    private lateinit var viewModel: AddWorkerViewModel
    private val args: AddWorkerArgs by navArgs()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAddWorkerBinding.inflate(layoutInflater, container, false)
        initViewModel()
        initListeners()
        return binding.root
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(AddWorkerViewModel :: class.java)
    }

    private fun initListeners() {
        binding.buttonApply.setOnClickListener {
            if (args.size > 10) {
                Toast.makeText(requireContext(),
                    "Достигнуто максимальное количество работников",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            val email = binding.mailInput.text.toString()
            val password = binding.passwordInput.text.toString()
            val fName = binding.fNameInput.text.toString()
            val sName = binding.sNameInput.text.toString()

            when ("") {
                sName -> Toast.makeText(requireContext(), "Заполните поле \"Фамилия\"", Toast.LENGTH_LONG).show()
                fName -> Toast.makeText(requireContext(), "Заполните поле \"Имя\"", Toast.LENGTH_LONG).show()
                email -> Toast.makeText(requireContext(), "Заполните поле \"Почта\"", Toast.LENGTH_LONG).show()
                password -> Toast.makeText(requireContext(), "Заполните поле \"пароль\"", Toast.LENGTH_LONG).show()
                else -> {
                    val worker = WorkerDb(
                        idForSort = args.size, fName = fName, sName = sName, tName = binding.tNameInput.text.toString(),
                        position = binding.posInput.text.toString(), email = email,
                        password = password, firstPass = password, admin = binding.isAdmin1.isChecked
                    )
                    viewModel.insert(worker) {
                        Toast.makeText(requireContext(), "Пользователь добавлен", Toast.LENGTH_LONG).show()
                        findNavController().popBackStack()
                    }
                }
            }
        }

        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
