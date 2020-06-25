package com.aslanovaslan.instakloneapp.Home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aslanovaslan.instakloneapp.R
import com.aslanovaslan.instakloneapp.utils.BottonNavigationHelper
import com.aslanovaslan.instakloneapp.utils.HomeFragmentPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class HomeActivity : AppCompatActivity() {
    private val TAG="HomeActivity"
    private val ACTIVITY_NO=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setNavigationBotomMenu()
        setUpFragmentPagerView()
    }



    fun setNavigationBotomMenu() {
        BottonNavigationHelper.setupButtonNavigationLocation(this,bnve)

    }

    private fun setUpFragmentPagerView() {
        val homeFragmentPagerAdapter: HomeFragmentPagerAdapter =
            HomeFragmentPagerAdapter(supportFragmentManager)
        homeFragmentPagerAdapter.addFragmentList(CameraFragment())
        homeFragmentPagerAdapter.addFragmentList(HomeFragment())
        homeFragmentPagerAdapter.addFragmentList(MessageFragment())

        homeViewPager.adapter=homeFragmentPagerAdapter

        homeViewPager.setCurrentItem(1)

    }
}