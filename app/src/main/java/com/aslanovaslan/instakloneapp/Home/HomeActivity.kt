package com.aslanovaslan.instakloneapp.Home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aslanovaslan.instakloneapp.R
import com.aslanovaslan.instakloneapp.utils.BottonNavigationHelper
import com.aslanovaslan.instakloneapp.utils.HomeFragmentPagerAdapter
import com.aslanovaslan.instakloneapp.utils.UniversalImageLoader
import com.nostra13.universalimageloader.core.ImageLoader
import kotlinx.android.synthetic.main.activity_main.*

class HomeActivity : AppCompatActivity() {
    private val TAG = "HomeActivity"
    private val ACTIVITY_NO = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initImageLoader()
        setNavigationBotomMenu()
        setUpFragmentPagerView()
    }


    fun setNavigationBotomMenu() {
        BottonNavigationHelper.setupButtonNavigationLocation(this, bnve)
        val menu = bnve.menu
        val menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.isChecked = true

    }

    private fun initImageLoader() {
        val universalImageLoader = UniversalImageLoader(this)
        ImageLoader.getInstance().init(universalImageLoader.mConfig)
    }

    private fun setUpFragmentPagerView() {
        val homeFragmentPagerAdapter: HomeFragmentPagerAdapter =
            HomeFragmentPagerAdapter(supportFragmentManager)
        homeFragmentPagerAdapter.addFragmentList(CameraFragment())
        homeFragmentPagerAdapter.addFragmentList(HomeFragment())
        homeFragmentPagerAdapter.addFragmentList(MessageFragment())

        homeViewPager.adapter = homeFragmentPagerAdapter

        homeViewPager.setCurrentItem(1)

    }
}