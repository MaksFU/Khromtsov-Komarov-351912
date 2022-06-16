package com.example.testdb.firstScreen

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.testdb.firstScreen.firstSubScreen.InfoCostOrInc
import com.example.testdb.firstScreen.secondSubScreen.Workers

class ViewPagerFirstAdapter(fm: FragmentManager, l: Lifecycle): FragmentStateAdapter(fm,l) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> InfoCostOrInc()
            else -> Workers()
        }
    }
}