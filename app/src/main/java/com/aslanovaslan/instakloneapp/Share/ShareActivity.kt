package com.aslanovaslan.instakloneapp.Share

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aslanovaslan.instakloneapp.R
import com.aslanovaslan.instakloneapp.utils.SharedPagerAdapter
import kotlinx.android.synthetic.main.activity_share.*

class ShareActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_share)

		setupNavigationShared();
	}

	private fun setupNavigationShared() {
		var tabName:ArrayList<String> = ArrayList()
		tabName.add("GALERY")
		tabName.add("CAMERA")
		tabName.add("VIDEO")
		val sharedPagerAdapter = SharedPagerAdapter(supportFragmentManager,tabName)
		sharedPagerAdapter.addFragmentShared(SharedGaleryFragment())
		sharedPagerAdapter.addFragmentShared(SharedCameraFragment())
		sharedPagerAdapter.addFragmentShared(SharedViedeoFragment())

		viewPagerShared.adapter=sharedPagerAdapter
		tabLayoutShared.setupWithViewPager(viewPagerShared)
		//viewPagerShared.setCurrentItem(1,true)
	}

	companion object {
		private const val TAG = "ShareActivity"
	}
}