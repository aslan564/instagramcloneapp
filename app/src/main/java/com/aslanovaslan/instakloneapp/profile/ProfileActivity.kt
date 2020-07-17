package com.aslanovaslan.instakloneapp.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.aslanovaslan.instakloneapp.R
import com.aslanovaslan.instakloneapp.home.HomeActivity
import com.aslanovaslan.instakloneapp.login.LoginActivity
import com.aslanovaslan.instakloneapp.userModel.UserAccount
import com.aslanovaslan.instakloneapp.utils.BottonNavigationHelper
import com.aslanovaslan.instakloneapp.utils.EventBusData
import com.aslanovaslan.instakloneapp.utils.UniversalImageLoader
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_profileuser.*
import kotlinx.android.synthetic.main.activity_profileuser.bnve
import kotlinx.android.synthetic.main.fragment_profile_edit.*
import org.greenrobot.eventbus.EventBus

class ProfileActivity : AppCompatActivity(), View.OnClickListener {
	private val ACTIVITY_NO = 4
	private lateinit var mAuth: FirebaseAuth
	private lateinit var mUser: FirebaseUser
	private  var checkSnapshot: Boolean=false

	private lateinit var mReference: DatabaseReference

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_profileuser)
		initializeAllVariable()
		setNavigationBottomMenu();
		getUserProfileInfo()
	}

	private fun initializeAllVariable() {
		mAuth = Firebase.auth
		mReference = FirebaseDatabase.getInstance().getReference("users")
		profileInfoTv.visibility = View.GONE
		tvWebSite.visibility = View.GONE
		accountSettingsImageView.setOnClickListener(this)
		tvEditProfileActivtyButton.setOnClickListener(this)
	}


	private fun getUserProfileInfo() {
		mReference.child(mAuth.currentUser!!.uid).addValueEventListener(object : ValueEventListener {
			override fun onCancelled(error: DatabaseError) {
				Log.e(TAG, "onCancelled: ", error.toException())
			}

			override fun onDataChange(snapshot: DataSnapshot) {
				if (snapshot.value != null) {
					checkSnapshot=true
					val userData = snapshot.getValue(UserAccount::class.java)
					EventBus.getDefault().postSticky(EventBusData.SendUserProfile(userData!!))
					profileFullNameTv.text = userData.nameSurname
					postProfileTv.text = userData.userAccountDetail!!.post
					followProfileTv.text = userData.userAccountDetail!!.follower
					followerProfileTv.text = userData.userAccountDetail!!.following
					usernameToolbarTextView.text = userData.userName
					if (!userData.userAccountDetail!!.biography.isNullOrEmpty()) {
						profileInfoTv.text = userData.userAccountDetail!!.biography
						profileInfoTv.visibility = View.VISIBLE
					}
					if (!userData.userAccountDetail!!.biography.isNullOrEmpty()) {
						tvWebSite.text = userData.userAccountDetail!!.webSite
						tvWebSite.visibility = View.VISIBLE
					}
					val imageUri: String = userData.userAccountDetail!!.profilePicture!!
					UniversalImageLoader.setImage(imageUri, profileImageCricle, progressBarProfActivt, "")
					println(userData.toString())
				}

			}

		})
	}

	private fun setupSettingsProfileUser() {
		if (checkSnapshot) {
			activProfileEditContainerLayout.visibility = View.GONE
			val editFragment = supportFragmentManager.beginTransaction()
			editFragment.replace(R.id.activityEditProfileContainer, ProfileEditFragment())
			editFragment.addToBackStack("bacPressOnProfileActivity")
			editFragment.commit()
		}
	}

	override fun onBackPressed() {
		activProfileEditContainerLayout.visibility = View.VISIBLE
		super.onBackPressed()
	}

	private fun setNavigationBottomMenu() {
		BottonNavigationHelper.setupButtonNavigationLocation(this, bnve)
		val menu = bnve.menu
		val menuItem = menu.getItem(ACTIVITY_NO)
		menuItem.isChecked = true

	}

	override fun onClick(v: View?) {
		when (v?.id) {
			R.id.tvEditProfileActivtyButton -> {
				setupSettingsProfileUser()
			}
			R.id.accountSettingsImageView -> {
				intentFromProfileToProfileSettings()
			}
		}
	}

	private fun intentFromProfileToProfileSettings() {


		val intent = Intent(
			this,
			ProfileSettingsActivity::class.java
		).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION and Intent.FLAG_ACTIVITY_CLEAR_TASK)
		//.intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
		startActivity(intent)
		finish()
	}

	private fun setUpAuthListener(user: FirebaseUser?) {

		if (user == null) {
			val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
				.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
			startActivity(intent)
			finish()
		}
	}

	override fun onStart() {
		super.onStart()
		Log.e(TAG, "onStart: ProfileActivity-deyiz")
		val currentUser = mAuth.currentUser
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
		private val TAG = "HomeActivity"
	}
}