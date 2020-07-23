package com.pss.spogoo

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.example.parsaniahardik.kotlin_image_slider.SlidingOnboadingImage_Adapter
import com.pss.spogoo.models.ImageModel
import com.pss.spogoo.util.ManagePermissions
import com.viewpagerindicator.CirclePageIndicator
import java.util.*
import kotlin.collections.ArrayList

class Onboarding_Screen : Activity() {

    private var imageModelArrayList: ArrayList<ImageModel>? = null
    lateinit var indicator: CirclePageIndicator
    private val PermissionsRequestCode = 123
    private lateinit var managePermissions: ManagePermissions


    private val myImageList = intArrayOf(
        R.drawable.onboard5,
        R.drawable.onboard6,
        R.drawable.onboard7,
        R.drawable.onboard8
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.onboarding1_screen)

        val skipbutton = findViewById<Button>(R.id.skipbutton)
        //initializing the modelslist
        imageModelArrayList = ArrayList()
        imageModelArrayList = populateList()

        //runtime permissions

        val list = listOf<String>(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_CALENDAR
        )
        imageSliderView()
        managePermissions = ManagePermissions(this, list, PermissionsRequestCode)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            managePermissions.checkPermissions()
        //skip button listener
        skipbutton.setOnClickListener {

            intent = Intent(applicationContext, Home_Screen::class.java)
            startActivity(intent)
            finish()
        }

    }

    //images auto slide
    private fun populateList(): ArrayList<ImageModel> {

        val list = ArrayList<ImageModel>()

        for (i in 0..3) {
            val imageModel = ImageModel()
            imageModel.setImage_drawables(myImageList[i])
            list.add(imageModel)
        }

        return list
    }


    private fun imageSliderView() {


        onbordingsliderpager = findViewById<ViewPager>(R.id.onbordingsliderpager)

        indicator = findViewById<CirclePageIndicator>(R.id.circleindicator)
        onbordingsliderpager!!.adapter =
            SlidingOnboadingImage_Adapter(this, this.imageModelArrayList!!)

        indicator.setViewPager(onbordingsliderpager)

        val density = resources.displayMetrics.density

        //Set circle indicator radius
        indicator.radius = 4 * density

        NUM_PAGES = imageModelArrayList!!.size

        // Auto start of viewpager
        val handler = Handler()
        val Update = Runnable {
            if (currentPage == NUM_PAGES) {
                currentPage = 0
            }
            onbordingsliderpager!!.setCurrentItem(currentPage++, true)
        }
        val swipeTimer = Timer()
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(Update)
            }
        }, 3000, 3000)

        // Pager listener over indicator
        indicator.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageSelected(position: Int) {
                currentPage = position

            }

            override fun onPageScrolled(pos: Int, arg1: Float, arg2: Int) {

            }

            override fun onPageScrollStateChanged(pos: Int) {

            }
        })

    }

    companion object {

        private var onbordingsliderpager: ViewPager? = null
        private var currentPage = 0
        private var NUM_PAGES = 0
    }


    // Receive the permissions request result
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PermissionsRequestCode -> {
                val isPermissionsGranted = managePermissions
                    .processPermissionsResult(requestCode, permissions, grantResults)

                if (isPermissionsGranted) {
                    // Do the task now
                    Toast.makeText(this, "Permissions granted.", Toast.LENGTH_LONG)
                } else {
                    Toast.makeText(this, "Permissions denied.", Toast.LENGTH_LONG)

                }
                return
            }
        }
    }


}