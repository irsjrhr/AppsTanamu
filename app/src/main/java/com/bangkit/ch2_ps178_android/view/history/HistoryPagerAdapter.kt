package com.bangkit.ch2_ps178_android.view.history

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class HistoryPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ActiveOrderFragment()
            1 -> ListOrderFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}