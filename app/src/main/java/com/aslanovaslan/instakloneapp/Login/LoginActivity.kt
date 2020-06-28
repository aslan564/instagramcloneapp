package com.aslanovaslan.instakloneapp.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.aslanovaslan.instakloneapp.Home.HomeActivity
import com.aslanovaslan.instakloneapp.R
import com.aslanovaslan.instakloneapp.UserModel.UserAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), View.OnClickListener,TextWatcher {

	private lateinit var mAuth: FirebaseAuth
	private lateinit var mAuthListener: FirebaseAuth.AuthStateListener
	private lateinit var mReference: DatabaseReference
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_login)

		mAuth = FirebaseAuth.getInstance()
		mReference = FirebaseDatabase.getInstance().reference



		initializeVariable()

	}


    private fun sendFromLoginToRegister(){
	    val intent: Intent = Intent(
		    this@LoginActivity,
		    RegisterActivity::class.java
	    ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
	    startActivity(intent)
    }

    private fun registerLoginChecking(
        usernameEmailOrPhoneNumber: String,
        password: String
    ) {
        var isUserFind=false
        mReference.child("users").orderByChild("email")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    println(error)
                    Log.e(TAG, "onCancelled: $error")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value != null) {
                        for (infoUser in snapshot.children) {
                            val userdata = infoUser.getValue(UserAccount::class.java)
                            if (userdata!!.email.equals(usernameEmailOrPhoneNumber)) {
                                signingEmailWithUndreamedPhoneNumber(userdata, password, false)
                                isUserFind=true
                                break
                            } else if (userdata.phone_number.equals(usernameEmailOrPhoneNumber)) {
                                signingEmailWithUndreamedPhoneNumber(userdata, password, true)
                                isUserFind=true
                                break
                            } else if (userdata.user_name.equals(usernameEmailOrPhoneNumber)) {
                                signingEmailWithUndreamedPhoneNumber(userdata, password, false)
                                isUserFind=true
                                break
                            }
                        }
                        if (!isUserFind) {
                            Toast.makeText(this@LoginActivity, "User not found: ", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            })
    }

    private fun signingEmailWithUndreamedPhoneNumber(
		data: UserAccount,
		password: String,
		isEmailOrPhone: Boolean
	) {
		var userdata = ""
	    userdata = if (isEmailOrPhone) {
		    data.emailed_with_number.toString()
	    } else {
		    data.email.toString()
	    }

        mAuth.signInWithEmailAndPassword(userdata,password)
            .addOnSuccessListener {
                val intent = Intent(
                    this@LoginActivity,
                    HomeActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
                finish()
                Toast.makeText(this@LoginActivity, " Your entry was successful ", Toast.LENGTH_SHORT).show()

            }.addOnFailureListener {
                Toast.makeText(this@LoginActivity, "Your entry was unsuccessful ", Toast.LENGTH_SHORT).show()
            }
	}

	private fun initializeVariable() {
		btnLoginWithPhoneOrEmail.setOnClickListener(this)
		tvSendRegisterActivity.setOnClickListener(this)
		etLoginUserNameOrEmali.addTextChangedListener(this)
		etloginUsernameOrPhonePassword.addTextChangedListener(this)

	}

	private fun setUpAuthListener(user: FirebaseUser?) {

		if (user != null) {
			val intent = Intent(
				this@LoginActivity,
				HomeActivity::class.java
			).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
			startActivity(intent)
			finish()
		}
	}

	override fun onStart() {
		super.onStart()
		val currentUser = mAuth.currentUser
		setUpAuthListener(currentUser)
	}

	override fun onStop() {
		super.onStop()
		val currentUser = mAuth.currentUser
		if (currentUser != null) {
			val user = FirebaseAuth.getInstance().currentUser!!
			setUpAuthListener(user)
		}
	}



	override fun onClick(v: View) {
		when(v.id){
			R.id.btnLoginWithPhoneOrEmail->{
				connectLoginMethods()
			}
			R.id.tvSendRegisterActivity->{
				sendFromLoginToRegister()
			}
		}

	}

	private fun connectLoginMethods() {
		val usernameEmailOrPhoneNumber = etLoginUserNameOrEmali.text.toString()
		val password = etloginUsernameOrPhonePassword.text.toString()
		registerLoginChecking(usernameEmailOrPhoneNumber, password)
	}

	override fun afterTextChanged(s: Editable?) {
	}

	override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
	}

	override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
		if (etLoginUserNameOrEmali.text.length >= 5 && etloginUsernameOrPhonePassword.text.length >= 6) {
			btnLoginWithPhoneOrEmail.isEnabled = true
			btnLoginWithPhoneOrEmail.isFocusable = true
			btnLoginWithPhoneOrEmail.isClickable = true
			this@LoginActivity.let { ContextCompat.getColor(it, R.color.whiteColor) }.let {
				btnLoginWithPhoneOrEmail.setTextColor(
					it
				)
			}
			btnLoginWithPhoneOrEmail.setBackgroundResource(R.drawable.edit_profile_button_background_activ)
		} else {
			btnLoginWithPhoneOrEmail.isEnabled = false
			btnLoginWithPhoneOrEmail.isFocusable = false
			btnLoginWithPhoneOrEmail.isClickable = false
			this@LoginActivity.let { ContextCompat.getColor(it, R.color.instaSearchClear) }
				.let {
					btnLoginWithPhoneOrEmail.setTextColor(
						it
					)
				}
			btnLoginWithPhoneOrEmail.setBackgroundResource(R.drawable.edit_profile_button_background)
		}
	}

	companion object {
		private const val TAG = "LoginActivity"
	}
}