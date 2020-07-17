package com.aslanovaslan.instakloneapp.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.aslanovaslan.instakloneapp.R
import com.aslanovaslan.instakloneapp.login.LoginActivity
import com.aslanovaslan.instakloneapp.utils.BottonNavigationHelper
import com.aslanovaslan.instakloneapp.utils.HomeFragmentPagerAdapter
import com.aslanovaslan.instakloneapp.utils.UniversalImageLoader
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.nostra13.universalimageloader.core.ImageLoader
import kotlinx.android.synthetic.main.activity_main.*

class HomeActivity : AppCompatActivity() {

	private lateinit var mAuth: FirebaseAuth
	private val ACTIVITY_NO = 0
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		mAuth = Firebase.auth
		setUpAuthListener(mAuth.currentUser)
		initImageLoader()
		setNavigationBottomMenu()
		setUpFragmentPagerView()
	}


	private fun setNavigationBottomMenu() {
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

		homeViewPager.currentItem = 1

	}

	private fun setUpAuthListener(user: FirebaseUser?) {
		Log.d(
			TAG,
			"setUpAuthListener: - ---------------------bura Lisenerdi  -----------------------------------girdi"
		)
		if (mAuth.currentUser == null) {
			val intent = Intent(
				this@HomeActivity,
				LoginActivity::class.java
			).addFlags(
				Intent.FLAG_ACTIVITY_NO_ANIMATION
			)
			intent.addFlags(
				Intent.FLAG_ACTIVITY_CLEAR_TOP
			)
			startActivity(intent)
			finish()
		}
	}

	override fun onStart() {
		super.onStart()
		Log.e(
			TAG,
			"onStart: - ---------------------HomeActivity -----------------------------------deyiz"
		)

	}

	override fun onResume() {
		super.onResume()
		setUpAuthListener(mAuth.currentUser)
		Log.d(
			TAG,
			"onResume: - ---------------------HomeActivity -----------------------------------deyiz"
		)
	}

	override fun onStop() {
		super.onStop()
		if (mAuth.currentUser != null) {
			val user = FirebaseAuth.getInstance().currentUser!!
			setUpAuthListener(user)
		}
	}

	companion object {
		private val TAG = "HomeActivity"
	}
}