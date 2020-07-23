package com.pss.spogoo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class LoginSucess_Screen : AppCompatActivity() {
    lateinit var start_button: Button
    lateinit var sharedPreferences: SharedPreferences
    lateinit var commintyloginprefs: SharedPreferences
    lateinit var logintype: String
    lateinit var communitylogintype: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loginsucess_screen)

        sharedPreferences =
            getSharedPreferences(
                "loginprefs",
                Context.MODE_PRIVATE
            )

        start_button = findViewById(R.id.start_button)

        logintype = sharedPreferences.getString("logintype", "")!!

        Log.e("usetype", logintype)

        start_button.setOnClickListener {

//            if (logintype.equals("user")) {

            startActivity(Intent(this, Home_Screen::class.java))
            finish()

//            } else {
//                startActivity(Intent(this, CommunityHome_Screen::class.java))
//                finish()
//
//            }

//            if (communitylogintype.equals("community")) {
//
//                startActivity(Intent(this, CommunityHome_Screen::class.java))
//
//            } else {
//                startActivity(Intent(this, Home_Screen::class.java))
//
//            }
        }

    }
}