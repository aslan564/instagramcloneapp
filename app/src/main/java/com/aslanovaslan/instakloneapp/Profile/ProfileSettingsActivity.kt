package com.aslanovaslan.instakloneapp.Profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.aslanovaslan.instakloneapp.R
import com.aslanovaslan.instakloneapp.utils.BottonNavigationHelper
import kotlinx.android.synthetic.main.activity_profile_settings.*
import kotlinx.android.synthetic.main.activity_profile_settings.bnve
import kotlinx.android.synthetic.main.activity_profileuser.*

class ProfileSettingsActivity : AppCompatActivity() {
    private val TAG = "ProfileSettingsActivity"
    private val ACTIVITY_NO = 4


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_settings)

        setupFragmentMoveNavigation()
        setNavigationBotomMenu()
    }

    private fun setupFragmentMoveNavigation() {

        tvEditProfileInfo.setOnClickListener {
            profileUserSettingContainer.visibility=View.GONE
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.profileSettingsContainerLayout, ProfileEditFragment())
            transaction.addToBackStack("addProfileSettFragment")
            transaction.commit()
        }

        tvLogoutAccount.setOnClickListener {
            profileUserSettingContainer.visibility=View.GONE
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.profileSettingsContainerLayout, LogOutFragment())
            transaction.addToBackStack("addLogOutFragment")
            transaction.commit()
        }
        backProfileImg.setOnClickListener {
            val intent = Intent(
                this,
                ProfileActivity::class.java
            ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        profileUserSettingContainer.visibility=View.VISIBLE
    }
    fun setNavigationBotomMenu() {
        BottonNavigationHelper.setupButtonNavigationLocation(this, bnve)
        val menu = bnve.menu
        val menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.isChecked = true

    }

}