package com.pss.spogoo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.privacypolicy_screen.*

class PrivacyPolicy_Screen : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var logintype: String
    lateinit var backprivacy: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.privacypolicy_screen)

        sharedPreferences = getSharedPreferences("loginprefs", Context.MODE_PRIVATE)

        backprivacy = toolbar.findViewById(R.id.backprivacy) as ImageView
        logintype = sharedPreferences.getString("logintype", "")!!

        backprivacy.setOnClickListener {

//            if (logintype.equals("user")) {

            intent = Intent(this, Home_Screen::class.java)
            startActivity(intent)
            finish()
//            } else {
//                startActivity(Intent(this, CommunityHome_Screen::class.java))
//                finish()
//            }
        }

    }
}