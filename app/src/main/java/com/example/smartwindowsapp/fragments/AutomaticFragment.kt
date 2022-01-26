package com.example.smartwindowsapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.smartwindowsapp.R
import com.example.smartwindowsapp.fragments.adapters.ViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_automatic.*


class AutomaticFragment : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_automatic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpTabs()


    }

    private fun setUpTabs(){
        val adapter = ViewPagerAdapter(supportFragmentManager = parentFragmentManager)
        adapter.addFragment(WeekdayFragment(), "Weekday")
        adapter.addFragment(WeekendFragment(), "Weekend")
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)

    }
}