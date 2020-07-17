package com.aslanovaslan.instakloneapp.profile

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.aslanovaslan.instakloneapp.R
import com.aslanovaslan.instakloneapp.userModel.UserAccount
import com.aslanovaslan.instakloneapp.utils.EventBusData
import com.aslanovaslan.instakloneapp.utils.UniversalImageLoader
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_profile_edit.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.EventBusException
import org.greenrobot.eventbus.Subscribe
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class ProfileEditFragment : Fragment(), View.OnClickListener {
	// TODO: Rename and change types of parameters
	private lateinit var imgUserProfilePhotos: CircleImageView
	private lateinit var progressBarEditFragment: ProgressBar
	private lateinit var nextUserData: UserAccount

	private lateinit var mAuth: FirebaseAuth
	private lateinit var mReference: DatabaseReference
	private lateinit var mStorageRef: StorageReference
	private var profilePhotoUri: String? = null


	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		// Inflate the layout for this fragment

		val view = inflater.inflate(R.layout.fragment_profile_edit, container, false)
		imgUserProfilePhotos = view.findViewById(R.id.imgUserProfilePhotos)
		progressBarEditFragment = view.findViewById(R.id.progressBarEditFragment)
		mReference = FirebaseDatabase.getInstance().reference
		mStorageRef = FirebaseStorage.getInstance().reference
		setupProfileUser(view)
		setupChangeProfile(view)
		view.imgCloseProfile.setOnClickListener {
			activity?.onBackPressed()
		}

		view.tvChangeProfilePhoto.apply {
			this.setOnClickListener {

				if (Build.VERSION.SDK_INT >= 23) {
					if (ContextCompat.checkSelfPermission(this@ProfileEditFragment.requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
						ActivityCompat.requestPermissions(this@ProfileEditFragment.requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_PERMISSION)
					} else {
						val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
						intent.type = "image/*"
						this@ProfileEditFragment.startActivityForResult(intent, REQUEST_CODE_LOAD_IMAGE)
						//Toast.makeText(this@ProfileEditFragment.requireContext(), "click image", Toast.LENGTH_SHORT).show()
					}
					//dialog.dismiss();
					//return;
				} else {
					val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
					intent.type = "image/*"
					this@ProfileEditFragment.startActivityForResult(intent, REQUEST_CODE_LOAD_IMAGE)
					//Toast.makeText(this@ProfileEditFragment.requireContext(), "click image", Toast.LENGTH_SHORT).show()
				}

			}
		}
		return view
	}

	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
		if (requestCode == REQUEST_PERMISSION) {
			if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				// Permission granted.
				val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
				intent.type = "image/*"
				this@ProfileEditFragment.startActivityForResult(intent, REQUEST_CODE_LOAD_IMAGE)
			}
			super.onRequestPermissionsResult(requestCode, permissions, grantResults)
		}
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		if (requestCode == REQUEST_CODE_LOAD_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
			profilePhotoUri = data.data!!.toString()
			UniversalImageLoader.setImage(profilePhotoUri.toString(), imgUserProfilePhotos, progressBarEditFragment, "")
			//imgUserProfilePhotos.setImageURI(profilePhotoUri)
		}


		super.onActivityResult(requestCode, resultCode, data)
		//	if (requestCode)
	}

	private fun setupProfileUser(view: View) {
		if (!nextUserData.userUid.isNullOrEmpty()) {
			try {
				view.etNameFm.setText(nextUserData.nameSurname.toString())
				view.etUsername.setText(nextUserData.userName.toString())
				view.edUserBiografy.setText(nextUserData.userAccountDetail!!.biography.toString())
				view.edUsernternetSite.setText(nextUserData.userAccountDetail!!.webSite.toString())
				view.edUserPhoneNumber.setText(nextUserData.phoneNumber.toString())
				val profileImageUri = nextUserData.userAccountDetail!!.profilePicture
				UniversalImageLoader.setImage(profileImageUri!!, imgUserProfilePhotos, progressBarEditFragment, "")
			} catch (e: EventBusException) {
				e.printStackTrace()
			}
		}
	}

	private fun setupChangeProfile(view: View) {

		view.img_save_editing_profile.apply {
			this.setOnClickListener {
				if (!nextUserData.userUid.isNullOrEmpty()) {
					try {
						//mReference.child("users").orderByChild("email")
						if (profilePhotoUri != null) {
							val uploaddialog=ProgresFragment()
							uploaddialog.isCancelable=false
							uploaddialog.show(activity!!.supportFragmentManager,"loadingFragment")
							val uuid = UUID.randomUUID()
							val imageName = "${nextUserData.userUid.toString()}/" + uuid + ".jpg"
							mStorageRef.child("images").child(imageName).putFile(Uri.parse(profilePhotoUri)).addOnSuccessListener { image ->

								val uploadImageRefStorage = FirebaseStorage.getInstance().reference.child("images").child(imageName)
								uploadImageRefStorage.downloadUrl.addOnSuccessListener { uri ->
									mReference.child("users").child(nextUserData.userUid.toString()).child("userAccountDetail").child("profilePicture").setValue(uri.toString())
									changeUsername(view, true)
									uploaddialog.dismiss()
								}.addOnFailureListener { ex ->
									Toast.makeText(this@ProfileEditFragment.requireContext(), ex.localizedMessage, Toast.LENGTH_SHORT).show()
									uploaddialog.dismiss()
								}

							}.addOnFailureListener(OnFailureListener { e ->
								Toast.makeText(this@ProfileEditFragment.requireContext(), e.localizedMessage, Toast.LENGTH_SHORT).show()
								changeUsername(view, false)
								uploaddialog.dismiss()
							})
						} else {
							changeUsername(view, null)
						}
					} catch (e: EventBusException) {
						e.printStackTrace()
					}
				}
			}
		}
	}

	private fun changeUsername(view: View, userImageChange: Boolean?) {
		if (nextUserData.userName.toString() != view.etUsername.text.toString()) {
			mReference.child("users").addListenerForSingleValueEvent(object : ValueEventListener {
				override fun onCancelled(error: DatabaseError) {
					println(error)
				}

				override fun onDataChange(snapshot: DataSnapshot) {
					var userNameIsInUse = false
					if (snapshot.value != null) {
						for (ds in snapshot.children) {
							val writeUserName = ds.getValue(UserAccount::class.java)!!.userName.toString()
							if (writeUserName == view.etUsername.text.toString()) {
								Toast.makeText(this@ProfileEditFragment.requireContext(), "Usernameis in use", Toast.LENGTH_SHORT).show()
								userNameIsInUse = true
								changeProfileData(view, userImageChange, false)
								break
							}

						}
						if (!userNameIsInUse) {
							if (nextUserData.userAccountDetail!!.biography.toString() != view.etUsername.text.toString()) {
								mReference.child("users").child(nextUserData.userUid.toString()).child("userName").setValue(view.etUsername.text.toString())
								Toast.makeText(this@ProfileEditFragment.requireContext(), "Profile updated", Toast.LENGTH_SHORT).show()
								changeProfileData(view, userImageChange, true)
							}
						}
					}
				}

			})
		} else {
			changeProfileData(view, userImageChange, null)
		}

	}

	private fun changeProfileData(view: View, userImageChange: Boolean?, userNameChange: Boolean?) {
		var changeProfileEdit: Boolean = false
		if (nextUserData.nameSurname.toString() != view.etNameFm.text.toString()) {
			mReference.child("users").child(nextUserData.userUid.toString()).child("nameSurname").setValue(view.etNameFm.text.toString())
			changeProfileEdit = true
		}
		if (nextUserData.userAccountDetail!!.biography.toString() != view.edUserBiografy.text.toString()) {
			mReference.child("users").child(nextUserData.userUid.toString()).child("userAccountDetail").child("biography").setValue(view.edUserBiografy.text.toString())
			changeProfileEdit = true
		}
		if (nextUserData.userAccountDetail!!.webSite.toString() != view.edUsernternetSite.text.toString()) {
			mReference.child("users").child(nextUserData.userUid.toString()).child("userAccountDetail").child("webSite").setValue(view.edUsernternetSite.text.toString())
			changeProfileEdit = true
		}
		if (nextUserData.phoneNumber.toString() != view.edUserPhoneNumber.text.toString()) {
			mReference.child("users").child(nextUserData.userUid.toString()).child("phoneNumber").setValue(view.edUserPhoneNumber.text.toString())
			changeProfileEdit = true
		}
		if (userImageChange == null && userNameChange == null && !changeProfileEdit) {
			Toast.makeText(this@ProfileEditFragment.requireContext(), "Profile not updated", Toast.LENGTH_SHORT).show()
			//activity!!.onBackPressed()
		} else if (userImageChange == null && userNameChange == null && changeProfileEdit) {
			Toast.makeText(this@ProfileEditFragment.requireContext(), "Profile updated", Toast.LENGTH_SHORT).show()
			activity!!.onBackPressed()
		} else if (userImageChange == null && (userNameChange == true || changeProfileEdit)) {
			Toast.makeText(this@ProfileEditFragment.requireContext(), "Profile  updated", Toast.LENGTH_SHORT).show()
		} else if (userImageChange == true && userNameChange == true && changeProfileEdit) {
			Toast.makeText(this@ProfileEditFragment.requireContext(), "Profile  updated", Toast.LENGTH_SHORT).show()
			activity!!.onBackPressed()
		} else {
			Toast.makeText(this@ProfileEditFragment.requireContext(), "Profile  updated", Toast.LENGTH_SHORT).show()
			activity!!.onBackPressed()
		}


	}

	@Subscribe(sticky = true)
	internal fun getUserProfileInfo(getUserInfo: EventBusData.SendUserProfile) {
		nextUserData = getUserInfo.userAccount
	}

	override fun onAttach(context: Context) {
		super.onAttach(context)
		EventBus.getDefault().register(this)
	}

	override fun onDestroy() {
		super.onDestroy()
		EventBus.getDefault().unregister(this)
	}

	override fun onClick(v: View) {
		when (v.id) {
			R.id.img_save_editing_profile -> {
			}
		}
	}

	companion object {
		private const val TAG = "ProfileEditFragment"
		private const val REQUEST_CODE_LOAD_IMAGE = 1
		private const val REQUEST_PERMISSION = 2
	}
}