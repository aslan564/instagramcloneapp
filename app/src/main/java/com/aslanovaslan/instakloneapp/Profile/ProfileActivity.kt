package com.aslanovaslan.instakloneapp.Profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.aslanovaslan.instakloneapp.R
import com.aslanovaslan.instakloneapp.utils.BottonNavigationHelper
import com.aslanovaslan.instakloneapp.utils.UniversalImageLoader
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_profileuser.*
import kotlinx.android.synthetic.main.activity_profileuser.bnve

class ProfileActivity : AppCompatActivity() {
    private val TAG = "ProfileActivity"
    private val ACTIVITY_NO = 4
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profileuser)
        setupSettingsProfileUser()
        setNavigationBotomMenu();
        setupProfilePhoto()
    }

    private fun setupProfilePhoto() {
        val imageUri ="instagram.fgyd5-2.fna.fbcdn.net/v/t51.2885-15/e35/p1080x1080/105946825_679732149545791_4665835235260013131_n.jpg?_nc_ht=instagram.fgyd5-2.fna.fbcdn.net&_nc_cat=107&_nc_ohc=D0gtCE0w79EAX9I1SXB&oh=22cd5b2c0545cd962a81bf66ab9eced1&oe=5F1FDD73"
        val minaImageUri ="instagram.fgyd5-2.fna.fbcdn.net/v/t51.2885-15/e35/p1080x1080/105418575_570653827216423_7530459996637786778_n.jpg?_nc_ht=instagram.fgyd5-2.fna.fbcdn.net&_nc_cat=107&_nc_ohc=6R2jSdeINRIAX_zNM2v&oh=0ebe2bcf5c73b8460203b12e75757a33&oe=5F20C3F6"
        UniversalImageLoader.setImage(imageUri,profileImageCricle,progressBarProfActivt,"https://")
    }

    private fun setupSettingsProfileUser() {
        accountSettingsImageView.setOnClickListener {
            val intent = Intent(this, ProfileSettingsActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }

        tvEditProfileActivtyButton.setOnClickListener{
            activProfileEditContainerLayout.visibility=View.GONE
            val editFragment=supportFragmentManager.beginTransaction()
            editFragment.replace(R.id.activityEditProfileContainer,ProfileEditFragment())
            editFragment.addToBackStack("bacPressOnProfileActivity")
            editFragment.commit()
        }
    }

    override fun onBackPressed() {
        activProfileEditContainerLayout.visibility=View.VISIBLE
        super.onBackPressed()
    }
    fun setNavigationBotomMenu() {
        BottonNavigationHelper.setupButtonNavigationLocation(this, bnve)
        val menu=bnve.menu
        val menuItem=menu.getItem(ACTIVITY_NO)
        menuItem.isChecked = true

    }

}