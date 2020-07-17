package com.aslanovaslan.instakloneapp.profile

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.aslanovaslan.instakloneapp.login.LoginActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LogOutFragment : DialogFragment() {

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

		return AlertDialog.Builder(activity).setTitle("Logout from the site")
			.setMessage("Are you sure you want to quit")
			.setPositiveButton(
				"Exit"
			) { _, _ ->
				Firebase.auth.signOut()
				val intent = Intent(
					activity,
					LoginActivity::class.java
				).addFlags(
					Intent.FLAG_ACTIVITY_NO_ANIMATION
							or Intent.FLAG_ACTIVITY_CLEAR_TOP
							or Intent.FLAG_ACTIVITY_NEW_TASK
							or Intent.FLAG_ACTIVITY_CLEAR_TASK
				)
				startActivity(intent)
                activity!!.finish()
			}.setNegativeButton(
				"No"
			) { _, _ ->
				dismiss()
			}.create()
	}
}


