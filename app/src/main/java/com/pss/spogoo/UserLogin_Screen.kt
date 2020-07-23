package com.pss.spogoo

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.facebook.CallbackManager
import com.facebook.FacebookSdk
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.pss.spogoo.api.APIClient
import com.pss.spogoo.api.Api
import com.pss.spogoo.models.UserSignupListResponse
import com.pss.spogoo.util.NetWorkConection
import retrofit2.Call
import retrofit2.Response

class UserLogin_Screen : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener,
    GoogleApiClient.ConnectionCallbacks {

    lateinit var generallogin_btn: ImageView
    lateinit var gplus_image: ImageView
    lateinit var fb_image: ImageView
    lateinit var communityogin_btn: ImageView
    lateinit var community_linear: LinearLayout
    lateinit var general_linear: LinearLayout
    lateinit var general_image: LinearLayout
    lateinit var getotpuser_btn: Button
    lateinit var signup_btn: TextView
    lateinit var usermobileedit: EditText
    lateinit var usermobilestrng: String
    lateinit var progressBarlogin: ProgressBar
    private val RC_SIGN_IN = 9001
    private var mGoogleApiClient: GoogleApiClient? = null
    private var locationManager: LocationManager? = null
    lateinit var sharedPreferences: SharedPreferences
    private var callbackManager: CallbackManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FacebookSdk.sdkInitialize(applicationContext)

        setContentView(R.layout.userlogin_screen)

        communityogin_btn = findViewById<ImageView>(R.id.communityogin_btn)
        generallogin_btn = findViewById<ImageView>(R.id.generallogin_btn)
        general_linear = findViewById<LinearLayout>(R.id.general_linear)
        general_image = findViewById<LinearLayout>(R.id.general_image)
        getotpuser_btn = findViewById<Button>(R.id.getotpuser_btn)
        signup_btn = findViewById<TextView>(R.id.signup_btn)
        usermobileedit = findViewById<EditText>(R.id.usermobileedit)
        fb_image = findViewById<ImageView>(R.id.fb_image)
        gplus_image = findViewById<ImageView>(R.id.gplus_image)
        progressBarlogin = findViewById<ProgressBar>(R.id.progressBarlogin)


        sharedPreferences =
            getSharedPreferences(
                "loginprefs",
                Context.MODE_PRIVATE
            )
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
        //google plus integration

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager


        mGoogleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()

        gplus_image.setOnClickListener {

            val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }


//        fb_image.setOnClickListener {
//
//
//            callbackManager = CallbackManager.Factory.create()
//            LoginManager.getInstance()
//                .logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))
//            LoginManager.getInstance().registerCallback(callbackManager,
//                object : FacebookCallback<LoginResult> {
//
//                    override fun onSuccess(loginResult: LoginResult) {
//
//                        Log.e("login", "Facebook token: " + loginResult.accessToken.token)
//                        Log.e("login", "Facebook token: " + loginResult.accessToken.userId)
//                        Log.d("FBLOGIN", loginResult.accessToken.token.toString())
//                        //                    Log.d("FBLOGIN", loginResult.recentlyGrantedPermissions.toString())
//
//
//                        val request =
//                            GraphRequest.newMeRequest(loginResult.accessToken) { `object`, response ->
//                                try {
//                                    var fbid = loginResult.accessToken.userId
//
//                                    var fbfirst_name = `object`.optString("name")
//                                    var fbemail = `object`.optString("email")
//                                    var fbtoken = loginResult.accessToken.token
//                                    //here is the data that you want
//                                    Log.e("FBLOGIN_JSON_RES", `object`.toString())
//                                    Log.e("fbUserFirstName", fbfirst_name)
//                                    Log.e("fbUserEmail", fbemail)
//                                    Log.e("fbUsertoken", fbtoken)
//
//                                    //facebook api integration
//
//                                    val call = RetrofitClient
//                                        .instance
//                                        .userfbsignup(
//                                            "facebook",
//                                            fbfirst_name,
//                                            fbemail,
//                                            "",
//                                            fbtoken,
//                                            ""
//                                        )
//
//
//                                    call.enqueue(object :
//                                        retrofit2.Callback<UserSignupListResponse> {
//                                        override fun onResponse(
//                                            call: Call<UserSignupListResponse>,
//                                            response: Response<UserSignupListResponse>
//                                        ) {
//                                            if (response.isSuccessful) {
//
//
//                                                val editor = sharedPreferences.edit()
//
//                                                var mobilenumres =
//                                                    response.body()!!.response.mobile_number
//                                                editor.putString(
//                                                    "name",
//                                                    response.body()!!.response.full_name
//                                                )
//                                                editor.putString(
//                                                    "email",
//                                                    response.body()!!.response.email_id
//                                                )
//
//                                                editor.putString(
//                                                    "logintype",
//                                                    response.body()!!.response.user_type
//                                                )
//
//                                                editor.putString(
//                                                    "mobilenumber",
//                                                    mobilenumres.toString()
//                                                )
//                                                editor.putBoolean("isLogin", true)
//                                                editor.commit()
//
//                                                Toast.makeText(
//                                                    this@UserLogin_Screen,
//                                                    "Registration Sucessful",
//                                                    Toast.LENGTH_LONG
//                                                ).show()
//                                                intent = Intent(
//                                                    applicationContext,
//                                                    Home_Screen::class.java
//                                                )
//                                                startActivity(intent)
//                                                finish()
//
//
//                                            }
//                                        }
//
//                                        override fun onFailure(
//                                            call: Call<UserSignupListResponse>,
//                                            t: Throwable
//                                        ) {
//
//                                            Toast.makeText(
//                                                this@UserLogin_Screen,
//                                                t.message,
//                                                Toast.LENGTH_LONG
//                                            ).show()
//
//                                        }
//                                    })
//
//
//                                } catch (e: Exception) {
//                                    e.printStackTrace()
//                                }
//                            }
//
//                        val parameters = Bundle()
//                        parameters.putString("fields", "name,email,id")
//
//                        request.parameters = parameters
//                        Log.e("fields", "" + parameters)
//
//                        request.executeAsync()
//
//                    }
//
//                    override fun onCancel() {
//                        Log.e("MainActivity", "Facebook onCancel.")
//
//                    }
//
//                    override fun onError(error: FacebookException) {
//                        Log.e("MainActivity", "Facebook onError." + error.message)
//
//                    }
//
//
//                })
//        }

        generallogin_btn.setOnClickListener {

            var editor = sharedPreferences.edit()
            var general_btnon = editor.putBoolean("generalbuttonon", true)
            editor.commit()
        }

//        communityogin_btn.setOnClickListener {
//            var editor = sharedPreferences.edit()
//            var community_btnon = editor.putBoolean("communitybuttonon", true)
//            editor.commit()
//            startActivity(Intent(this, CommunityLogin_Screen::class.java))
//            finish()
//        }

        getotpuser_btn.setOnClickListener {

            usermobilestrng = usermobileedit.text.toString().trim()

            if (usermobilestrng.isEmpty()) {
                usermobileedit.error = "Enter a Mobile Number"

            } else {
                val intent = Intent(this, LoginOtp_Screen::class.java)
                intent.putExtra("usermobile", usermobilestrng)
                startActivity(intent)
                finish()

            }

        }

        signup_btn.setOnClickListener {

            startActivity(Intent(this, UserSignup_Screen::class.java))
            finish()
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        //google
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)

            if (result!!.isSuccess) {


                var gname = result.signInAccount!!.displayName
                var gemail = result.signInAccount!!.email
                val gimage = result.signInAccount?.photoUrl
                val gid = result.signInAccount?.id
                val gtoken = result.signInAccount?.idToken


                Log.e("app login sucess", "" + gname)
                Log.e("app login sucess", "" + gemail)
                Log.e("app login sucess", "" + gid)
                Log.e("app login sucess", "" + gtoken)


                if (NetWorkConection.isNEtworkConnected(this)) {

                    try {

                        var apiServices = APIClient.client.create(Api::class.java)

                        Log.e("API : ", "" + apiServices)

                        val call =
                            apiServices.usergmailsignup("gmail", gname!!, gemail!!, "")

                        Log.e("API Cards : ", "" + call)
                        Log.e("gname : ", "" + gname)
                        Log.e("gemail : ", "" + gemail)

                        progressBarlogin.visibility = View.VISIBLE

                        call.enqueue(object : retrofit2.Callback<UserSignupListResponse> {


                            override fun onResponse(
                                call: Call<UserSignupListResponse>,
                                response: Response<UserSignupListResponse>
                            ) {
                                val loginResponse = response.body()
                                progressBarlogin.visibility = View.GONE
                                Log.e(ContentValues.TAG, response.toString())
                                var code = response.body()!!.code

                                if ((code == 1) || (code.equals("1"))) {


                                    Log.e("code login1", "" + code)

                                    var statusfalse = response.body()!!.status
                                    Log.e("status", "" + statusfalse)

                                    Log.e(" Login Response", "" + response.body())


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
                                    Log.e(
                                        "gmail logintype",
                                        response.body()!!.response.user_type
                                    )
                                    startActivity(
                                        Intent(
                                            this@UserLogin_Screen,
                                            Home_Screen::class.java
                                        )
                                    )
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
                                    Log.e(
                                        "gmail logintype",
                                        response.body()!!.response.user_type
                                    )
                                    Toast.makeText(
                                        this@UserLogin_Screen,
                                        "Already Registred with Email",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    startActivity(
                                        Intent(
                                            this@UserLogin_Screen,
                                            Home_Screen::class.java
                                        )
                                    )
                                    finish()

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
                                        "logintype",
                                        response.body()!!.response.user_type
                                    )
                                    editor.putString(
                                        "userid",
                                        response.body()!!.response.user_id.toString()
                                    )
                                    editor.putBoolean("isLogin", true)
                                    editor.commit()
                                    Log.e(
                                        "gmail logintype",
                                        response.body()!!.response.user_type
                                    )

                                    Toast.makeText(
                                        this@UserLogin_Screen,
                                        "Already Registred with Mobile",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    startActivity(
                                        Intent(
                                            this@UserLogin_Screen,
                                            Home_Screen::class.java
                                        )
                                    )
                                    finish()


                                } else {

                                    Log.e("gmail signup", "" + response.body()?.code)
                                }
                            }


                            override fun onFailure(
                                call: Call<UserSignupListResponse>,
                                t: Throwable
                            ) {


                                progressBarlogin.visibility = View.GONE

//                                Toast.makeText(
//                                    this@UserLogin_Screen,
//                                    "Already Registred",
//                                    Toast.LENGTH_LONG
//                                ).show()
//                                val editor = sharedPreferences.edit()
//
//                                editor.putBoolean("isLogin", true)
//                                editor.commit()
//                                Log.e("Login error", t.message)
//                                startActivity(
//                                    Intent(
//                                        this@UserLogin_Screen,
//                                        Home_Screen::class.java
//                                    )
//                                )
                            }
                        })

                    } catch (e: IllegalStateException) {
                        e.printStackTrace()
                    } catch (e: UninitializedPropertyAccessException) {
                        e.printStackTrace()
                    } catch (e: NumberFormatException) {
                        e.printStackTrace()
                    }
                } else {

                    Toast.makeText(this, "Please Check your internet", Toast.LENGTH_LONG).show()
                }
            }

        }
    }


    override fun onConnectionFailed(p0: ConnectionResult) {
        Log.e("bett", "onConnectionFailed:" + p0)
    }

    override fun onConnected(p0: Bundle?) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
    }

    override fun onConnectionSuspended(p0: Int) {
        Log.i("TAG", "Connection Suspended")
        mGoogleApiClient!!.connect()
    }


}