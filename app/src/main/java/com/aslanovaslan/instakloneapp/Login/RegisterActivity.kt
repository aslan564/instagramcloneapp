package com.aslanovaslan.instakloneapp.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.aslanovaslan.instakloneapp.Home.HomeActivity
import com.aslanovaslan.instakloneapp.R
import com.aslanovaslan.instakloneapp.UserModel.UserAccount
import com.aslanovaslan.instakloneapp.utils.EventBusData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_register.*
import org.greenrobot.eventbus.EventBus
import java.lang.Exception

class RegisterActivity : AppCompatActivity(), FragmentManager.OnBackStackChangedListener {

    private lateinit var manager: FragmentManager
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mAuthListener: FirebaseAuth.AuthStateListener
    private lateinit var mReference: DatabaseReference
    private val TAG = "RegisterActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setUpAuthListener()

        initializeVariable()



        manager = supportFragmentManager
        manager.addOnBackStackChangedListener(this)

        setRegisterActWorks()
    }

    private fun initializeVariable() {
        mAuth = FirebaseAuth.getInstance()
        mReference = FirebaseDatabase.getInstance().reference

        tvLoginSendFragPhone1.setOnClickListener{
            val intent = Intent(
                this@RegisterActivity,
                LoginActivity::class.java
            ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }
    }

    private fun setRegisterActWorks() {
        tvPhoneSelectorRegister.setOnClickListener {

            phoneWiewBlack.visibility = View.VISIBLE
            emailWiewBlack.visibility = View.INVISIBLE
            etRegisterInputType.setText("")
            etRegisterInputType.hint = "Phone +994500000000"
            etRegisterInputType.inputType = InputType.TYPE_CLASS_PHONE
            btnRegisterNext.isEnabled = false
            btnRegisterNext.setBackgroundResource(R.drawable.edit_profile_button_background)
            btnRegisterNext.setTextColor(
                ContextCompat.getColor(
                    this@RegisterActivity,
                    R.color.instaSearchClear
                )
            )
        }
        tvEmailSelectorRegister.setOnClickListener {
            phoneWiewBlack.visibility = View.INVISIBLE
            emailWiewBlack.visibility = View.VISIBLE
            etRegisterInputType.setText("")
            etRegisterInputType.hint = "Email example@some.com"
            etRegisterInputType.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            btnRegisterNext.isEnabled = false
            btnRegisterNext.setBackgroundResource(R.drawable.edit_profile_button_background)
            btnRegisterNext.setTextColor(
                ContextCompat.getColor(
                    this@RegisterActivity,
                    R.color.instaSearchClear
                )
            )
        }
        etRegisterInputType.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    if (s!!.length >= 7) {
                        btnRegisterNext.isEnabled = true
                        btnRegisterNext.setBackgroundResource(R.drawable.edit_profile_button_background_activ)
                        btnRegisterNext.setTextColor(
                            ContextCompat.getColor(
                                this@RegisterActivity,
                                R.color.whiteColor
                            )
                        )
                    } else {
                        btnRegisterNext.isEnabled = false
                        btnRegisterNext.setBackgroundResource(R.drawable.edit_profile_button_background)
                        btnRegisterNext.setTextColor(
                            ContextCompat.getColor(
                                this@RegisterActivity,
                                R.color.instaSearchClear
                            )
                        )

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }


            }

        })

        btnRegisterNext.setOnClickListener {
            try {
                when (etRegisterInputType.inputType) {
                    InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS -> {
                        if (isValidEmail(etRegisterInputType.text.toString())) {
                            var isEmailAddress = false
                            mReference.child("users")
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onCancelled(error: DatabaseError) {
                                        println(error)
                                        Log.e(TAG, "onCancelled: " + error.message.toString())
                                    }

                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if (snapshot.value != null) {
                                            for (user in snapshot.children) {
                                                val userEmailFromDatabase =
                                                    user.getValue(UserAccount::class.java)
                                                if (userEmailFromDatabase!!.email!!.equals(
                                                        etRegisterInputType.text.toString()
                                                    )
                                                ) {
                                                    isEmailAddress = true
                                                    Toast.makeText(
                                                        this@RegisterActivity,
                                                        "Email is in use ",
                                                        Toast.LENGTH_SHORT
                                                    )
                                                        .show()
                                                    break
                                                }
                                            }
                                            if (!isEmailAddress) {
                                                resgisterRootConstLayou.visibility = View.GONE
                                                registerFrameLayout.visibility = View.VISIBLE
                                                val transaction =
                                                    supportFragmentManager.beginTransaction()
                                                transaction.replace(
                                                    R.id.registerFrameLayout,
                                                    RegisterUserFragment()
                                                )
                                                transaction.addToBackStack("emailApplayFragment")
                                                transaction.commit()
                                                EventBus.getDefault()
                                                    .postSticky(
                                                        EventBusData.SendRegisterData(
                                                            null,
                                                            etRegisterInputType.text.toString(),
                                                            null,
                                                            true
                                                        )
                                                    )

                                            }
                                        }

                                    }


                                })


                        } else {
                            Toast.makeText(
                                this,
                                "${etRegisterInputType.text} : is not valid email",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    InputType.TYPE_CLASS_PHONE -> {
                        if (isValidPhoneNumber(etRegisterInputType.text.toString())) {
                            var isPhoneNumber = false
                            mReference.child("users")
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onCancelled(error: DatabaseError) {
                                        Log.e(TAG, "onCancelled: $error")
                                        println(error)
                                    }

                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if (snapshot.value != null) {
                                            for (user in snapshot.children) {
                                                val userAccountFornumber =
                                                    user.getValue(UserAccount::class.java)
                                                if (userAccountFornumber!!.phone_number!!.equals(
                                                        etRegisterInputType.text.toString()
                                                    )
                                                ) {
                                                    isPhoneNumber = true
                                                    Toast.makeText(
                                                        this@RegisterActivity,
                                                        "Phone is in use ",
                                                        Toast.LENGTH_SHORT
                                                    )
                                                        .show()
                                                    break
                                                }
                                            }
                                            if (!isPhoneNumber) {
                                                resgisterRootConstLayou.visibility = View.GONE
                                                registerFrameLayout.visibility = View.VISIBLE
                                                val transaction =
                                                    supportFragmentManager.beginTransaction()
                                                transaction.replace(
                                                    R.id.registerFrameLayout,
                                                    PhoneActivationFragment()
                                                )
                                                transaction.addToBackStack("phoneApplayFragment")
                                                transaction.commit()
                                                EventBus.getDefault()
                                                    .postSticky(
                                                        EventBusData.SendRegisterData(
                                                            etRegisterInputType.text.toString(),
                                                            null,
                                                            null,
                                                            false
                                                        )
                                                    )
                                            }
                                        }
                                    }

                                })

                        } else {
                            Toast.makeText(
                                this,
                                "${etRegisterInputType.text} : is not valid Phone number",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    else -> {
                        println(etRegisterInputType.inputType.toString())
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


    }

    private fun isValidPhoneNumber(userNumber: String): Boolean {
        if (userNumber == "") {
            return false
        } else if (userNumber[0] == '+' && userNumber.length > 11 && userNumber.length < 16) {
            return android.util.Patterns.PHONE.matcher(userNumber).matches()
        }
        return false
    }

    private fun isValidEmail(userEmail: String): Boolean {
        return if (userEmail == "") {
            false
        } else {
            android.util.Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()
        }
    }

    /* override fun onBackPressed() {

         super.onBackPressed()
     }*/

    override fun onBackStackChanged() {
        val managerCount = supportFragmentManager.backStackEntryCount
        if (managerCount == 0) {
            resgisterRootConstLayou.visibility = View.VISIBLE
            registerFrameLayout.visibility = View.GONE
        }
    }

    private fun setUpAuthListener() {
        mAuthListener= FirebaseAuth.AuthStateListener {
            val user=FirebaseAuth.getInstance().currentUser
            if (user!= null) {
                val intent = Intent(
                    this@RegisterActivity,
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