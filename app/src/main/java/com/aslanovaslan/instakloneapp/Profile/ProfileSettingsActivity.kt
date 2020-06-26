package com.aslanovaslan.instakloneapp.Profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aslanovaslan.instakloneapp.Home.CameraFragment
import com.aslanovaslan.instakloneapp.Home.HomeFragment
import com.aslanovaslan.instakloneapp.Home.MessageFragment
import com.aslanovaslan.instakloneapp.R
import com.aslanovaslan.instakloneapp.utils.BottonNavigationHelper
import com.aslanovaslan.instakloneapp.utils.HomeFragmentPagerAdapter
import kotlinx.android.synthetic.main.activity_profile_settings.*

class ProfileSettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_settings)

        setNavigationBotomMenu()
    }
    fun setNavigationBotomMenu() {
        BottonNavigationHelper.setupButtonNavigationLocation(this,bnve)

    }

}