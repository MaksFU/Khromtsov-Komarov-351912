package com.example.testdb.start

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.example.testdb.databinding.FragmentRegistrationBinding
import com.example.testdb.modelsDb.WorkerDb
import com.example.testdb.utils.USER

class RegistrationFragment : Fragment() {
    lateinit var binding: FragmentRegistrationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistrationBinding.inflate(layoutInflater, container, false)
        initListeners()
        return binding.root
    }
    private fun initListeners() {
        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.buttonReg.setOnClickListener {
            val fName = binding.fNameInput.text.toString()
            val sName = binding.sNameInput.text.toString()
            val mail = binding.mailInput.text.toString()
            val pass = binding.passwordInput.text.toString()

            when(""){
                sName -> Toast.makeText(requireContext(), "Заполните поле \"Фамилия\"", Toast.LENGTH_LONG).show()
                fName -> Toast.makeText(requireContext(), "Заполните поле \"Имя\"", Toast.LENGTH_LONG).show()
                mail -> Toast.makeText(requireContext(), "Заполните поле \"Почта\"", Toast.LENGTH_LONG).show()
                pass -> Toast.makeText(requireContext(), "Заполните поле \"Пароль\"", Toast.LENGTH_LONG).show()
                else ->{
                    USER = WorkerDb(idForSort = 0, admin = true, fName = fName, sName = sName,
                        position = "Руководитель", tName = binding.tNameInput.text.toString(),
                            password = pass, email = mail, firstPass = pass)
                    setFragmentResult(REQUEST_KEY, bundleOf())
                    findNavController().popBackStack()
                }
            }
        }
    }

    companion object{
        const val REQUEST_KEY = "REQUEST_KEY_REG"
    }
}