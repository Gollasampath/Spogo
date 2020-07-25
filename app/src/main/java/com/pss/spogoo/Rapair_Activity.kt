package com.pss.spogoo

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.parsaniahardik.kotlin_image_slider.ShopSlideImage_Adapter
import com.pss.spogoo.adapter.*
import com.pss.spogoo.api.APIClient
import com.pss.spogoo.api.Api
import com.pss.spogoo.models.*
import com.pss.spogoo.util.NetWorkConection
import com.pss.spogoo.util.VerticalSpacingItemDecorator
import com.viewpagerindicator.CirclePageIndicator
import kotlinx.android.synthetic.main.shop_screen.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class Rapair_Activity : AppCompatActivity() {

    private var imagesliderpager: ViewPager? = null
    private var currentPage = 0
    private var NUM_PAGES = 0

    lateinit var listOfcategory: List<RepairsCategoriesResponse>
    lateinit var listOfcycling: List<RepairsCyclingResponse>
    lateinit var listOftt: List<RepairsTtResponse>
    lateinit var listOfcricket: List<RepairsCricketResponse>
    lateinit var listOfgym: List<RepairsGymResponse>
    lateinit var listOfrecent: List<RepairsRecentResponse>

    lateinit var categoryAdapter: RepairsAllCategory_Adapter
    lateinit var repairrecentAdapter: RepairRecent_Adapter
    lateinit var repairstrngAdapter: RepairStringService_Adapter
    lateinit var cyclingAdapter: RepairCyclingService_Adapter
    lateinit var TtAdapter: RepairTtService_Adapter
    lateinit var CricketAdapter: RepairCricketService_Adapter
    lateinit var GymAdapter: RepairGymService_Adapter

    private var imageModelArrayList: java.util.ArrayList<ImageModel>? = null

    private val myImageList = intArrayOf(
        R.drawable.sli4,
        R.drawable.sli1,
        R.drawable.sli5
    )
    lateinit var gvspecificrepair: RecyclerView
    lateinit var gvrecentrepair: RecyclerView
    lateinit var gvstrngservices: GridView
    lateinit var cyclingservices: GridView
    lateinit var ttservices: GridView
    lateinit var cricketservices: GridView
    lateinit var gymservices: GridView
    lateinit var backshop_image: ImageView
    lateinit var progressBarrepair: ProgressBar
    lateinit var sharedPreferences: SharedPreferences
    lateinit var shopcircleindicator: CirclePageIndicator

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.repair_screen)


        gvspecificrepair = findViewById<RecyclerView>(R.id.gvspecificrepair) as RecyclerView
        progressBarrepair = findViewById<ProgressBar>(R.id.progressBarrepair) as ProgressBar
        gvrecentrepair = findViewById<RecyclerView>(R.id.gvrecent) as RecyclerView
        gvstrngservices = findViewById<GridView>(R.id.repairstringservce) as GridView
        cyclingservices = findViewById<GridView>(R.id.repaircyclingservices) as GridView
        ttservices = findViewById<GridView>(R.id.repairttservices) as GridView
        cricketservices = findViewById<GridView>(R.id.cricketservices) as GridView
        gymservices = findViewById<GridView>(R.id.gymservices) as GridView


        backshop_image = toolbar.findViewById<ImageView>(R.id.backshop_image) as ImageView

        shopcircleindicator = findViewById<CirclePageIndicator>(R.id.repaircircleindicator)


        sharedPreferences =
            getSharedPreferences(
                "loginprefs",
                Context.MODE_PRIVATE
            )
        val exploreall_text = findViewById<TextView>(R.id.exploreall_text) as TextView

        imagesliderpager = findViewById<ViewPager>(R.id.imagesliderpager)
        //initializing the modelslist
        imageModelArrayList = ArrayList()
        imageModelArrayList = populateList()

        imageSliderView()

        imagesliderpager!!.clipToPadding = false
        imagesliderpager!!.pageMargin = 10
        imagesliderpager!!.setPadding(40, 0, 40, 0)
        backshop_image.setOnClickListener {

            startActivity(Intent(this, Home_Screen::class.java))
            finish()
        }


        //adding a layoutmanager
        gvspecificrepair.layoutManager =
            LinearLayoutManager(this, LinearLayout.HORIZONTAL, false)


        //adding a layoutmanager
        gvrecentrepair.layoutManager =
            LinearLayoutManager(this, LinearLayout.HORIZONTAL, false)


        //string services


        exploreall_text.setOnClickListener {


            startActivity(Intent(this, RepiarByCategory_Activity::class.java))

        }

//        getRepairsCategoriesList()
//        getRepairRecentProducts()
        getRepairsStringServicesList()
        getcyclinglist()
        getTtServicelist()
        getCricketservicelist()
        getgymServicelist()
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

    @SuppressLint("UseRequireInsteadOfGet")
    private fun imageSliderView() {


        imagesliderpager!!.adapter =
            ShopSlideImage_Adapter(this, imageModelArrayList!!)

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
            imagesliderpager!!.setCurrentItem(
                currentPage++, true
            )
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

    companion object {

        private var currentPage = 0
        private var NUM_PAGES = 0
    }

    private fun getRepairsCategoriesList() {

        if (NetWorkConection.isNEtworkConnected(this)) {

            //Set the Adapter to the RecyclerView//


            var apiServices = APIClient.client.create(Api::class.java)


            val call = apiServices.getrepairscategory()

            call.enqueue(object : Callback<RepairsCategoryListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<RepairsCategoryListResponse>,
                    response: Response<RepairsCategoryListResponse>
                ) {

                    progressBarrepair.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//

                        listOfcategory = response.body()?.response!!

                        categoryAdapter =
                            RepairsAllCategory_Adapter(
                                this@Rapair_Activity,
                                listOfcategory as ArrayList<RepairsCategoriesResponse>
                            )
                        gvspecificrepair.adapter = categoryAdapter
                        categoryAdapter.notifyDataSetChanged()


                        val itemDecorator2 = VerticalSpacingItemDecorator(20)
                        gvspecificrepair.addItemDecoration(itemDecorator2)

                        gvspecificrepair.addOnItemClickListener(object :
                            OnItemClickListener {
                            override fun onItemClicked(position: Int, view: View) {
                                // Your logic

//                                val intent =
//                                    Intent(
//                                        this@Rapair_Activity,
//                                        RepiarByCategory_Activity::class.java
//                                    )

//                                startActivity(intent)
                                startActivity(
                                    Intent(
                                        this@Rapair_Activity,
                                        Shop_Activity::class.java
                                    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                                )
                                finish()
                            }
                        })

                    }
                }

                override fun onFailure(call: Call<RepairsCategoryListResponse>?, t: Throwable?) {
                    progressBarrepair.visibility = View.GONE
                    Log.e(ContentValues.TAG, t.toString())
                }
            })


        } else {

            Toast.makeText(this, "Please Check your internet", Toast.LENGTH_LONG)
                .show()
        }


    }

    private fun getRepairsStringServicesList() {

        if (NetWorkConection.isNEtworkConnected(this)) {

            //Set the Adapter to the RecyclerView//


            var apiServices = APIClient.client.create(Api::class.java)


            val call = apiServices.getrepairscategory()

            call.enqueue(object : Callback<RepairsCategoryListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<RepairsCategoryListResponse>,
                    response: Response<RepairsCategoryListResponse>
                ) {

                    progressBarrepair.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//

                        listOfcategory = response.body()?.response!!

                        repairstrngAdapter = RepairStringService_Adapter(
                            this@Rapair_Activity,
                            listOfcategory as ArrayList<RepairsCategoriesResponse>
                        )


                        gvstrngservices.adapter = repairstrngAdapter
                        repairstrngAdapter.notifyDataSetChanged()



                        gvstrngservices.setOnItemClickListener { adapterView, parent, position, l ->


                            startActivity(
                                Intent(
                                    this@Rapair_Activity,
                                    RepiarByCategory_Activity::class.java
                                ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            )
                            finish()

                        }

                    }
                }

                override fun onFailure(call: Call<RepairsCategoryListResponse>?, t: Throwable?) {
                    progressBarrepair.visibility = View.GONE
                    Log.e(ContentValues.TAG, t.toString())
                }
            })


        } else {

            Toast.makeText(this, "Please Check your internet", Toast.LENGTH_LONG)
                .show()
        }


    }

    private fun getcyclinglist() {

        if (NetWorkConection.isNEtworkConnected(this)) {

            //Set the Adapter to the RecyclerView//


            var apiServices = APIClient.client.create(Api::class.java)


            val call = apiServices.getcyclingsubcategorylist()

            call.enqueue(object : Callback<RepairsCyclingListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<RepairsCyclingListResponse>,
                    response: Response<RepairsCyclingListResponse>
                ) {

                    progressBarrepair.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//

                        listOfcycling = response.body()?.response!!

                        cyclingAdapter = RepairCyclingService_Adapter(
                            this@Rapair_Activity,
                            listOfcycling as ArrayList<RepairsCyclingResponse>
                        )


                        cyclingservices.adapter = cyclingAdapter
                        cyclingAdapter.notifyDataSetChanged()



                        cyclingservices.setOnItemClickListener { adapterView, parent, position, l ->

                            val subid =
                                cyclingAdapter.arrayListImage.get(position).repair_sub_categories_id
                            val editor = sharedPreferences.edit()
                            editor.putInt("repairlist", 1)
                            editor.putString("cyclesubid", subid.toString())
                            editor.commit()
//
                            startActivity(
                                Intent(
                                    this@Rapair_Activity,
                                    RepairsProdctList_Screen::class.java
                                ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            )
                            finish()

                        }

                    }
                }

                override fun onFailure(call: Call<RepairsCyclingListResponse>?, t: Throwable?) {
                    progressBarrepair.visibility = View.GONE
                    Log.e(ContentValues.TAG, t.toString())
                }
            })


        } else {

            Toast.makeText(this, "Please Check your internet", Toast.LENGTH_LONG)
                .show()
        }


    }

    private fun getTtServicelist() {

        if (NetWorkConection.isNEtworkConnected(this)) {

            //Set the Adapter to the RecyclerView//


            var apiServices = APIClient.client.create(Api::class.java)


            val call = apiServices.getttsubcategorylist()

            call.enqueue(object : Callback<RepairsTtListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<RepairsTtListResponse>,
                    response: Response<RepairsTtListResponse>
                ) {

                    progressBarrepair.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//

                        listOftt = response.body()?.response!!

                        TtAdapter = RepairTtService_Adapter(
                            this@Rapair_Activity,
                            listOftt as ArrayList<RepairsTtResponse>
                        )


                        ttservices.adapter = TtAdapter
                        TtAdapter.notifyDataSetChanged()



                        ttservices.setOnItemClickListener { adapterView, parent, position, l ->

                            val subid =
                                TtAdapter.arrayListImage.get(position).repair_sub_categories_id
                            val editor = sharedPreferences.edit()
                            editor.putInt("repairlist", 2)
                            editor.putString("ttsubid", subid.toString())
                            editor.commit()
//
                            startActivity(
                                Intent(
                                    this@Rapair_Activity,
                                    RepairsProdctList_Screen::class.java
                                ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            )
                            finish()

                        }

                    }
                }

                override fun onFailure(call: Call<RepairsTtListResponse>?, t: Throwable?) {
                    progressBarrepair.visibility = View.GONE
                    Log.e(ContentValues.TAG, t.toString())
                }
            })


        } else {

            Toast.makeText(this, "Please Check your internet", Toast.LENGTH_LONG)
                .show()
        }


    }

    private fun getgymServicelist() {

        if (NetWorkConection.isNEtworkConnected(this)) {

            //Set the Adapter to the RecyclerView//


            var apiServices = APIClient.client.create(Api::class.java)


            val call = apiServices.getgymsubcategorylist()

            call.enqueue(object : Callback<RepairsGymListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<RepairsGymListResponse>,
                    response: Response<RepairsGymListResponse>
                ) {

                    progressBarrepair.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//

                        listOfgym = response.body()?.response!!

                        GymAdapter = RepairGymService_Adapter(
                            this@Rapair_Activity,
                            listOfgym as ArrayList<RepairsGymResponse>
                        )


                        gymservices.adapter = GymAdapter
                        GymAdapter.notifyDataSetChanged()



                        gymservices.setOnItemClickListener { adapterView, parent, position, l ->

                            val subid =
                                GymAdapter.arrayListImage.get(position).repair_sub_categories_id
                            val editor = sharedPreferences.edit()
                            editor.putInt("repairlist", 4)
                            editor.putString("gymsubid", subid.toString())
                            editor.commit()
//
                            startActivity(
                                Intent(
                                    this@Rapair_Activity,
                                    RepairsProdctList_Screen::class.java
                                ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            )
                            finish()

                        }

                    }
                }

                override fun onFailure(call: Call<RepairsGymListResponse>?, t: Throwable?) {
                    progressBarrepair.visibility = View.GONE
                    Log.e(ContentValues.TAG, t.toString())
                }
            })


        } else {

            Toast.makeText(this, "Please Check your internet", Toast.LENGTH_LONG)
                .show()
        }


    }

    private fun getCricketservicelist() {

        if (NetWorkConection.isNEtworkConnected(this)) {

            //Set the Adapter to the RecyclerView//


            var apiServices = APIClient.client.create(Api::class.java)


            val call = apiServices.getcricketsubcategorylist()

            call.enqueue(object : Callback<RepairsCricketListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<RepairsCricketListResponse>,
                    response: Response<RepairsCricketListResponse>
                ) {

                    progressBarrepair.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//

                        listOfcricket = response.body()?.response!!

                        CricketAdapter = RepairCricketService_Adapter(
                            this@Rapair_Activity,
                            listOfcricket as ArrayList<RepairsCricketResponse>
                        )


                        cricketservices.adapter = CricketAdapter
                        CricketAdapter.notifyDataSetChanged()



                        cricketservices.setOnItemClickListener { adapterView, parent, position, l ->

                            val subid =
                                CricketAdapter.arrayListImage.get(position).repair_sub_categories_id
                            val editor = sharedPreferences.edit()
                            editor.putInt("repairlist", 3)
                            editor.putString("cricketsubid", subid.toString())
                            editor.commit()
//
                            startActivity(
                                Intent(
                                    this@Rapair_Activity,
                                    RepairsProdctList_Screen::class.java
                                ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            )
                            finish()

                        }

                    }
                }

                override fun onFailure(call: Call<RepairsCricketListResponse>?, t: Throwable?) {
                    progressBarrepair.visibility = View.GONE
                    Log.e(ContentValues.TAG, t.toString())
                }
            })


        } else {

            Toast.makeText(this, "Please Check your internet", Toast.LENGTH_LONG)
                .show()
        }


    }


    private fun getRepairRecentProducts() {

        if (NetWorkConection.isNEtworkConnected(this)) {

            //Set the Adapter to the RecyclerView//


            var apiServices = APIClient.client.create(Api::class.java)


            val call = apiServices.getrepairsRecentProducts()

            call.enqueue(object : Callback<RepairsRecentListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<RepairsRecentListResponse>,
                    response: Response<RepairsRecentListResponse>
                ) {

                    progressBarrepair.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//

                        listOfrecent = response.body()?.response!!

                        repairrecentAdapter =
                            RepairRecent_Adapter(
                                this@Rapair_Activity,
                                listOfrecent as ArrayList<RepairsRecentResponse>
                            )
                        gvrecentrepair.adapter = repairrecentAdapter
                        repairrecentAdapter.notifyDataSetChanged()


                        val itemDecorator2 = VerticalSpacingItemDecorator(20)
                        gvrecentrepair.addItemDecoration(itemDecorator2)

                        gvrecentrepair.addOnItemClickListener(object :
                            OnItemClickListener {
                            override fun onItemClicked(position: Int, view: View) {
                                // Your logic
                                val recentrepair_id =
                                    repairrecentAdapter.userList.get(position).repairs_id

                                val editor = sharedPreferences.edit()
                                editor.putString("recentrepair_id", recentrepair_id.toString())
                                editor.putInt("repair_indetails", 11)
                                editor.commit()
                                Log.e("recentrepair_id", recentrepair_id.toString())

//                                val intent = Intent(
//                                    this@Rapair_Activity,
//                                    ServiceDetails_Activity::class.java
//                                )
//                                startActivity(intent)
//
                                startActivity(
                                    Intent(
                                        this@Rapair_Activity,
                                        Shop_Activity::class.java
                                    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                )
                                finish()

                            }
                        })

                    }
                }

                override fun onFailure(call: Call<RepairsRecentListResponse>?, t: Throwable?) {
                    progressBarrepair.visibility = View.GONE
                    Log.e(ContentValues.TAG, t.toString())
                }
            })


        } else {

            Toast.makeText(this, "Please Check your internet", Toast.LENGTH_LONG).show()
        }


    }

    //on item click interface
    interface OnItemClickListener {
        fun onItemClicked(position: Int, view: View)
    }

    fun RecyclerView.addOnItemClickListener(onClickListener: OnItemClickListener) {
        this.addOnChildAttachStateChangeListener(object :
            RecyclerView.OnChildAttachStateChangeListener {
            override fun onChildViewDetachedFromWindow(view: View) {
            }


            override fun onChildViewAttachedToWindow(view: View) {
                view.setOnClickListener {
                    val holder = getChildViewHolder(view)
                    onClickListener.onItemClicked(holder.adapterPosition, view)
                }
            }
        })
    }

}
