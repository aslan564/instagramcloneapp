package com.aslanovaslan.instakloneapp.Search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.aslanovaslan.instakloneapp.R
import com.aslanovaslan.instakloneapp.login.LoginActivity
import com.aslanovaslan.instakloneapp.utils.BottonNavigationHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.bnve
import kotlinx.android.synthetic.main.activity_profileuser.*

class SearchActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private val TAG="SearchActivity"
    private val ACTIVITY_NO=1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeAllVariable()

        setNavigationBotomMenu();
    }

    private fun initializeAllVariable() {
        try {
            mAuth=Firebase.auth

        } catch (e: FirebaseAuthException) {
            Log.e(TAG, "initializeAllVariable: $e")
            e.printStackTrace()
        }
    }

    private fun setNavigationBotomMenu() {
        BottonNavigationHelper.setupButtonNavigationLocation(this,bnve)
        val menu=bnve.menu
        val menuItem=menu.getItem(ACTIVITY_NO)
        menuItem.isChecked = true
    }
    private fun setUpAuthListener(user: FirebaseUser?) {

        if (user == null) {
            val intent = Intent(
                this@SearchActivity,
                LoginActivity::class.java
            ).addFlags(
                Intent.FLAG_ACTIVITY_NO_ANIMATION
            )
            intent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP
                        or Intent.FLAG_ACTIVITY_CLEAR_TASK
            )
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
        try {
            val currentUser = mAuth.currentUser
            if (mAuth.currentUser != null) {
                val user = FirebaseAuth.getInstance().currentUser!!
                setUpAuthListener(user)
            }
        } catch (e: FirebaseAuthException) {
            e.printStackTrace()
        }

    }

    companion object {
        private val TAG = "HomeActivity"
    }
}