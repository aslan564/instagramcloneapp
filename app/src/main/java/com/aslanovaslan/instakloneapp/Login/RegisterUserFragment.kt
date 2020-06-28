package com.aslanovaslan.instakloneapp.Login

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
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.aslanovaslan.instakloneapp.Home.HomeActivity
import com.aslanovaslan.instakloneapp.R
import com.aslanovaslan.instakloneapp.UserModel.UserAccount
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

class RegisterUserFragment : Fragment() {

	private lateinit var saveInfoDatabase: Button
	private lateinit var fragmentUserProgres: ProgressBar
	private var pass = ""
	private var userName = ""
	private var fullname = ""
	private var userPhonyNumber = ""

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

		mAuth = FirebaseAuth.getInstance()
		mReference = FirebaseDatabase.getInstance().reference

		view.tvNameAndSurname.addTextChangedListener(watcher)
		view.tvUserName.addTextChangedListener(watcher)
		view.tvUserPassword.addTextChangedListener(watcher)
		saveInfoDatabase = view.btnLoginRegFrag as Button
		fragmentUserProgres = view.frRegisterUserProgres

		view.tvLoginSendFragPhone3.setOnClickListener {
            val intent = Intent(
                activity,
                LoginActivity::class.java
            ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
		}
		saveInfoDatabase.setOnClickListener {
			var isUserNameUses = false

			mReference.child("users")
				.addListenerForSingleValueEvent(object : ValueEventListener {
					override fun onCancelled(error: DatabaseError) {
						Log.e(TAG, "onCancelled: " + error.message)
					}

					override fun onDataChange(snapshot: DataSnapshot) {
						if (snapshot.value != null) {
							for (user in snapshot.children) {
								val userSingleName = user.getValue(UserAccount::class.java)
								if (userSingleName!!.user_name.equals(view.tvUserName.text.toString())) {
									isUserNameUses = true
									Toast.makeText(
										activity,
										"Username is in use ",
										Toast.LENGTH_SHORT
									).show()
									break
								}

							}
							if (!isUserNameUses) {
								fragmentUserProgres.visibility = View.VISIBLE
								pass = view.tvUserPassword.text.toString()
								userName = view.tvUserName.text.toString()
								fullname = view.tvNameAndSurname.text.toString()

								if (userRegisterWithEmail) {
									try {
										mAuth.createUserWithEmailAndPassword(
											userEventBusEmail,
											pass
										)
											.addOnCompleteListener(OnCompleteListener {
												if (it.isSuccessful) {
													val userUid = mAuth.currentUser!!.uid
													val createNewUserAccount =
														UserAccount(
															userEventBusEmail,
															pass,
															userName,
															fullname,
															"noPhone",
															"noPhoneEmail",
															userUid
														)
													mReference.child("users")
														.child(userUid)
														.setValue(createNewUserAccount)
														.addOnSuccessListener {
															val intent = Intent(
																activity,
																HomeActivity::class.java
															).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
															startActivity(intent)
															activity!!.finish()
															fragmentUserProgres.visibility =
																View.INVISIBLE
															Toast.makeText(
																activity,
																"Created user ",
																Toast.LENGTH_SHORT
															).show()
														}.addOnFailureListener { e ->
															fragmentUserProgres.visibility =
																View.INVISIBLE
															mAuth.currentUser!!.delete()
																.addOnSuccessListener {
																	Toast.makeText(
																		activity,
																		"Please try again",
																		Toast.LENGTH_SHORT
																	).show()
																}.addOnFailureListener { f ->
																f.printStackTrace()
															}
															e.printStackTrace()
															Log.e(
																TAG,
																"onCreateView: ${e.printStackTrace()}"
															)
														}

													Toast.makeText(
														activity,
														" ${it.result!!.user!!.email} : Created",
														Toast.LENGTH_SHORT
													).show()
												} else {
													Toast.makeText(
														activity, it.exception!!.localizedMessage,
														Toast.LENGTH_SHORT
													).show()
												}
											}).addOnFailureListener(OnFailureListener {
												it.printStackTrace()
												fragmentUserProgres.visibility = View.INVISIBLE
												Log.e(TAG, "onCreateView: " + it.stackTrace)
											})
									} catch (e: FirebaseAuthException) {
										e.printStackTrace()
									}


								} else {
									val createEmailWithNumber = "$userPhonyNumber@customer.com"
									mAuth.createUserWithEmailAndPassword(
										createEmailWithNumber,
										pass
									)
										.addOnSuccessListener {
											val userUid = mAuth.currentUser!!.uid
											val createNewUserAccount =
												UserAccount(
													"noEmail",
													pass,
													userName,
													fullname,
													userPhonyNumber,
													createEmailWithNumber,
													userUid
												)
											mReference.child("users")
												.child(userUid)
												.setValue(createNewUserAccount)
												.addOnSuccessListener {
													val intent = Intent(
														activity,
														HomeActivity::class.java
													).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
													startActivity(intent)
													activity!!.finish()
													fragmentUserProgres.visibility = View.INVISIBLE
													Toast.makeText(
														activity,
														"User created",
														Toast.LENGTH_SHORT
													).show()
												}.addOnFailureListener { it1 ->
													fragmentUserProgres.visibility = View.INVISIBLE
													mAuth.currentUser!!.delete()
														.addOnSuccessListener {
															Toast.makeText(
																activity,
																"Please try Again",
																Toast.LENGTH_SHORT
															).show()
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

		}
		return view
	}

	private var watcher = object : TextWatcher {
		override fun afterTextChanged(s: Editable?) {
		}

		override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
		}

		override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
			if (!s.isNullOrEmpty() && s.length > 5) {
				if (tvNameAndSurname.length() > 5 && tvNameAndSurname.text.contains(" ") && tvUserName.length() > 5 && tvUserPassword.length() > 5) {
					saveInfoDatabase.isEnabled = true
					saveInfoDatabase.isFocusable = true
					saveInfoDatabase.isClickable = true

					activity?.let { ContextCompat.getColor(it, R.color.whiteColor) }?.let {
						saveInfoDatabase.setTextColor(
							it
						)
					}
					saveInfoDatabase.setBackgroundResource(R.drawable.edit_profile_button_background_activ)
				} else {
					saveInfoDatabase.isEnabled = false
					saveInfoDatabase.isFocusable = false
					saveInfoDatabase.isClickable = false
					activity?.let { ContextCompat.getColor(it, R.color.instaSearchClear) }?.let {
						saveInfoDatabase.setTextColor(
							it
						)
					}
					saveInfoDatabase.setBackgroundResource(R.drawable.edit_profile_button_background)
				}
			} else {
				saveInfoDatabase.isEnabled = false
				saveInfoDatabase.isFocusable = false
				saveInfoDatabase.isClickable = false
				activity?.let { ContextCompat.getColor(it, R.color.instaSearchClear) }?.let {
					saveInfoDatabase.setTextColor(
						it
					)
				}
				saveInfoDatabase.setBackgroundResource(R.drawable.edit_profile_button_background)
			}
		}

	}

	@Subscribe(sticky = true)
	internal fun setupRegistryDataInCome(sendRegisterData: EventBusData.SendRegisterData) {
		if (sendRegisterData.sendRegistryWithEmail) {
			userRegisterWithEmail = true
			userEventBusEmail = sendRegisterData.sendEmail.toString()
			val isRegistryWithEmail = sendRegisterData.sendRegistryWithEmail
			Toast.makeText(
				context,
				"email: $userEventBusEmail :$isRegistryWithEmail",
				Toast.LENGTH_SHORT
			).show()
		} else {
			userRegisterWithEmail = false
			userPhonyNumber = sendRegisterData.sendNumber.toString()
			val userUserRegistryCode = sendRegisterData.sendRegisteryCode
			Toast.makeText(
				context,
				"phone: $userPhonyNumber :$userUserRegistryCode",
				Toast.LENGTH_SHORT
			).show()
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
}