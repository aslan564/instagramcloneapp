package com.aslanovaslan.instakloneapp.utils

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.MenuItem
import android.widget.Toast
import com.aslanovaslan.instakloneapp.Home.HomeActivity
import com.aslanovaslan.instakloneapp.News.NewsActivity
import com.aslanovaslan.instakloneapp.Profile.ProfileActivity
import com.aslanovaslan.instakloneapp.R
import com.aslanovaslan.instakloneapp.Search.SearchActivity
import com.aslanovaslan.instakloneapp.Share.ShareActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx

class BottonNavigationHelper {

    companion object {
        fun setupBottomNavigationView(bnve: BottomNavigationViewEx) {
            //bnve.enableAnimation(false);
              //bnve.selectedItemId
            //bnve.setBackgroundColor(Color.parseColor("#b8b8b8"))
            //bnve.enableShiftingMode(false);
            //bnve.enableItemShiftingMode(true);
        }

        fun setupButtonNavigationLocation(context: Context, bottomNavigationViewEx: BottomNavigationViewEx) {
            bottomNavigationViewEx.onNavigationItemSelectedListener =
                BottomNavigationView.OnNavigationItemSelectedListener { p0 ->
                    when (p0.itemId) {
                        R.id.ic_homeImage -> {
                            val intent: Intent = Intent(
                                context,
                                HomeActivity::class.java
                            ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                            context.startActivity(intent)
                        }
                        R.id.ic_searchImage -> {

                            val intent: Intent = Intent(
                                context,
                                SearchActivity::class.java
                            ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                            context.startActivity(intent)
                        }
                        R.id.ic_addphotoImage -> {
                            val intent: Intent = Intent(
                                context,
                                ShareActivity::class.java
                            ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                            context.startActivity(intent)

                        }
                        R.id.ic_newsImage -> {
                            val intent: Intent = Intent(
                                context,
                                NewsActivity::class.java
                            ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                            context.startActivity(intent)

                        }
                        R.id.ic_profileImage -> {
                            val intent: Intent = Intent(
                                context,
                                ProfileActivity::class.java
                            ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                            context.startActivity(intent)
                        }
                    }
                    false
                }

        }
    }
}


