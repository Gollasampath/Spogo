package com.pss.spogoo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log


class Splash_screen : Activity() {
    lateinit var sharedPreferences: SharedPreferences

    // This is the loading time of the splash screen
    private val SPLASH_TIME_OUT: Long = 3000 // 1 sec
    var isLogined: Boolean = false
    var communityisLogined: Boolean = false
    lateinit var logintype: String
    var generalbuttonon: Boolean = false
    var communitybuttonon: Boolean = false
    lateinit var communitylogintype: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)

        Handler().postDelayed({
            // This method will be executed once the timer is over
            // Start your app main activity
            sharedPreferences = getSharedPreferences("loginprefs", Context.MODE_PRIVATE)


            isLogined = sharedPreferences.getBoolean("isLogin", false)

            generalbuttonon = sharedPreferences.getBoolean("generalbuttonon", false)
            communitybuttonon = sharedPreferences.getBoolean("communitybuttonon", false)

            Log.e("isLogin", "" + isLogined)
            logintype = sharedPreferences.getString("logintype", "")!!

            if (isLogined) {

                intent = Intent(this, Home_Screen::class.java)
                startActivity(intent)
                finish()
            } else {

                intent = Intent(
                    this,
                    Home_Screen::class.java
                )
                startActivity(intent)
                finish()
            }


            // close this activity
        }, SPLASH_TIME_OUT)
    }

}
