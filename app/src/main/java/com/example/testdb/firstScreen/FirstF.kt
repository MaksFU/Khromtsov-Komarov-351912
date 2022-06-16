package com.example.testdb.firstScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.testdb.databinding.FragmentFirstFBinding
import com.google.android.material.tabs.TabLayoutMediator

class FirstF : Fragment() {
    lateinit var binding: FragmentFirstFBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFirstFBinding.inflate(layoutInflater, container, false)
        initViewPager()
        return binding.root
    }

    private fun initViewPager() {
        binding.viewPagerFirst.adapter = ViewPagerFirstAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
        TabLayoutMediator(binding.tabLayoutFirst, binding.viewPagerFirst)
        { tab, position ->
            when (position) {
                0 -> tab.text = "Статистика"
                1 -> tab.text = "Коллектив"
            }
        }.attach()
    }
}