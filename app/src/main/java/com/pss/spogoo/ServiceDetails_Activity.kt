package com.pss.spogoo

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.parsaniahardik.kotlin_image_slider.DynamicSlideImage_Adapter
import com.google.android.gms.tasks.Task
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.pss.spogoo.adapter.ApprealsRelated_Adapter
import com.pss.spogoo.api.APIClient
import com.pss.spogoo.api.Api
import com.pss.spogoo.models.*
import com.pss.spogoo.util.NetWorkConection
import com.viewpagerindicator.CirclePageIndicator
import kotlinx.android.synthetic.main.product_details_screen.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.util.*

class ServiceDetails_Activity : AppCompatActivity() {

    lateinit var listOfindetailsrepair: RepairsRecentInDetailsResponse
    lateinit var relatedadapter: ApprealsRelated_Adapter
    lateinit var gripdetailsadapter: AddGripProductsDetailsResponse
    lateinit var procutindetail_image: ViewPager
    lateinit var procutindetail_newimage: TextView
    lateinit var procutindetail_title: TextView
    lateinit var procutindetail_subtitle: TextView
    lateinit var procutindetail_offerprice: TextView
    lateinit var procutindetail_mainprice: TextView
    lateinit var procutindetail_discount: TextView
    lateinit var procutindetail_size: TextView
    lateinit var procutindetail_gauge: TextView
    lateinit var cartlayout: FrameLayout
    lateinit var procutindetail_material: TextView
    lateinit var viewpagerlinear: LinearLayout
    lateinit var procutindetail_imageview: ImageView
    lateinit var procutindetail_grade: TextView
    lateinit var procutindetail_package: TextView
    lateinit var procutindetail_location: TextView
    lateinit var share: TextView
    lateinit var colour1: TextView
    lateinit var colour2: TextView
    lateinit var colour3: TextView
    lateinit var procutindetail_brand: TextView
    lateinit var procutindetail_qty: TextView
    lateinit var tensions_text: TextView
    lateinit var racketname_edit: EditText
    lateinit var procutindetail_description: TextView
    lateinit var procutindetail_tension: Spinner
    lateinit var procutindetail_sellerlocation: TextView
    lateinit var procutindetail_addgrip: TextView
    lateinit var slectbutton: TextView
    lateinit var backimage_product: ImageView
    lateinit var colors: TextView
    lateinit var addcart_btn: Button
    lateinit var sharedPreferences: SharedPreferences
    lateinit var progressBarproductdetails: ProgressBar
    lateinit var productcircleindicator: CirclePageIndicator
    var listnames: MutableList<String> = mutableListOf<String>()
    lateinit var item: CartItem
    lateinit var cart_badge: TextView
    var repairdetails: Int = 0

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.service_details_screen)
//
        addcart_btn = findViewById<Button>(R.id.addcart_btn) as Button
        tensions_text = findViewById<TextView>(R.id.tensions_text) as TextView
        share = findViewById<TextView>(R.id.share) as TextView
        backimage_product = toolbar.findViewById<ImageView>(R.id.backimage_product) as ImageView
        procutindetail_addgrip = findViewById<TextView>(R.id.procutindetail_addgrip) as TextView
        colour1 = findViewById<TextView>(R.id.colour1) as TextView
        colour2 = findViewById<TextView>(R.id.colour2) as TextView
        colour3 = findViewById<TextView>(R.id.colour3) as TextView
        racketname_edit = findViewById<EditText>(R.id.racketname_edit) as EditText
        procutindetail_imageview =
            findViewById<ImageView>(R.id.procutindetail_imageview) as ImageView
        viewpagerlinear = findViewById<LinearLayout>(R.id.viewpagerlinear) as LinearLayout
        progressBarproductdetails =
            findViewById<ProgressBar>(R.id.progressBarproductdetails) as ProgressBar
        procutindetail_image = findViewById<ViewPager>(R.id.procutindetail_image)
        cartlayout = findViewById<FrameLayout>(R.id.cartlayout)

        productcircleindicator = findViewById<CirclePageIndicator>(R.id.productcircleindicator)
        procutindetail_newimage = findViewById<TextView>(R.id.procutindetail_newimage)
        procutindetail_title = findViewById<TextView>(R.id.procutindetail_title)
        procutindetail_subtitle = findViewById<TextView>(R.id.procutindetail_subtitle)
        procutindetail_offerprice = findViewById<TextView>(R.id.procutindetail_offerprice)
        procutindetail_discount = findViewById<TextView>(R.id.procutindetail_discount)
        procutindetail_mainprice = findViewById<TextView>(R.id.procutindetail_mainprice)
        procutindetail_size = findViewById<TextView>(R.id.procutindetail_size)
        procutindetail_gauge = findViewById<TextView>(R.id.procutindetail_gauge)
        procutindetail_material = findViewById<TextView>(R.id.procutindetail_material)
        procutindetail_grade = findViewById<TextView>(R.id.procutindetail_grade)
        procutindetail_package = findViewById<TextView>(R.id.procutindetail_package)
        procutindetail_location = findViewById<TextView>(R.id.procutindetail_location)
        procutindetail_brand = findViewById<TextView>(R.id.procutindetail_brand)
        procutindetail_qty = findViewById<TextView>(R.id.procutindetail_qty)
        procutindetail_description = findViewById<TextView>(R.id.procutindetail_description)
        procutindetail_tension = findViewById<Spinner>(R.id.procutindetail_tension)
        procutindetail_sellerlocation = findViewById<TextView>(R.id.procutindetail_sellerlocation)

//        progressBarproductdetails.visibility = View.VISIBLE
        sharedPreferences =
            getSharedPreferences(
                "loginprefs",
                Context.MODE_PRIVATE
            )

        repairdetails = sharedPreferences.getInt("repair_indetails", 0)

        cart_badge = toolbar.findViewById(R.id.cart_badge)

        if (ShoppingCart.getShoppingCartSize().toString().equals("0")) {
            cart_badge.visibility = View.GONE
        } else {
            cart_badge.visibility = View.VISIBLE

            cart_badge.text = ShoppingCart.getShoppingCartSize().toString()

        }

        cartlayout.setOnClickListener {
            startActivity(Intent(this, Cart_Screen::class.java))
            finish()
        }
        backimage_product.setOnClickListener {
            startActivity(Intent(this, RepairsProdctList_Screen::class.java))
            finish()
        }

        share.setOnClickListener {

            val sharebody=listOfindetailsrepair.product_name+"\n"+listOfindetailsrepair.product_image

            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, sharebody)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)

        }
        addcart_btn.setOnClickListener {
//            Observable.create(ObservableOnSubscribe<MutableList<CartItem>> {


            ShoppingCart.addItem(item)
            //notify users

            Toast.makeText(this, "Items Added Sucessfully", Toast.LENGTH_LONG)
                .show()


            ShoppingCart.getCart()
            startActivity(Intent(this, Cart_Screen::class.java))
            finish()

//            }).subscribe { cart ->
//
//                var quantity = 0
//
//                cart.forEach { cartItem ->
//
//                    quantity += cartItem.quantity
//                }
//
//
//                Toast.makeText(this, "Cart size $quantity", Toast.LENGTH_SHORT).show()
//
//
//            }

        }

        if (repairdetails == 11) {

            getRecentProductInDetailslist()
        } else if (repairdetails == 12) {
            getRecentProductsDetailslist()
        } else if (repairdetails == 13) {
            getgripDetailslist()
        }

    }

    private fun getgripDetailslist() {

        if (NetWorkConection.isNEtworkConnected(this)) {

            //Set the Adapter to the RecyclerView//


            var apiServices = APIClient.client.create(Api::class.java)
            val repair_grip_id = sharedPreferences.getString("repair_grip_id", "")

            Log.e("repair_id", repair_grip_id)
            val call = apiServices.getgripproductsdetailslist(repair_grip_id.toString())

            call.enqueue(object : Callback<AddGripProdcutsDetailsListResponse> {
                override fun onResponse(
                    call: Call<AddGripProdcutsDetailsListResponse>,
                    response: Response<AddGripProdcutsDetailsListResponse>
                ) {

                    progressBarproductdetails.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//

                        gripdetailsadapter = response.body()?.response!!

//                        var recentlist = listOfindetailsrepair.map {

                        try {
                            procutindetail_title.text = gripdetailsadapter.product_name
                            colour1.text = gripdetailsadapter.product_name
                            procutindetail_description.text = gripdetailsadapter.description
//                        procutindetail_gauge.text = gripdetailsadapter.gr
//                            procutindetail_brand.text = gripdetailsadapter.color
                            procutindetail_material.text = gripdetailsadapter.material_info
                            procutindetail_mainprice.text =
                                "\u20B9 " + gripdetailsadapter.regular_price.toString()


                            procutindetail_addgrip.setOnClickListener {
                                var rocketname_Stng = racketname_edit.text.toString()
                                if (rocketname_Stng.isEmpty()) {
                                    racketname_edit.error = "Please Enter Racket Name"
                                } else {

                                    var editor = sharedPreferences.edit()
                                    editor.putString(
                                        "repaircategoryid",
                                        gripdetailsadapter.repair_categories_id.toString()
                                    )
                                    editor.commit()
                                    startActivity(
                                        Intent(
                                            this@ServiceDetails_Activity,
                                            AddGripList_Activity::class.java
                                        )
                                    )
                                }

                            }
                        } catch (e: NullPointerException) {
                            e.printStackTrace()
                        }
                        try {


                            item = CartItem(
                                gripdetailsadapter.repair_categories_id,
                                gripdetailsadapter.product_image,
                                gripdetailsadapter.product_image,
                                gripdetailsadapter.regular_price.toString(),
                                gripdetailsadapter.special_price.toString(),
                                gripdetailsadapter.product_model,
                                gripdetailsadapter.special_price.toString(),
                                "1",
                                0
                            )

                        } catch (e: NotImplementedError) {
                            e.printStackTrace()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
//                            if (it.size.isEmpty()) {
//                                procutindetail_size.visibility = View.GONE
//                            } else {
//                                procutindetail_size.text = it.size.toString()
//
//                            }
                        try {
                            var offerprice = gripdetailsadapter.special_price

                            if (offerprice == null || offerprice.equals("") || (offerprice.equals("0"))) {
                                procutindetail_offerprice.visibility = View.GONE
                                procutindetail_discount.visibility = View.GONE
                                procutindetail_newimage.visibility = View.GONE
                            } else {
                                procutindetail_offerprice.text = "\u20B9 " + offerprice

                                var regularprice = gripdetailsadapter.regular_price.toString()
                                var saledprice = gripdetailsadapter.special_price.toString()
                                var percentage =
                                    ((regularprice.toInt()
                                        .minus(saledprice.toInt())) / regularprice.toDouble() * 100)
                                val discountformat = DecimalFormat("##")


                                procutindetail_discount.text =
                                    discountformat.format(percentage) + "%"
                                Log.e("percentage", "" + percentage)
                            }
                        } catch (e: NullPointerException) {
                            e.printStackTrace()
                        }

                        var product_image = gripdetailsadapter.product_image

                        var sliderarray = gripdetailsadapter.slider_images
                        if (sliderarray.equals("") || (sliderarray.isEmpty()) || (sliderarray == null)) {
                            Glide.with(this@ServiceDetails_Activity)
                                .load(gripdetailsadapter.product_image)
                                .apply(RequestOptions().centerCrop())
                                .error(R.drawable.logo)
                                .into(procutindetail_imageview)
                            viewpagerlinear.visibility = View.GONE
                            procutindetail_imageview.visibility = View.VISIBLE
                        } else {
                            viewpagerlinear.visibility = View.VISIBLE
                            procutindetail_imageview.visibility = View.GONE


                            procutindetail_image.adapter =
                                DynamicSlideImage_Adapter(
                                    this@ServiceDetails_Activity,
                                    sliderarray as ArrayList<String>
                                )
                            productcircleindicator.setViewPager(procutindetail_image)

                            val density = resources.displayMetrics.density

                            //Set circle indicator radius
                            productcircleindicator.radius = 4 * density

                            NUM_PAGES = listnames.size

                            // Auto start of viewpager
                            val handler = Handler()
                            val Update = Runnable {
                                if (currentPage == NUM_PAGES) {
                                    currentPage = 0
                                }
                                procutindetail_image.setCurrentItem(currentPage++, true)
                            }
                            val swipeTimer = Timer()
                            swipeTimer.schedule(object : TimerTask() {
                                override fun run() {
                                    handler.post(Update)
                                }
                            }, 3000, 3000)

                            // Pager listener over indicator
                            productcircleindicator.setOnPageChangeListener(object :
                                ViewPager.OnPageChangeListener {

                                override fun onPageSelected(position: Int) {
                                    currentPage = position

                                }

                                override fun onPageScrolled(pos: Int, arg1: Float, arg2: Int) {

                                }

                                override fun onPageScrollStateChanged(pos: Int) {

                                }
                            })
                        }

//                        }

                    }
                }

                override fun onFailure(
                    call: Call<AddGripProdcutsDetailsListResponse>?,
                    t: Throwable?
                ) {
                    progressBarproductdetails.visibility = View.GONE
                    Log.e(ContentValues.TAG, t.toString())
                }
            })


        } else {

            Toast.makeText(this, "Please Check your internet", Toast.LENGTH_LONG).show()
        }
    }

    private fun getRecentProductsDetailslist() {


        if (NetWorkConection.isNEtworkConnected(this)) {

            //Set the Adapter to the RecyclerView//


            var apiServices = APIClient.client.create(Api::class.java)
            val repair_id = sharedPreferences.getString("repair_id", "")

            Log.e("repair_id", repair_id)
            val call = apiServices.getrepairsRecentindetailsProducts(repair_id.toString())

            call.enqueue(object : Callback<RepairsRecentIndetailsListResponse> {
                override fun onResponse(
                    call: Call<RepairsRecentIndetailsListResponse>,
                    response: Response<RepairsRecentIndetailsListResponse>
                ) {

                    progressBarproductdetails.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//

                        listOfindetailsrepair = response.body()?.response!!

//                        var recentlist = listOfindetailsrepair.map {
                        try {


                            procutindetail_title.text = listOfindetailsrepair.product_name

                            procutindetail_description.text = listOfindetailsrepair.description
                            procutindetail_gauge.text = listOfindetailsrepair.gauge
                            procutindetail_brand.text = listOfindetailsrepair.color
                            Log.e("color in dea", "" + listOfindetailsrepair.color)

                            procutindetail_material.text = listOfindetailsrepair.material_info
                            procutindetail_mainprice.text =
                                "\u20B9  " + listOfindetailsrepair.regular_price.toString()
//                            colors.text = listOfindetailsrepair.color

                            var tensionstrng = listOfindetailsrepair.tension

                            Log.e("tensionstrng", "" + tensionstrng)
                            if (tensionstrng.isEmpty() || (tensionstrng.equals(""))) {
                                procutindetail_tension.visibility = View.GONE
                                tensions_text.visibility = View.GONE
                            } else {
                                procutindetail_tension.visibility = View.VISIBLE
                                tensions_text.visibility = View.VISIBLE
                                val arrayAdapter =
                                    ArrayAdapter<String>(
                                        this@ServiceDetails_Activity,
                                        R.layout.spinner_adapter,
                                        tensionstrng
                                    )

                                procutindetail_tension.adapter = arrayAdapter
                            }


                            procutindetail_addgrip.setOnClickListener {
                                var racketname_stng = racketname_edit.text.toString()
                                if (racketname_stng.isEmpty()) {
                                    racketname_edit.error = "Please Enter Racket Name"
                                } else {


                                    var editor = sharedPreferences.edit()
                                    editor.putString(
                                        "repaircategoryid",
                                        listOfindetailsrepair.repair_categories_id.toString()
                                    )
                                    editor.commit()
                                    startActivity(
                                        Intent(
                                            this@ServiceDetails_Activity,
                                            AddGripList_Activity::class.java
                                        )
                                    )
                                }
                            }
                        } catch (e: NullPointerException) {
                            e.printStackTrace()
                        }
                        try {


                            item = CartItem(
                                listOfindetailsrepair.repair_categories_id,
                                listOfindetailsrepair.product_name,
                                listOfindetailsrepair.product_image,
                                listOfindetailsrepair.regular_price,
                                listOfindetailsrepair.special_price,
                                listOfindetailsrepair.size,
                                listOfindetailsrepair.size,
                                "1",
                                0
                            )

                        } catch (e: NotImplementedError) {
                            e.printStackTrace()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
//                            if (it.size.isEmpty()) {
//                                procutindetail_size.visibility = View.GONE
//                            } else {
//                                procutindetail_size.text = it.size.toString()
//
//                            }

                        try {
                            var offerprice = listOfindetailsrepair.special_price

                            if (offerprice == null || offerprice.equals("") || (offerprice.equals("0"))) {
                                procutindetail_offerprice.visibility = View.GONE
                                procutindetail_discount.visibility = View.GONE
                                procutindetail_newimage.visibility = View.GONE
                            } else {
                                procutindetail_offerprice.text = "\u20B9 " + offerprice

                                var regularprice = listOfindetailsrepair.regular_price.toString()
                                var saledprice = listOfindetailsrepair.special_price.toString()
                                var percentage =
                                    ((regularprice.toInt()
                                        .minus(saledprice.toInt())) / regularprice.toDouble() * 100)
                                val discountformat = DecimalFormat("##")


                                procutindetail_discount.text =
                                    discountformat.format(percentage) + "%"
                                Log.e("percentage", "" + percentage)
                            }


                        } catch (e: NullPointerException) {
                            e.printStackTrace()
                        }
                        var product_image = listOfindetailsrepair.product_image

                        var sliderarray = listOfindetailsrepair.slider_images
                        if (sliderarray.equals("") || (sliderarray.isEmpty()) || (sliderarray == null)) {
                            Glide.with(this@ServiceDetails_Activity)
                                .load(listOfindetailsrepair.product_image)
                                .apply(RequestOptions().centerCrop())
                                .error(R.drawable.logo)
                                .into(procutindetail_imageview)
                            viewpagerlinear.visibility = View.GONE
                            procutindetail_imageview.visibility = View.VISIBLE
                        } else {
                            viewpagerlinear.visibility = View.VISIBLE
                            procutindetail_imageview.visibility = View.GONE


                            procutindetail_image.adapter =
                                DynamicSlideImage_Adapter(
                                    this@ServiceDetails_Activity,
                                    sliderarray as ArrayList<String>
                                )
                            productcircleindicator.setViewPager(procutindetail_image)

                            val density = resources.displayMetrics.density

                            //Set circle indicator radius
                            productcircleindicator.radius = 4 * density

                            NUM_PAGES = listnames.size

                            // Auto start of viewpager
                            val handler = Handler()
                            val Update = Runnable {
                                if (currentPage == NUM_PAGES) {
                                    currentPage = 0
                                }
                                procutindetail_image.setCurrentItem(currentPage++, true)
                            }
                            val swipeTimer = Timer()
                            swipeTimer.schedule(object : TimerTask() {
                                override fun run() {
                                    handler.post(Update)
                                }
                            }, 3000, 3000)

                            // Pager listener over indicator
                            productcircleindicator.setOnPageChangeListener(object :
                                ViewPager.OnPageChangeListener {

                                override fun onPageSelected(position: Int) {
                                    currentPage = position

                                }

                                override fun onPageScrolled(pos: Int, arg1: Float, arg2: Int) {

                                }

                                override fun onPageScrollStateChanged(pos: Int) {

                                }
                            })
                        }

//                        }

                    }
                }

                override fun onFailure(
                    call: Call<RepairsRecentIndetailsListResponse>?,
                    t: Throwable?
                ) {
                    progressBarproductdetails.visibility = View.GONE
                    Log.e(ContentValues.TAG, t.toString())
                }
            })


        } else {

            Toast.makeText(this, "Please Check your internet", Toast.LENGTH_LONG).show()
        }
    }

    private fun getRecentProductInDetailslist() {

        if (NetWorkConection.isNEtworkConnected(this)) {

            //Set the Adapter to the RecyclerView//


            var apiServices = APIClient.client.create(Api::class.java)
            val recentrepair_id = sharedPreferences.getString("recentrepair_id", "")
            Log.e("recentrepair_id", recentrepair_id)
            val call = apiServices.getrepairsRecentindetailsProducts(recentrepair_id.toString())

            call.enqueue(object : Callback<RepairsRecentIndetailsListResponse> {
                override fun onResponse(
                    call: Call<RepairsRecentIndetailsListResponse>,
                    response: Response<RepairsRecentIndetailsListResponse>
                ) {

                    progressBarproductdetails.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//

                        listOfindetailsrepair = response.body()?.response!!

//                        var recentlist = listOfindetailsrepair.map {

                        try {
                            procutindetail_title.text = listOfindetailsrepair.product_name
                            procutindetail_description.text = listOfindetailsrepair.description
                            procutindetail_gauge.text = listOfindetailsrepair.gauge
                            procutindetail_brand.text = listOfindetailsrepair.color
                            Log.e("color", "" + listOfindetailsrepair.color)
                            procutindetail_material.text = listOfindetailsrepair.material_info
                            "\u20B9  " + listOfindetailsrepair.regular_price.toString()

                            var tensionstrng = listOfindetailsrepair.tension


                            Log.e("tensionstrng", "" + tensionstrng)
                            if (tensionstrng.isEmpty() || (tensionstrng.equals(""))) {
                                procutindetail_tension.visibility = View.GONE
                                tensions_text.visibility = View.GONE
                            } else {
                                procutindetail_tension.visibility = View.VISIBLE
                                tensions_text.visibility = View.VISIBLE
                                val arrayAdapter =
                                    ArrayAdapter<String>(
                                        this@ServiceDetails_Activity,
                                        R.layout.spinner_adapter,
                                        tensionstrng
                                    )

                                procutindetail_tension.adapter = arrayAdapter
                            }


                            //cart items
                            try {


                                item = CartItem(
                                    listOfindetailsrepair.repair_categories_id,
                                    listOfindetailsrepair.product_name,
                                    listOfindetailsrepair.product_image,
                                    listOfindetailsrepair.regular_price.toString(),
                                    listOfindetailsrepair.special_price.toString(),
                                    listOfindetailsrepair.size,
                                    listOfindetailsrepair.special_price.toString(),
                                    "1",
                                    0
                                )

                            } catch (e: NotImplementedError) {
                                e.printStackTrace()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
//                            if (it.size.isEmpty()) {
//                                procutindetail_size.visibility = View.GONE
//                            } else {
//                                procutindetail_size.text = it.size.toString()
//
//                            }
                            var offerprice = listOfindetailsrepair.special_price

                            if (offerprice == null || offerprice.equals("") || (offerprice.equals("0"))) {
                                procutindetail_offerprice.visibility = View.GONE
                                procutindetail_discount.visibility = View.GONE
                                procutindetail_newimage.visibility = View.GONE
                            } else {
                                procutindetail_offerprice.text = "\u20B9 " + offerprice

                                var regularprice = listOfindetailsrepair.regular_price.toString()
                                var saledprice = listOfindetailsrepair.special_price.toString()
                                var percentage =
                                    ((regularprice.toInt()
                                        .minus(saledprice.toInt())) / regularprice.toDouble() * 100)
                                val discountformat = DecimalFormat("##")


                                procutindetail_discount.text =
                                    discountformat.format(percentage) + "%"
                                Log.e("percentage", "" + percentage)
                            }

//                        prod.text = listOfapparels.quantity.toString()
                            procutindetail_brand.text = listOfindetailsrepair.color.toString()
                            Log.e("color", "" + listOfindetailsrepair.color)

//                        procutindetail_sellerinfo.text =
//                            listOfapparels.additional_info.toString()

                        } catch (e: NullPointerException) {
                            e.printStackTrace()
                        }

                        var product_image = listOfindetailsrepair.product_image
//                        colors.text = listOfindetailsrepair.color
//                        procutindetail_sellerinfo.text = listOfindetailsrepair.tension

                        var sliderarray = listOfindetailsrepair.slider_images
                        if (sliderarray.equals("") || (sliderarray.isEmpty()) || (sliderarray == null)) {
                            Glide.with(this@ServiceDetails_Activity)
                                .load(listOfindetailsrepair.product_image)
                                .apply(RequestOptions().centerCrop())
                                .error(R.drawable.logo)
                                .into(procutindetail_imageview)
                            viewpagerlinear.visibility = View.GONE
                            procutindetail_imageview.visibility = View.VISIBLE
                        } else {
                            viewpagerlinear.visibility = View.VISIBLE
                            procutindetail_imageview.visibility = View.GONE


                            procutindetail_image.adapter =
                                DynamicSlideImage_Adapter(
                                    this@ServiceDetails_Activity,
                                    sliderarray as ArrayList<String>
                                )
                            productcircleindicator.setViewPager(procutindetail_image)

                            val density = resources.displayMetrics.density

                            //Set circle indicator radius
                            productcircleindicator.radius = 4 * density

                            NUM_PAGES = listnames.size

                            // Auto start of viewpager
                            val handler = Handler()
                            val Update = Runnable {
                                if (currentPage == NUM_PAGES) {
                                    currentPage = 0
                                }
                                procutindetail_image.setCurrentItem(currentPage++, true)
                            }
                            val swipeTimer = Timer()
                            swipeTimer.schedule(object : TimerTask() {
                                override fun run() {
                                    handler.post(Update)
                                }
                            }, 3000, 3000)

                            // Pager listener over indicator
                            productcircleindicator.setOnPageChangeListener(object :
                                ViewPager.OnPageChangeListener {

                                override fun onPageSelected(position: Int) {
                                    currentPage = position

                                }

                                override fun onPageScrolled(pos: Int, arg1: Float, arg2: Int) {

                                }

                                override fun onPageScrollStateChanged(pos: Int) {

                                }
                            })
                        }

//                        }

                    }
                }

                override fun onFailure(
                    call: Call<RepairsRecentIndetailsListResponse>?,
                    t: Throwable?
                ) {
                    progressBarproductdetails.visibility = View.GONE
                    Log.e(ContentValues.TAG, t!!.message)
                }
            })


        } else {

            Toast.makeText(this, "Please Check your internet", Toast.LENGTH_LONG).show()
        }
    }


    companion object {

        private var currentPage = 0
        private var NUM_PAGES = 0
    }

}



