package com.pss.spogoo.ui.search


import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pss.spogoo.*
import com.pss.spogoo.api.APIClient
import com.pss.spogoo.api.Api
import com.pss.spogoo.models.UserLoginListResponse

import com.pss.spogoo.util.NetWorkConection
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.Logger

class AccountProfile_Fragment : Fragment() {

    lateinit var username: TextView
    lateinit var usergender: TextView
    lateinit var useraddress: TextView
    lateinit var phonenum: TextView
    lateinit var useremail: TextView
    lateinit var age: TextView
    lateinit var termsandconditions: TextView
    lateinit var logout: TextView
    lateinit var privacypolicy: TextView
    lateinit var aboutus: TextView
    lateinit var profilepicview: ImageView
    lateinit var editlayout: LinearLayout
    lateinit var profilelayout: LinearLayout
    lateinit var profileupdatebutton: Button
    lateinit var editusername: EditText
    lateinit var editage: EditText
    lateinit var editphonenum: EditText
    lateinit var edituseremail: EditText
    lateinit var edituseraddress: EditText

    lateinit var username_stng: String
    lateinit var phonenum_strng: String
    lateinit var useremail_stng: String
    lateinit var useraddress_stng: String
    lateinit var userage_stng: String
    lateinit var viewage: View
    lateinit var agelayout: RelativeLayout
    lateinit var profileprogressBar: ProgressBar
    private var mImageFileLocation = ""
    private var camerapath: String? = null
    private var picturePath: String? = null
    private var fileUri: Uri? = null
    lateinit var imagename: MultipartBody.Part
    lateinit var sharedPreferences: SharedPreferences
    private val STORAGE_REQUEST_CODE = 101
    private val CAMERA_REQUEST_CODE = 123

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.profile_screen, container, false)

        val editimage = root.findViewById(R.id.editimage) as ImageView
        username = root.findViewById(R.id.username) as TextView
        logout = root.findViewById(R.id.logout) as TextView
        viewage = root.findViewById(R.id.viewage) as View
        useraddress = root.findViewById(R.id.useraddress) as TextView
        phonenum = root.findViewById(R.id.phonenum) as TextView
        aboutus = root.findViewById(R.id.aboutus) as TextView
        agelayout = root.findViewById(R.id.agelayout) as RelativeLayout
        privacypolicy = root.findViewById(R.id.privacypolicy) as TextView
        termsandconditions = root.findViewById(R.id.termsandconditions) as TextView
        useremail = root.findViewById(R.id.useremail) as TextView
        age = root.findViewById(R.id.age) as TextView
        profilepicview = root.findViewById(R.id.profilepicview_edit) as ImageView
        editlayout = root.findViewById(R.id.editlayout) as LinearLayout
        profilelayout = root.findViewById(R.id.profilelayout) as LinearLayout
        profileupdatebutton = root.findViewById(R.id.profileupdatebutton) as Button
        editusername = root.findViewById(R.id.editusername) as EditText
        editphonenum = root.findViewById(R.id.editphonenum) as EditText
        edituseremail = root.findViewById(R.id.edituseremail) as EditText
        edituseraddress = root.findViewById(R.id.edituseraddress) as EditText
        editage = root.findViewById(R.id.editage) as EditText
        profileprogressBar = root.findViewById(R.id.profileprogressBar) as ProgressBar

        sharedPreferences =
            requireContext().getSharedPreferences("loginprefs", Context.MODE_PRIVATE)

        setupPermissions()
        getusersProfile()

        termsandconditions.setOnClickListener {
            startActivity(Intent(activity, TermsandConditions_Screen::class.java))

        }

        privacypolicy.setOnClickListener {
            startActivity(Intent(activity, PrivacyPolicy_Screen::class.java))
        }

        logout.setOnClickListener {

            var editor = sharedPreferences.edit()
            editor.clear().commit()
            startActivity(Intent(activity, UserLogin_Screen::class.java))
        }


        aboutus.setOnClickListener {
            startActivity(Intent(activity, AboutUs_Screen::class.java))
        }
        profilepicview.setOnClickListener {

            selectImage()
            Log.e("camera", "camera")
        }

//        username.text = sharedPreferences.getString("updateusename", "")
//        age.text = sharedPreferences.getString("updateuserage", "")
//        phonenum.text = sharedPreferences.getString("updateuserpfmobile", "")
//        useremail.text = sharedPreferences.getString("updateuseremail", "")
        editimage.setOnClickListener {

            editlayout.visibility = View.VISIBLE
            profilelayout.visibility = View.GONE
            profileupdatebutton.visibility = View.VISIBLE



            editusername.setText(sharedPreferences.getString("updateusename", ""))
            edituseremail.setText(sharedPreferences.getString("updateuseremail", ""))
            edituseraddress.setText(sharedPreferences.getString("updateusercity", ""))
            editphonenum.setText(sharedPreferences.getString("updateuserpfmobile", ""))
            editage.setText(sharedPreferences.getString("updateuserage", ""))

            profileupdatebutton.setOnClickListener {

                userUpdate()

            }
        }


        return root
    }

    //allow camera and storage permissions
    private fun setupPermissions() {
        val permission = activity?.let {
            ContextCompat.checkSelfPermission(
                it,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }

        val permisison2 =
            activity?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.CAMERA) }
        if (permisison2 != PackageManager.PERMISSION_GRANTED) {
            makeRequest2()
        }

        val permisison3 =
            activity?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            }
        if (permisison3 != PackageManager.PERMISSION_GRANTED) {
            makeRequest3()
        }

    }

    private fun makeRequest() {
        activity?.let {
            ActivityCompat.requestPermissions(
                it,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                STORAGE_REQUEST_CODE
            )
        }
    }

    private fun makeRequest3() {
        activity?.let {
            ActivityCompat.requestPermissions(
                it,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                STORAGE_REQUEST_CODE
            )
        }
    }

    private fun makeRequest2() {
        activity?.let {
            ActivityCompat.requestPermissions(
                it,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_REQUEST_CODE
            )
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            STORAGE_REQUEST_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(activity, "Permission Denied", Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(activity, "Permission Granted", Toast.LENGTH_SHORT).show()

                }
            }
        }
    }


    private fun selectImage() {
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Add Photo!")
        builder.setItems(options) { dialog, item ->
            if (options[item] == "Take Photo") {


                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, 1)


            } else if (options[item] == "Choose from Gallery") {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, 2)
            } else if (options[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {


                val extras = data?.extras
//
                val imageBitmap = extras?.get("data") as Bitmap
                val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmSS").format(Date())
                val imageFileName = timeStamp
                // Here we specify the environment location and the exact path where we want to save the so-created file
                val storageDirectory =
                    (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS))
                Logger.getAnonymousLogger().info("Storage directory set")


                val image = File.createTempFile(imageFileName, ".jpg", storageDirectory)

                // Here the location is saved into the string mImageFileLocation
                Logger.getAnonymousLogger().info("File name and path set")

                var out: FileOutputStream? = null
                try {
                    out = FileOutputStream(image)
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                    out.flush()
                    out.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                camerapath = image.absolutePath
                fileUri = Uri.parse(mImageFileLocation)
                Log.e("camerauri", "" + fileUri)

                profilepicview.setImageBitmap(imageBitmap)
                val imageString = Base64.encodeToString(
                    getBytesFromBitmap(imageBitmap),
                    Base64.NO_WRAP
                )

                Log.e("imagename", "" + imageString)

//                postPath = camerapath
//
                userImageUpdate(imageString)
            } else if (requestCode == 2) {
                if (data != null) {
                    // Get the Image from data
                    val selectedImage = data.data
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

                    val cursor = requireActivity().contentResolver.query(
                        selectedImage!!,
                        filePathColumn,
                        null,
                        null,
                        null
                    )
                    assert(cursor != null)
                    cursor!!.moveToFirst()

                    val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                    picturePath = cursor.getString(columnIndex)
                    // Set the Image in ImageView for Previewing the Media
                    var imageBitmap = BitmapFactory.decodeFile(picturePath)
                    profilepicview.setImageBitmap(imageBitmap)
                    cursor.close()

                    Log.e("galleryPath", "" + picturePath)

                    profilepicview.setImageBitmap(imageBitmap)
                    val imageString = Base64.encodeToString(
                        getBytesFromBitmap(imageBitmap),
                        Base64.NO_WRAP
                    )

                    Log.e("imagename", "" + imageString)
//                    postPath = picturePath
//
                    userImageUpdate(imageString)
                }

            }
        }
    }

    fun getBytesFromBitmap(bitmap: Bitmap): ByteArray? {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
        return stream.toByteArray()
    }

    private fun getusersProfile() {


        if (NetWorkConection.isNEtworkConnected(requireActivity())) {


            //Set the Adapter to the RecyclerView//

            var apiServices = APIClient.client.create(Api::class.java)

            Log.e("API : ", "" + apiServices)

            var userid = sharedPreferences.getString("userid", "")
            Log.e("userid", userid)

            val call = apiServices.getuserlogin("user", userid.toString())
            Log.e("API users : ", "" + call)
//

            call.enqueue(object : Callback<UserLoginListResponse> {
                override fun onResponse(
                    call: Call<UserLoginListResponse>,
                    response: Response<UserLoginListResponse>
                ) {

                    var status = response.body()?.status
                    if (status == true) {


                        Log.e("user response", response.toString())
                        var loginresponse = response.body()?.response


                        val pimage: String? = loginresponse!!.profile_img
                        Log.e("get user response: ", "" + loginresponse.profile_img)

                        val pfemail: String? = loginresponse.email_id
                        val pfname: String? = loginresponse.full_name
                        val pfmobile = loginresponse.mobile_number
                        val pfimage: String? = loginresponse.profile_img
                        val pfage: String? = loginresponse.age

                        val editor = sharedPreferences.edit()
                        editor.putString("updateusename", pfname)
                        editor.putString("updateuseremail", pfemail)
                        editor.putString("updateuserage", pfage)
                        editor.putString("updateuserpfimage", pfimage)
                        editor.putString("updateuserpfmobile", pfmobile.toString())
                        editor.commit()

                        Log.e("updateuserpfmobile", pfmobile.toString())
                        username.text = pfname
                        useremail.text = pfemail
                        if (pfage.equals("") || (pfage == null)) {
                            agelayout.visibility = View.GONE
                            viewage.visibility = View.GONE
                        } else {
                            age.text = pfage

                        }
                        phonenum.text = pfmobile.toString()

                        Log.e("profile editname", pfname)

                        Glide.with(activity!!).load(loginresponse.profile_img)
                            .placeholder(R.drawable.user_icon)
                            .apply(RequestOptions().centerCrop())
                            .into(profilepicview)


                    }
                }

                override fun onFailure(call: Call<UserLoginListResponse>, t: Throwable?) {
                    Log.e("response", t!!.message)
                }
            })


        } else {


            Toast.makeText(activity, "Please Check your internet", Toast.LENGTH_LONG).show()
        }
    }

    private fun userUpdate() {

        username_stng = editusername.text.toString().trim()
        phonenum_strng = editphonenum.text.toString().trim()
        useremail_stng = edituseremail.text.toString().trim()
        useraddress_stng = edituseraddress.text.toString().trim()
        userage_stng = editage.text.toString().trim()


        profileprogressBar.visibility = View.VISIBLE
        var apiServices = APIClient.client.create(Api::class.java)

        var userid = sharedPreferences.getString("userid", "")


        val call = apiServices

            .getuserupdatelogin(
                "user",
                userid.toString(),
                username_stng,
                useremail_stng,
                phonenum_strng,
                userage_stng
            )

        call.enqueue(object : retrofit2.Callback<UserLoginListResponse> {
            override fun onResponse(
                call: Call<UserLoginListResponse>,
                response: Response<UserLoginListResponse>
            ) {
                profileprogressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val dr = response.body()
                    var loginresponse = response.body()?.response
                    username.text = loginresponse?.full_name
                    age.text = loginresponse?.age
                    phonenum.text = loginresponse?.mobile_number.toString()
                    useremail.text = loginresponse?.email_id

                    val editor = sharedPreferences.edit()
                    editor.putString("updateusename", loginresponse?.full_name)
                    editor.putString("updateuseremail", loginresponse?.email_id)
                    editor.putString("updateusercity", "")
                    editor.putString("updateuserpfmobile", loginresponse?.mobile_number.toString())
                    editor.putString("updateuserage", loginresponse?.age)
                    editor.commit()
                    Log.e("username update", loginresponse?.full_name)

                    var clickintent = Intent(activity, Home_Screen::class.java)
                    startActivity(clickintent)
                    activity!!.finish()
                    Toast.makeText(activity, "Profile Updated Sucessful", Toast.LENGTH_LONG).show()

                }
            }

            override fun onFailure(call: Call<UserLoginListResponse>, t: Throwable) {
                profileprogressBar.visibility = View.GONE

                Toast.makeText(activity, "Profile updated Failed", Toast.LENGTH_LONG).show()

            }
        })


    }

    private fun userImageUpdate(imageString: String) {


        profileprogressBar.visibility = View.VISIBLE
        var apiServices = APIClient.client.create(Api::class.java)

        var userid = sharedPreferences.getString("userid", "")


        val call = apiServices

            .getuserupdateimgelogin(
                "user",
                userid.toString(),
                imageString
            )

        call.enqueue(object : retrofit2.Callback<UserLoginListResponse> {
            override fun onResponse(
                call: Call<UserLoginListResponse>,
                response: Response<UserLoginListResponse>
            ) {
                profileprogressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val dr = response.body()
                    var loginresponse = response.body()?.response
                    var profileimage = loginresponse?.profile_img


                    activity?.let {
                        Glide.with(it).load(loginresponse?.profile_img)
                            .placeholder(R.drawable.ic_user_icon)
                            .apply(RequestOptions().centerCrop())

                            .into(profilepicview)
                    }
                    Log.e("image update", loginresponse?.profile_img)

                    var clickintent = Intent(activity, Home_Screen::class.java)
                    startActivity(clickintent)
                    activity!!.finish()
                    Toast.makeText(activity, "Profile Updated Sucessful", Toast.LENGTH_LONG).show()

                }
            }

            override fun onFailure(call: Call<UserLoginListResponse>, t: Throwable) {
                profileprogressBar.visibility = View.GONE

                Toast.makeText(activity, "Profile updated Failed", Toast.LENGTH_LONG).show()

            }
        })


    }


}
