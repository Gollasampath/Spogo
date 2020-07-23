package com.pss.spogoo.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.parsaniahardik.kotlin_image_slider.ShopSlideImage_Adapter
import com.example.parsaniahardik.kotlin_image_slider.SlidingImage_Adapter
import com.pss.spogoo.R
import com.pss.spogoo.Rapair_Activity
import com.pss.spogoo.Shop_Activity
import com.pss.spogoo.models.ImageModel
import com.viewpagerindicator.CirclePageIndicator
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

    private var imagesliderpager: ViewPager? = null
    private var currentPage = 0
    private var NUM_PAGES = 0
    lateinit var buidprofile_button: Button
    lateinit var shop_image: ImageView
    lateinit var repair_image: ImageView
    lateinit var trainer_image: ImageView
    lateinit var retail_image: ImageView
    private var imageModelArrayList: ArrayList<ImageModel>? = null
    lateinit var shopcircleindicator: CirclePageIndicator

    private val myImageList = intArrayOf(
        R.drawable.sli4,
        R.drawable.sli5,
        R.drawable.sli1
    )

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.home_screen, container, false)

        buidprofile_button = root.findViewById<Button>(R.id.buidprofile_button)
        shop_image = root.findViewById<ImageView>(R.id.shop_image)
        trainer_image = root.findViewById<ImageView>(R.id.trainer_image)
        retail_image = root.findViewById<ImageView>(R.id.retail_image)
        repair_image = root.findViewById<ImageView>(R.id.repair_image)

        shopcircleindicator = root.findViewById<CirclePageIndicator>(R.id.shopcircleindicator)

        imagesliderpager = root.findViewById<ViewPager>(R.id.imagesliderpager)
        //initializing the modelslist
        imageModelArrayList = ArrayList()
        imageModelArrayList = populateList()

        imageSliderView()

        imagesliderpager!!.clipToPadding = false
        imagesliderpager!!.pageMargin = 10
        imagesliderpager!!.setPadding(40, 0, 40, 0)

        imagesliderpager!!.adapter = context?.let {
            SlidingImage_Adapter(
                it,
                imageModelArrayList!!
            )
        }
        NUM_PAGES = imageModelArrayList!!.size

        // Auto start of viewpager
        val handler = Handler()
        val Update = Runnable {
            if (currentPage == NUM_PAGES) {
                currentPage = 0
            }
            imagesliderpager!!.setCurrentItem(currentPage++, true)
        }

        val swipeTimer = Timer()
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(Update)
            }
        }, 3000, 3000)



        buidprofile_button.setOnClickListener {

//            val navController = Navigation.findNavController(activity!!, R.id.nav_host_fragment)
//            navController.navigate(R.id.action_nav_home_to_nav_buildprofile)

            Toast.makeText(context, "Available Soon", Toast.LENGTH_LONG).show()


        }
        shop_image.setOnClickListener {
            startActivity(Intent(activity, Shop_Activity::class.java))

            shop_image.setImageResource(R.drawable.shopgreen)
            repair_image.setImageResource(R.drawable.repairnormal)
            retail_image.setImageResource(R.drawable.retailernormal)
            trainer_image.setImageResource(R.drawable.trainernormal)
//            val navController = Navigation.findNavController(activity!!, R.id.nav_host_fragment)
//            navController.navigate(R.id.action_nav_home_to_nav_shop)

        }
        repair_image.setOnClickListener {
//
//            val navController = Navigation.findNavController(activity!!, R.id.nav_host_fragment)
//            navController.navigate(R.id.action_nav_home_to_nav_repair)

            startActivity(Intent(activity, Rapair_Activity::class.java))
            shop_image.setImageResource(R.drawable.shopnormal)
            repair_image.setImageResource(R.drawable.repairgreen)
            retail_image.setImageResource(R.drawable.retailernormal)
            trainer_image.setImageResource(R.drawable.trainernormal)

        }

        retail_image.setOnClickListener {
//
//            val navController = Navigation.findNavController(activity!!, R.id.nav_host_fragment)
//            navController.navigate(R.id.action_nav_home_to_nav_repair)

            shop_image.setImageResource(R.drawable.shopnormal)
            repair_image.setImageResource(R.drawable.repairnormal)
            retail_image.setImageResource(R.drawable.retailergreen)
            trainer_image.setImageResource(R.drawable.trainernormal)

            Toast.makeText(context, "Available Soon", Toast.LENGTH_LONG).show()

        }

        trainer_image.setOnClickListener {
//
//            val navController = Navigation.findNavController(activity!!, R.id.nav_host_fragment)
//            navController.navigate(R.id.action_nav_home_to_nav_repair)

            shop_image.setImageResource(R.drawable.shopnormal)
            repair_image.setImageResource(R.drawable.repairnormal)
            retail_image.setImageResource(R.drawable.retailernormal)
            trainer_image.setImageResource(R.drawable.trainergreen)

            Toast.makeText(context, "Available Soon", Toast.LENGTH_LONG).show()

        }




        return root
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun imageSliderView() {


        imagesliderpager!!.adapter =
            ShopSlideImage_Adapter(activity!!, imageModelArrayList!!)

        shopcircleindicator.setViewPager(imagesliderpager)

        val density = resources.displayMetrics.density

        //Set circle indicator radius
        shopcircleindicator.radius = 4 * density

        NUM_PAGES = imageModelArrayList!!.size

        // Auto start of viewpager
        val handler = Handler()
        val Update = Runnable {
            if (currentPage == NUM_PAGES) {
                currentPage = 0
            }
            imagesliderpager!!.setCurrentItem(currentPage++, true)
        }
        val swipeTimer = Timer()
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(Update)
            }
        }, 3000, 3000)

        // Pager listener over indicator
        shopcircleindicator.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageSelected(position: Int) {
                currentPage = position

            }

            override fun onPageScrolled(pos: Int, arg1: Float, arg2: Int) {

            }

            override fun onPageScrollStateChanged(pos: Int) {

            }
        })

    }

    //images auto slide
    private fun populateList(): ArrayList<ImageModel> {

        val list = ArrayList<ImageModel>()

        for (i in 0..2) {
            val imageModel = ImageModel()
            imageModel.setImage_drawables(myImageList[i])
            list.add(imageModel)
        }

        return list
    }


    //images auto slide


}
