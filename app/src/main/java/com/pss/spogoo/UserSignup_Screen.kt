package com.pss.spogoo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class UserSignup_Screen : AppCompatActivity() {

    lateinit var name_edit: EditText
    lateinit var email_edit: EditText
    lateinit var mobileno_edit: EditText
    lateinit var usergetotp_btn: Button

    private lateinit var name_stng: String
    private lateinit var email_stng: String
    private lateinit var mobileno_stng: String
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_register_screen)

        name_edit = findViewById<EditText>(R.id.name_edit)
        email_edit = findViewById<EditText>(R.id.email_edit)
        mobileno_edit = findViewById<EditText>(R.id.mobileno_edit)
        usergetotp_btn = findViewById<Button>(R.id.usergetotp_btn)
        sharedPreferences =
            getSharedPreferences(
                "loginprefs",
                Context.MODE_PRIVATE
            )

        usergetotp_btn.setOnClickListener {
            name_stng = name_edit.text.toString()
            email_stng = email_edit.text.toString()
            mobileno_stng = mobileno_edit.text.toString()

            if (name_stng.isEmpty()) {
                name_edit.error = "Enter a Full Name"

            } else if (email_stng.isEmpty()) {
                email_edit.error = "Enter a Email ID"

            } else if (mobileno_stng.isEmpty()) {
                mobileno_edit.error = "Enter a Mobile Number"

            } else {


                val intent = Intent(this, SignupOtp_Screen::class.java)
                intent.putExtra("mobile", mobileno_stng)
                intent.putExtra("email", email_stng)
                intent.putExtra("name", name_stng)
                startActivity(intent)
                finish()


            }
        }


    }
}