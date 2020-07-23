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
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.parsaniahardik.kotlin_image_slider.SlidingImage_Adapter
import com.pss.spogoo.adapter.AllProducts_Adapter
import com.pss.spogoo.api.APIClient
import com.pss.spogoo.api.Api
import com.pss.spogoo.models.ImageModel
import com.pss.spogoo.models.ShoppingCart
import com.pss.spogoo.models.SubProductListResponse
import com.pss.spogoo.models.SubProductResponse
import com.pss.spogoo.util.NetWorkConection
import kotlinx.android.synthetic.main.product_details_screen.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class AllProductsList_Activity : AppCompatActivity() {

    private var imagesliderpager: ViewPager? = null
    private var currentPage = 0
    private var NUM_PAGES = 0

    lateinit var subuserslist: List<SubProductResponse>
    lateinit var repairstrngAdapter: AllProducts_Adapter

    private var imageModelArrayList: java.util.ArrayList<ImageModel>? = null

    private val myImageList = intArrayOf(
        R.drawable.sli1,
        R.drawable.sli3,
        R.drawable.sli5
    )
    lateinit var allproductsstringservce: GridView
    lateinit var progressBarrepair: ProgressBar
    lateinit var sharedPreferences: SharedPreferences
    lateinit var productname: String
    lateinit var zoomimage: String
    lateinit var productcat_id: String
    lateinit var productname_category: TextView
    lateinit var productbackimage: ImageView
    lateinit var zoomimage_: ImageView
    lateinit var cartlayout: FrameLayout
    lateinit var cart_badge: TextView
    lateinit var productstext: TextView

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.allprodcutslist_screen)


        progressBarrepair = findViewById<ProgressBar>(R.id.progressBarrepair) as ProgressBar
        allproductsstringservce = findViewById<GridView>(R.id.allproductsstringservce) as GridView
        productname_category = findViewById<TextView>(R.id.productname_category) as TextView
        productstext = findViewById<TextView>(R.id.productstext) as TextView
        productbackimage = toolbar.findViewById<ImageView>(R.id.productbackimage) as ImageView
        zoomimage_ = findViewById<ImageView>(R.id.zoomimage) as ImageView
        cartlayout = toolbar.findViewById<FrameLayout>(R.id.cartlayout) as FrameLayout




        sharedPreferences =
            getSharedPreferences(
                "loginprefs",
                Context.MODE_PRIVATE
            )
        zoomimage = sharedPreferences.getString("zoomimage", "").toString()

        Glide.with(this)
            .load(zoomimage)
            .apply(RequestOptions().centerCrop())
            .error(R.drawable.logo)
            .into(zoomimage_)

        val zoomin: Animation = AnimationUtils.loadAnimation(this, R.anim.zoomin)
        val zoomout: Animation = AnimationUtils.loadAnimation(this, R.anim.zoomout)
        zoomimage_.animation = zoomin
        zoomimage_.animation = zoomout
        cartlayout.setOnClickListener {
            startActivity(Intent(this, Cart_Screen::class.java))
            finish()
        }


        cart_badge = toolbar.findViewById(R.id.cart_badge)

        Log.e("cartsize in shop", "" + ShoppingCart.getShoppingCartSize())
        if (ShoppingCart.getShoppingCartSize().toString().equals("0")) {
            cart_badge.visibility = View.GONE
        } else {
            cart_badge.visibility = View.VISIBLE

            cart_badge.text = ShoppingCart.getShoppingCartSize().toString()

        }


        productname = sharedPreferences.getString("productname", "")!!
        productcat_id = sharedPreferences.getString("productcat_id", "")!!
        productname_category.text = productname
        productstext.text = productname
        imagesliderpager = findViewById<ViewPager>(R.id.imagesliderpager)
        //initializing the modelslist
        imageModelArrayList = java.util.ArrayList()
        imageModelArrayList = populateList()

        imagesliderpager!!.adapter =
            SlidingImage_Adapter(
                this,
                imageModelArrayList!!
            )

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

        productbackimage.setOnClickListener {

            startActivity(Intent(this, Shop_Activity::class.java))
            finish()
        }


        getRepairsStringServicesList()
    }

    //images auto slide
    private fun populateList(): java.util.ArrayList<ImageModel> {

        val list = java.util.ArrayList<ImageModel>()

        for (i in 0..2) {
            val imageModel = ImageModel()
            imageModel.setImage_drawables(myImageList[i])
            list.add(imageModel)
        }

        return list
    }


    private fun getRepairsStringServicesList() {

        if (NetWorkConection.isNEtworkConnected(this)) {

            //Set the Adapter to the RecyclerView//


            var apiServices = APIClient.client.create(Api::class.java)


            val call = apiServices.getusersubproductList(productcat_id.toString())

            call.enqueue(object : Callback<SubProductListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<SubProductListResponse>,
                    response: Response<SubProductListResponse>
                ) {

                    progressBarrepair.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//

                        subuserslist = response.body()?.response!!

                        repairstrngAdapter = AllProducts_Adapter(
                            this@AllProductsList_Activity,
                            subuserslist as ArrayList<SubProductResponse>
                        )

                        allproductsstringservce.adapter = repairstrngAdapter
                        repairstrngAdapter.notifyDataSetChanged()


                        allproductsstringservce.setOnItemClickListener { adapterView, parent, position, l ->

                            // Your logic


                            val editor = sharedPreferences.edit()
                            val subcatid =
                                repairstrngAdapter.arrayListImage.get(position).sub_categories_id.toString()
                            val subcatname =
                                repairstrngAdapter.arrayListImage.get(position).sub_category_name.toString()
                            editor.putString("subcategories_id", subcatid)
                            editor.putString("subcategory_name", subcatname)
                            editor.putInt("indetailswitch", 6)
                            editor.commit()
                            Log.e("subcatid", subcatid)
                            Log.e("subcatname", subcatname)
//
                            val intent =
                                Intent(
                                    this@AllProductsList_Activity,
                                    UserProdctList_Screen::class.java
                                )
                            startActivity(intent)


                        }

                    }
                }

                override fun onFailure(call: Call<SubProductListResponse>?, t: Throwable?) {
                    progressBarrepair.visibility = View.GONE
                    Log.e(ContentValues.TAG, t.toString())
                }
            })


        } else {

            Toast.makeText(this, "Please Check your internet", Toast.LENGTH_LONG)
                .show()
        }


    }


}
