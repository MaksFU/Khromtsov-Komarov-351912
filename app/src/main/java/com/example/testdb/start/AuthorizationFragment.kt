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
import com.example.testdb.databinding.FragmentAuthorizationBinding
import com.example.testdb.modelsDb.WorkerDb
import com.example.testdb.utils.*

class AuthorizationFragment : Fragment() {
    lateinit var binding: FragmentAuthorizationBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAuthorizationBinding.inflate(layoutInflater, container, false)
        initListeners()
        return binding.root
    }

    private fun initListeners() {
        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.buttonAuth.setOnClickListener {
            val mail = binding.inputEmail.text.toString()
            val pass = binding.inputPasswordReg.text.toString()
            when(""){
                mail -> Toast.makeText(requireContext(), "Заполните поле \"Почта\"", Toast.LENGTH_LONG).show()
                pass -> Toast.makeText(requireContext(), "Заполните поле \"Пароль\"", Toast.LENGTH_LONG).show()
                else -> {
                    USER = WorkerDb(email = mail, password = pass)
                    setFragmentResult(REQUEST_KEY, bundleOf())
                    findNavController().popBackStack()
                }
            }
        }
    }

    companion object{
        const val REQUEST_KEY = "REQUEST_KEY_AUTH"
    }
}