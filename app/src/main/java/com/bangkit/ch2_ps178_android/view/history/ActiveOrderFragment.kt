package com.bangkit.ch2_ps178_android.view.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bangkit.ch2_ps178_android.R

class ActiveOrderFragment : Fragment() {
    class ActiveOrderFragment : Fragment() {
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            return inflater.inflate(R.layout.fragment_active_order, container, false)
        }
    }
}