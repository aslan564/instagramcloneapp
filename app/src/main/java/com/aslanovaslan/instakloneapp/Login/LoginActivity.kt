package com.aslanovaslan.instakloneapp.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.aslanovaslan.instakloneapp.Home.HomeActivity
import com.aslanovaslan.instakloneapp.R
import com.aslanovaslan.instakloneapp.UserModel.UserAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

	private val TAG = "LoginActivity"
	private lateinit var mAuth: FirebaseAuth
	private lateinit var mAuthListener: FirebaseAuth.AuthStateListener
	private lateinit var mReference: DatabaseReference
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_login)
        setUpAuthListener()
		initializeVariable()

		setUpClicklistenerView()
	}


    private fun setUpClicklistenerView() {
		btnLoginWithPhoneOrEmail.setOnClickListener {
			val usernameEmailOrPhoneNumber = etLoginUserNameOrEmali.text.toString()
			val password = etloginUsernameOrPhonePassword.text.toString()
            registerLoginChecking(usernameEmailOrPhoneNumber, password)

		}
		tvSendRegisterActivity.setOnClickListener {
			val intent: Intent = Intent(
				this@LoginActivity,
				RegisterActivity::class.java
			).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
			startActivity(intent)
		}

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
		var useremail = ""
		if (isEmailOrPhone) {
			useremail = data.emailed_with_number.toString()
		} else {
			useremail = data.email.toString()
		}

        mAuth.signInWithEmailAndPassword(useremail,password)
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
		etLoginUserNameOrEmali.addTextChangedListener(watcher)
		etloginUsernameOrPhonePassword.addTextChangedListener(watcher)
		mAuth = FirebaseAuth.getInstance()
		mReference = FirebaseDatabase.getInstance().reference
	}

	private val watcher: TextWatcher = object : TextWatcher {
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

	}
    private fun setUpAuthListener() {
        mAuthListener= FirebaseAuth.AuthStateListener {
            val user=FirebaseAuth.getInstance().currentUser
            if (user!= null) {
                val intent = Intent(
                    this@LoginActivity,
                    HomeActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mAuth.addAuthStateListener { mAuthListener }
    }

    override fun onStop() {
        super.onStop()
        if (mAuth.currentUser != null) {
            mAuth.removeAuthStateListener { mAuthListener }
        }
    }
}