package com.aslanovaslan.instakloneapp.Search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.aslanovaslan.instakloneapp.R
import com.aslanovaslan.instakloneapp.utils.BottonNavigationHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.bnve
import kotlinx.android.synthetic.main.activity_profileuser.*

class SearchActivity : AppCompatActivity() {

    private val TAG="SearchActivity"
    private val ACTIVITY_NO=1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setNavigationBotomMenu();
    }

    fun setNavigationBotomMenu() {
        BottonNavigationHelper.setupButtonNavigationLocation(this,bnve)
        val menu=bnve.menu
        val menuItem=menu.getItem(ACTIVITY_NO)
        menuItem.isChecked = true
    }
}