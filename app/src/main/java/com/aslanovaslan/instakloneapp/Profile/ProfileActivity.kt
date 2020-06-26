package com.aslanovaslan.instakloneapp.Profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aslanovaslan.instakloneapp.R
import com.aslanovaslan.instakloneapp.utils.BottonNavigationHelper
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
    }


    /*
    *
    *  <androidx.constraintlayout.widget.ConstraintLayout
                android:layout_width="match_parent"
                android:layout_height="50sp">


            </androidx.constraintlayout.widget.ConstraintLayout>
    *
    *
    * */
    private fun setupSettingsProfileUser() {
        accountSettingsImageView.setOnClickListener {
            val intent = Intent(this, ProfileSettingsActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }
    }

    fun setNavigationBotomMenu() {
        BottonNavigationHelper.setupButtonNavigationLocation(this, bnve)
        val menu=bnve.menu
        val menuItem=menu.getItem(ACTIVITY_NO)
        menuItem.isChecked = true

    }

}