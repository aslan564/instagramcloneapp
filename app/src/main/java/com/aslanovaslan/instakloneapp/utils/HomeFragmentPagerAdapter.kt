package com.aslanovaslan.instakloneapp.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

@Suppress("DEPRECATION")
class HomeFragmentPagerAdapter (fm: FragmentManager):
    FragmentPagerAdapter(fm) {
    private val myFragmentList = ArrayList<Fragment>()
    override fun getItem(position: Int): Fragment {
        return myFragmentList[position]
    }

    override fun getCount(): Int {
        return myFragmentList.size
    }

    fun addFragmentList(fragment: Fragment) {
        this.myFragmentList.add(fragment)
    }
}