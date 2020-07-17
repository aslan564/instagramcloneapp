package com.aslanovaslan.instakloneapp.login

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import com.aslanovaslan.instakloneapp.R
import com.aslanovaslan.instakloneapp.utils.EventBusData
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.fragment_phone_activation.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.EventBusException
import org.greenrobot.eventbus.Subscribe
import java.util.concurrent.TimeUnit

class PhoneActivationFragment : Fragment() {
    private lateinit var phoneNumber: String
    private lateinit var progressBar: ProgressBar
    private lateinit var verificationIdIn: String
    private var comeSmsCodeInPhone = ""
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_phone_activation, container, false)
        progressBar = view.findViewById(R.id.progressBarWaitMessageCode)
        view.tvPhoneNumber.text = phoneNumber
        setupCallBacks()

        view.tvLoginSendFragPhone2.setOnClickListener{
            val intent = Intent(
                activity,
                LoginActivity::class.java
            ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }

        view.btnCheckPhoneNumber.setOnClickListener {
            val etSmsValue = view.etConfirmSmsCodePhone.text.toString()
            if (comeSmsCodeInPhone == etSmsValue && etSmsValue != "") {
                try {
                    EventBus.getDefault().postSticky(
                        EventBusData.SendRegisterData(
                            phoneNumber,
                            null,
                            comeSmsCodeInPhone,
                            false
                        )
                    )
                    val transaction = activity!!.supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.registerFrameLayout, RegisterUserFragment())
                    transaction.addToBackStack("RegisterUserwithphone")
                    transaction.commit()

                } catch (e: EventBusException) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(view.context, "Dogrulama Duzgun deyil", Toast.LENGTH_SHORT).show()

            }
        }

        this.activity?.let {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber, // Phone number to verify
                60, // Timeout duration
                TimeUnit.SECONDS, // Unit of timeout
                it, // Activity (for callback binding)
                callbacks
            )
        } // OnVerificationStateChangedCallbacks
        println(phoneNumber)
        return view
    }

    private fun setupCallBacks() {
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                if (!credential.smsCode.isNullOrEmpty()) {
                    comeSmsCodeInPhone = credential.smsCode.toString()
                    progressBar.visibility = View.INVISIBLE
                } else {
                    Log.d(TAG, "onVerificationCompleted: $credential")
                }

            }

            override fun onVerificationFailed(e: FirebaseException) {
                e.printStackTrace()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                if (verificationId != "") {
                    verificationIdIn = verificationId
                    progressBar.visibility = View.VISIBLE
                } else {
                    Log.d(TAG, "onCodeSent: Error verifications")
                }
            }
        }


    }

    @Subscribe(sticky = true)
    internal fun getDataFromEventBus(sendRegisterData: EventBusData.SendRegisterData) {
        phoneNumber = sendRegisterData.sendNumber!!
        // println(phone)
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