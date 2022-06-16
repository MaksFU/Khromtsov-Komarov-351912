package com.example.testdb.start

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.testdb.databinding.FragmentDeveloperInfoBinding

class DeveloperInfoFragment : Fragment() {
    lateinit var binding: FragmentDeveloperInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDeveloperInfoBinding.inflate(layoutInflater, container, false)
        initListeners()
        return binding.root
    }

    private fun initListeners() {
        binding.buttonB.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}