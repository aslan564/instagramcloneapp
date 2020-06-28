package com.aslanovaslan.instakloneapp.News

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aslanovaslan.instakloneapp.R
import com.aslanovaslan.instakloneapp.utils.BottonNavigationHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.bnve
import kotlinx.android.synthetic.main.activity_profileuser.*

class NewsActivity : AppCompatActivity() {

    private val TAG="NewsActivity"
    private val ACTIVITY_NO=3
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        setNavigationBotomMenu();
    }

    fun setNavigationBotomMenu() {
        BottonNavigationHelper.setupButtonNavigationLocation(this,bnve)
        val menu=bnve.menu
        val menuItem=menu.getItem(ACTIVITY_NO)
        menuItem.isChecked = true
    }

}