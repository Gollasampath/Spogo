package com.pss.spogoo

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.pss.spogoo.api.APIClient
import com.pss.spogoo.api.Api
import com.pss.spogoo.models.UserSignupListResponse
import retrofit2.Call
import retrofit2.Response
import java.util.concurrent.TimeUnit

class SignupOtp_Screen : AppCompatActivity() {
    lateinit var verfybutton: Button
    lateinit var otpedittext1: EditText

    lateinit var mobilenumtext: TextView
    lateinit var mobile: String
    private lateinit var mVerificationId: String
    lateinit var sharedPreferences: SharedPreferences
    lateinit var email: String
    lateinit var name: String
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.otp_screen)
        verfybutton = findViewById(R.id.verfybutton)

        mAuth = FirebaseAuth.getInstance()
        otpedittext1 = findViewById(R.id.otpedittext1)

        mobilenumtext = findViewById(R.id.mobilenumtext)

        sharedPreferences =
            getSharedPreferences(
                "loginprefs",
                Context.MODE_PRIVATE
            )
        val intent = intent

        mobile = intent.getStringExtra("mobile")
        email = intent.getStringExtra("email")
        name = intent.getStringExtra("name")
        sendVerificationCode(mobile)

        mobilenumtext.text = "6 digit code sent to +91 " + mobile
        Log.e("signupotp", mobile)


        verfybutton.setOnClickListener {
            val otp_stng1 = otpedittext1.text.toString().trim()

            if (otp_stng1.isEmpty()) {
                otpedittext1.error = "Enter valid code"
                otpedittext1.requestFocus()
            } else {
                verifyVerificationCode(otp_stng1)

            }

        }

    }


    private fun verifyVerificationCode(code: String) { //creating the credential
        val credential = PhoneAuthProvider.getCredential(mVerificationId, code)
        //signing the user
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) { //verification successful we will start the profile activity
//                    val intent =
//                        Intent(this@Verifyphone_Activity, Home_Screen::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                    startActivity(intent)
//                    finish()

                    postmobilenum()

                } else { //verification unsuccessful.. display an error message
                    var message =
                        "Somthing is wrong, we will fix it soon..."
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        message = "Invalid code entered..."
                    }

                    Toast.makeText(this, "" + message, Toast.LENGTH_LONG).show()
                    Log.e("otp sucess", message)

                }
            }
    }

    //the method is sending verification code
//the country id is concatenated
//you can take the country id as user input as well
    private fun sendVerificationCode(mobile: String) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            "+91$mobile",
            60,
            TimeUnit.SECONDS,
            TaskExecutors.MAIN_THREAD,
            mCallbacks
        )

        Log.e("signupotp", mobile)
    }


    //the callback to detect the verification status
    private val mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) { //Getting the code sent by SMS
                val code = phoneAuthCredential.smsCode
                //sometime the code is not detected automatically
//in this case the code will be null
//so user has to manually enter the code
                if (code != null) {


                    otpedittext1.setText(code)


                    //verifying the code
                    verifyVerificationCode(code)
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(this@SignupOtp_Screen, e.message, Toast.LENGTH_LONG).show()
            }

            override fun onCodeSent(
                s: String,
                forceResendingToken: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(s, forceResendingToken)
                //storing the verification id that is sent to the user
                mVerificationId = s
            }
        }


    private fun postmobilenum() {


        var apiServices = APIClient.client.create(Api::class.java)

        Log.e("API : ", "" + apiServices)

        val call = apiServices.usersignup("mobile", name, email, mobile)

        Log.e("API Cards : ", "" + call)
        Log.e("mobile : ", "" + mobile)

//        progressbar.visibility = View.VISIBLE

        call.enqueue(object : retrofit2.Callback<UserSignupListResponse> {


            override fun onResponse(
                call: Call<UserSignupListResponse>,
                response: Response<UserSignupListResponse>
            ) {
                val loginResponse = response.body()
//                progressbar.visibility = View.GONE
                var code = response.body()!!.code

                if (response.isSuccessful) {


                    Log.e(ContentValues.TAG, response.toString())
                    Log.e(" Login Response", "" + response.body())


                    var mobilenumres = response.body()!!.response.mobile_number

                    val editor = sharedPreferences.edit()
                    editor.putString("name", response.body()!!.response.full_name)
                    editor.putString("email", response.body()!!.response.email_id)
                    editor.putString("logintype", response.body()!!.response.user_type)
                    editor.putString("mobilenumber", mobilenumres.toString())
                    editor.putBoolean("isLogin", true)
                    editor.commit()
                    Log.e("logintype", response.body()!!.response.user_type)

                    startActivity(Intent(this@SignupOtp_Screen, LoginSucess_Screen::class.java))
                    finish()

                } else if ((code == 2) || (code.equals("2"))) {
                    Log.e("code login2", "" + code)

                    val editor = sharedPreferences.edit()
                    editor.putString(
                        "name",
                        response.body()!!.response.full_name
                    )
                    editor.putString(
                        "email",
                        response.body()!!.response.email_id
                    )

                    editor.putString(
                        "logintype",
                        response.body()!!.response.user_type
                    )

                    editor.putString(
                        "userid",
                        response.body()!!.response.user_id.toString()
                    )
                    editor.putBoolean("isLogin", true)
                    editor.commit()
                    Toast.makeText(
                        this@SignupOtp_Screen,
                        "Already Registred",
                        Toast.LENGTH_LONG
                    ).show()
                    startActivity(
                        Intent(
                            this@SignupOtp_Screen,
                            Home_Screen::class.java
                        )
                    )

                } else if ((code == 3) || (code.equals("3"))) {
                    Log.e("code login3", "" + code)
                    val editor = sharedPreferences.edit()
                    editor.putString(
                        "name",
                        response.body()!!.response.full_name
                    )
                    editor.putString(
                        "email",
                        response.body()!!.response.email_id
                    )
                    editor.putString(
                        "userid",
                        response.body()!!.response.user_id.toString()
                    )

                    editor.putString(
                        "logintype",
                        response.body()!!.response.user_type
                    )

                    editor.putBoolean("isLogin", true)
                    editor.commit()
                    Toast.makeText(
                        this@SignupOtp_Screen,
                        "Already Registred",
                        Toast.LENGTH_LONG
                    ).show()
                    startActivity(
                        Intent(
                            this@SignupOtp_Screen,
                            Home_Screen::class.java
                        )
                    )

                }
            }

            override fun onFailure(call: Call<UserSignupListResponse>, t: Throwable) {


//                progressbar.visibility = View.GONE

                Toast.makeText(this@SignupOtp_Screen, "Login Error", Toast.LENGTH_LONG).show()

            }
        })
    }

}
