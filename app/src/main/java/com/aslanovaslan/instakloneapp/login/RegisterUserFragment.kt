package com.aslanovaslan.instakloneapp.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.aslanovaslan.instakloneapp.home.HomeActivity
import com.aslanovaslan.instakloneapp.R
import com.aslanovaslan.instakloneapp.userModel.UserAccount
import com.aslanovaslan.instakloneapp.userModel.UserAccountDetail
import com.aslanovaslan.instakloneapp.utils.EventBusData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_register_user.*
import kotlinx.android.synthetic.main.fragment_register_user.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class RegisterUserFragment : Fragment(), View.OnClickListener, TextWatcher {

	private lateinit var fragmentUserProgres: ProgressBar
	private var pass = ""
	private var userName = ""
	private var fullname = ""
	private var userPhonyNumber = ""


/*
	private var readyUserNameEt = ""
	private var readyPasswordEt = ""
	private var readyFullNameEt = ""
*/


	private val TAG = "RegisterUserFragment"
	private var userRegisterWithEmail: Boolean = true
	private var userEventBusEmail = ""
	private lateinit var mAuth: FirebaseAuth
	private lateinit var mReference: DatabaseReference

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		// Inflate the layout for this fragment

		val view = inflater.inflate(R.layout.fragment_register_user, container, false)

		initializeVariableInstance(view)


		return view
	}

	private fun sendLoginIntent() {
		val intent = Intent(
			activity,
			LoginActivity::class.java
		).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
		startActivity(intent)
	}

	private fun initializeVariableInstance(view: View) {
		mAuth = FirebaseAuth.getInstance()
		mReference = FirebaseDatabase.getInstance().reference


		view.btnLoginRegFrag.setOnClickListener(this)
		view.tvLoginSendFragPhone3.setOnClickListener(this)


		fragmentUserProgres = view.frRegisterUserProgres
		view.tvNameAndSurnameFragment.addTextChangedListener(this)
		view.tvUserNameFragment.addTextChangedListener(this)
		view.tvUserPasswordFragment.addTextChangedListener(this)

		/*readyUserNameEt = view.tvUserNameFragment.text.toString()
		readyPasswordEt = view.tvUserPasswordFragment.text.toString()
		readyFullNameEt = view.tvNameAndSurnameFragment.text.toString()*/
	}

	@Subscribe(sticky = true)
	internal fun setupRegistryDataInCome(sendRegisterData: EventBusData.SendRegisterData) {
		if (sendRegisterData.sendRegistryWithEmail) {
			userRegisterWithEmail = true
			userEventBusEmail = sendRegisterData.sendEmail.toString()
			val isRegistryWithEmail = sendRegisterData.sendRegistryWithEmail
			Toast.makeText(context, "email: $userEventBusEmail :$isRegistryWithEmail", Toast.LENGTH_SHORT).show()
		} else {
			userRegisterWithEmail = false
			userPhonyNumber = sendRegisterData.sendNumber.toString()
			val userUserRegistryCode = sendRegisterData.sendRegisteryCode
			Toast.makeText(context, "phone: $userPhonyNumber :$userUserRegistryCode", Toast.LENGTH_SHORT).show()
		}
	}

	override fun onAttach(context: Context) {
		super.onAttach(context)
		EventBus.getDefault().register(this)
	}

	override fun onDetach() {
		super.onDetach()
		EventBus.getDefault().unregister(this)
	}

	override fun onClick(v: View?) {
		when (v?.id) {
			R.id.btnLoginRegFrag -> {
				createUserDatabaseInfo()
			}
			R.id.tvLoginSendFragPhone3 -> {
				sendLoginIntent()
			}
		}
	}

	private fun createUserDatabaseInfo() {
		var isUserNameUses = false
		fragmentUserProgres.visibility = View.VISIBLE
		//Toast.makeText(activity, "click", Toast.LENGTH_SHORT).show()
		try {
			mReference.child("users")
				.addListenerForSingleValueEvent(object : ValueEventListener {
					override fun onCancelled(error: DatabaseError) {
						Log.e(TAG, "onCancelled: " + error.message)
					}

					override fun onDataChange(snapshot: DataSnapshot) {
						if (snapshot.value != null) {
							for (user in snapshot.children) {
								val userSingleName = user.getValue(UserAccount::class.java)
								if (userSingleName!!.userName.equals(tvUserNameFragment.text.toString())) {
									isUserNameUses = true
									fragmentUserProgres.visibility = View.INVISIBLE
									Toast.makeText(activity, "Username is in use ", Toast.LENGTH_SHORT).show()
									break
								}

							}
							if (!isUserNameUses) {
								pass = tvUserPasswordFragment.text.toString()
								userName = tvUserNameFragment.text.toString()
								fullname = tvNameAndSurnameFragment.text.toString()

								if (userRegisterWithEmail) {
									try {
										mAuth.createUserWithEmailAndPassword(userEventBusEmail, pass)
											.addOnSuccessListener {
												val userUid = mAuth.currentUser!!.uid
												val userDetailInfo: UserAccountDetail = UserAccountDetail("0", "0", "0", "", "", "dfasdasds")
												//var datas= listOf<String,UserAccount>
												var createNewUserAccount =
													UserAccount(userEventBusEmail, pass, userName, fullname, "noPhone", "noPhoneEmail", userUid, userDetailInfo)
												//	try {
												mReference.child("users")
													.child(userUid)
													.setValue(createNewUserAccount)
													.addOnSuccessListener {
														val intent = Intent(activity, HomeActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
														startActivity(intent)
														//activity!!.finish()
														fragmentUserProgres.visibility = View.INVISIBLE
														Toast.makeText(activity, "Created user ", Toast.LENGTH_SHORT).show()
													}
													.addOnFailureListener { e ->
														fragmentUserProgres.visibility = View.INVISIBLE
														mAuth.currentUser!!.delete()
															.addOnSuccessListener {
																Toast.makeText(activity, "Please try again", Toast.LENGTH_SHORT).show()
															}.addOnFailureListener { f ->
																f.printStackTrace()
																f.stackTrace
															}
														e.printStackTrace()
														Log.e(TAG, "onCreateView: ${e.printStackTrace()}")
													}

												Toast.makeText(activity, " ${it.user!!.email} : Created", Toast.LENGTH_SHORT).show()
											}
											.addOnFailureListener(OnFailureListener {
												it.printStackTrace()
												fragmentUserProgres.visibility =
													View.INVISIBLE
												Log.e(TAG, "onCreateView: " + it.stackTrace)
											})
									} catch (e: DatabaseException) {
										e.printStackTrace()
									}


								} else {
									val createEmailWithNumber = "$userPhonyNumber@customer.com"
									mAuth.createUserWithEmailAndPassword(createEmailWithNumber, pass)
										.addOnSuccessListener {
											val userUid = mAuth.currentUser!!.uid
											val userAccountDetail =
												UserAccountDetail("0", "0", "0", "", "", "")
											val createNewUserAccount =
												UserAccount("noEmail", pass, userName, fullname, userPhonyNumber, createEmailWithNumber, userUid, userAccountDetail)
											mReference.child("users")
												.child(userUid)
												.setValue(createNewUserAccount)
												.addOnSuccessListener {
													val intent = Intent(activity, HomeActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
													startActivity(intent)
													activity!!.finish()
													fragmentUserProgres.visibility = View.INVISIBLE
													Toast.makeText(activity, "User created", Toast.LENGTH_SHORT).show()
												}.addOnFailureListener { it1 ->
													fragmentUserProgres.visibility = View.INVISIBLE
													mAuth.currentUser!!.delete()
														.addOnSuccessListener {
															Toast.makeText(activity, "Please try Again", Toast.LENGTH_SHORT).show()
														}.addOnFailureListener {
															it.printStackTrace()
														}
													it1.printStackTrace()
												}
										}.addOnFailureListener {
											fragmentUserProgres.visibility = View.INVISIBLE
											it.printStackTrace()
										}
								}
							}
						}

					}

				})
		} catch (e: DatabaseException) {
			e.printStackTrace()
		}


	}

	override fun afterTextChanged(s: Editable?) {
	}

	override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
	}

	override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
		if (!s.isNullOrEmpty() && s.length > 5) {
			if (tvNameAndSurnameFragment.length() > 5 && tvNameAndSurnameFragment.text.contains(" ") && tvUserNameFragment.length() > 5 && tvUserPasswordFragment.length() > 5) {
				btnLoginRegFrag.isEnabled = true
				btnLoginRegFrag.isFocusable = true
				btnLoginRegFrag.isClickable = true

				activity?.let { ContextCompat.getColor(it, R.color.whiteColor) }?.let {
					btnLoginRegFrag.setTextColor(
						it
					)
				}
				btnLoginRegFrag.setBackgroundResource(R.drawable.edit_profile_button_background_activ)
			} else {
				btnLoginRegFrag.isEnabled = false
				btnLoginRegFrag.isFocusable = false
				btnLoginRegFrag.isClickable = false
				activity?.let { ContextCompat.getColor(it, R.color.instaSearchClear) }?.let {
					btnLoginRegFrag.setTextColor(
						it
					)
				}
				btnLoginRegFrag.setBackgroundResource(R.drawable.edit_profile_button_background)
			}
		} else {
			btnLoginRegFrag.isEnabled = false
			btnLoginRegFrag.isFocusable = false
			btnLoginRegFrag.isClickable = false
			activity?.let { ContextCompat.getColor(it, R.color.instaSearchClear) }?.let {
				btnLoginRegFrag.setTextColor(
					it
				)
			}
			btnLoginRegFrag.setBackgroundResource(R.drawable.edit_profile_button_background)
		}
	}
}