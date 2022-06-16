package com.example.testdb.thirdScreen

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.testdb.databinding.FragmentThirdFBinding
import com.google.android.material.tabs.TabLayoutMediator

class ThirdF : Fragment() {
    lateinit var binding: FragmentThirdFBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentThirdFBinding.inflate(layoutInflater, container, false)
        initViewPager()
        return binding.root
    }

    private fun initViewPager() {
        binding.viewPagerThird.adapter = ViewPagerThirdAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
        TabLayoutMediator(binding.tabLayoutThird, binding.viewPagerThird)
        { tab, position ->
                when (position) {
                    0 -> tab.text = "День"
                    1 -> tab.text = "Неделя"
                    2 -> tab.text = "Месяц"
                    3 ->tab.text = "Год"
                }
            }.attach()
    }
}