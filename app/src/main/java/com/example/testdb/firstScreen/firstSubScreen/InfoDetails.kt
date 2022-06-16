package com.example.testdb.firstScreen.firstSubScreen

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.testdb.databinding.FragmentInfoDetailsBinding
import com.example.testdb.utils.COST
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class InfoDetails : Fragment() {
    private lateinit var binding: FragmentInfoDetailsBinding
    private val args: InfoDetailsArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentInfoDetailsBinding.inflate(layoutInflater, container, false)
        initView()
        initListeners()
        return binding.root
    }

    private fun initListeners() {
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initView() {
        val dateFormat: DateFormat = SimpleDateFormat("yyyy-mm-dd HH:mm:ss", Locale("ru"))
        val dateNF = SimpleDateFormat("yyyy-mm-dd hh:mm:ss", Locale("ru"))
            .parse(dateFormat.format(Date(args.info.time)))
        val rusDate = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.MEDIUM, Locale("ru"))
            .format(dateNF!!)

        binding.date.text = rusDate
        if (args.info.type == COST) {
            binding.type.text = "Расход"
            binding.type.setTextColor(Color.RED)
        } else{
            binding.type.text = "Доход"
            binding.type.setTextColor(Color.GREEN)
        }
        val n = args.info.sName + " " + args.info.fName + " " + args.info.tName + " ("+ args.info.position +")"
        binding.name.text = n
        binding.catValue.text = args.info.cat
        binding.sumValue.text = args.info.number.toString()
        binding.noteValue.text = args.info.note
    }
}