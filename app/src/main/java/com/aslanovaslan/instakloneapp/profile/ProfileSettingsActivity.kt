package com.aslanovaslan.instakloneapp.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.aslanovaslan.instakloneapp.R
import com.aslanovaslan.instakloneapp.login.LoginActivity
import com.aslanovaslan.instakloneapp.utils.BottonNavigationHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_profile_settings.*
import kotlinx.android.synthetic.main.activity_profile_settings.bnve

class ProfileSettingsActivity : AppCompatActivity(), View.OnClickListener {

	private val ACTIVITY_NO = 4
	private lateinit var mAuth: FirebaseAuth

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_profile_settings)
		initializedAllVariable()


		setNavigationBotomMenu()
	}

	private fun initializedAllVariable() {
        mAuth=Firebase.auth
		tvLogoutAccount.setOnClickListener(this)
        backProfileImg.setOnClickListener(this)
        tvEditProfileInfo.setOnClickListener(this)
	}

	private fun setupFragmentMoveNavigation() {
			profileUserSettingContainer.visibility = View.GONE
			val transaction = supportFragmentManager.beginTransaction()
			transaction.replace(R.id.profileSettingsContainerLayout, ProfileEditFragment())
			transaction.addToBackStack("addProfileSettFragment")
			transaction.commit()

	}
    private fun backProfileFromImgIcon() {
        val intent = Intent(
            this,
            ProfileActivity::class.java
        ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
    }

	override fun onBackPressed() {
		super.onBackPressed()
		profileUserSettingContainer.visibility = View.VISIBLE
	}

	fun setNavigationBotomMenu() {
		BottonNavigationHelper.setupButtonNavigationLocation(this, bnve)
		val menu = bnve.menu
		val menuItem = menu.getItem(ACTIVITY_NO)
		menuItem.isChecked = true

	}

	override fun onClick(v: View?) {
		when (v!!.id) {
			R.id.tvLogoutAccount -> {
				logOutFromAppGoTooLogin()
			}
            R.id.backProfileImg->{
                backProfileFromImgIcon()
            }
            R.id.tvEditProfileInfo->{
                setupFragmentMoveNavigation()
            }
		}
	}


    private fun logOutFromAppGoTooLogin() {
		val dialog = LogOutFragment()
		dialog.show(supportFragmentManager, "showLogotDialog")
		Log.e(TAG, "logOutFromAppGoTooLogin-------------: ${dialog.toString()}")
	}

	private fun setUpAuthListener(user: FirebaseUser?) {

		if (user == null) {
			val intent = Intent(
				this@ProfileSettingsActivity,
				LoginActivity::class.java
			).addFlags(
				Intent.FLAG_ACTIVITY_NO_ANIMATION
						or Intent.FLAG_ACTIVITY_CLEAR_TOP
						or Intent.FLAG_ACTIVITY_NEW_TASK
						or Intent.FLAG_ACTIVITY_CLEAR_TASK
			)
			startActivity(intent)
			finish()
		}
	}

	override fun onStart() {
		super.onStart()
		val currentUser = mAuth.currentUser
		Log.e(TAG, "onStart: - HomeActivity deyiz")
		setUpAuthListener(currentUser)
	}

	override fun onStop() {
		super.onStop()
		val currentUser = mAuth.currentUser
		if (mAuth.currentUser != null) {
			val user = FirebaseAuth.getInstance().currentUser!!
			setUpAuthListener(user)
		}
	}

	companion object {
		private val TAG = "ProfileSettingsActivity"
	}

}