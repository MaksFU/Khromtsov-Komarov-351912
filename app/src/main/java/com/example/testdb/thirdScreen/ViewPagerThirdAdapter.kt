package com.example.testdb.thirdScreen

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.testdb.utils.K_MONTH
import com.example.testdb.utils.K_WEEK
import com.example.testdb.utils.K_YEAR

class ViewPagerThirdAdapter(fm: FragmentManager, l: Lifecycle): FragmentStateAdapter(fm,l) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
       return when(position){
            0->{ ExactDay()}
            1-> Periods().apply {arguments = bundleOf("KEY" to K_WEEK)}
            2-> Periods().apply {arguments = bundleOf("KEY" to K_MONTH)}
            else -> Periods().apply {arguments = bundleOf("KEY" to K_YEAR)}
       }
    }
}


