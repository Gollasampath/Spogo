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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.parsaniahardik.kotlin_image_slider.DynamicSlideImage_Adapter
import com.pss.spogoo.adapter.*
import com.pss.spogoo.api.APIClient
import com.pss.spogoo.api.Api
import com.pss.spogoo.models.*
import com.pss.spogoo.util.NetWorkConection
import com.pss.spogoo.util.VerticalSpacingItemDecorator
import com.viewpagerindicator.CirclePageIndicator
import io.paperdb.Paper
import kotlinx.android.synthetic.main.product_details_screen.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.util.*

class ProductDetails_Activity : AppCompatActivity() {

    lateinit var listOfapparels: ApparealsDetsilsResponse
    lateinit var listOfelectronics: ElectronicsDetailsResponse
    lateinit var listOfhealth: HealthProductDetailsResponse
    lateinit var listOfaccessories: AccessoriesinDetailsResponse
    lateinit var listOfrecent: List<RecentinDetailsProdcutsResponse>
    lateinit var listOfindetailscat: List<RecentinDetailsProdcutsResponse>
    lateinit var listOfindetailsfitness: List<FitnessinDetailsResponse>
    lateinit var listOfindetailsbrands: List<BrandsDetailsResponse>
    lateinit var listOfrecentapparels: List<ApparelsProductResponse>
    lateinit var lostofrecentlectronics: List<ElectronicsProductResponse>
    lateinit var lostofrecenthealth: List<HealthProductsResponse>
    lateinit var lostofrecentaccesories: List<AccessoriesProductResponse>
    lateinit var lostofrecentproducts: List<RelatedproductResponse>

    lateinit var relatedadapter: ApprealsRelated_Adapter
    lateinit var procutindetail_image: ViewPager
    lateinit var procutindetail_newimage: TextView
    lateinit var procutindetail_title: TextView
    lateinit var procutindetail_subtitle: TextView
    lateinit var procutindetail_offerprice: TextView
    lateinit var procutindetail_mainprice: TextView
    lateinit var procutindetail_discount: TextView
    lateinit var procutindetail_size: TextView
    lateinit var procutindetail_weight: TextView
    lateinit var procutindetail_type: TextView
    lateinit var viewpagerlinear: LinearLayout
    lateinit var procutindetail_imageview: ImageView
    lateinit var procutindetail_grade: TextView
    lateinit var procutindetail_playertype: TextView
    lateinit var procutindetail_location: TextView
    lateinit var procutindetail_brand: TextView
    lateinit var procutindetail_qty: TextView
    lateinit var procutindetail_description: TextView
    lateinit var procutindetail_sellerinfo: TextView
    lateinit var procutindetail_sellerlocation: TextView
    lateinit var addcart_btn: Button
    lateinit var sharedPreferences: SharedPreferences
    lateinit var apparels_id: String
    lateinit var healthprod_id: String
    lateinit var accesoriesprod_id: String
    lateinit var apparelscat_id: String
    var electrioncs_id: Int = 0
    var apparelsdetails: Int = 0
    lateinit var productcircleindicator: CirclePageIndicator
    lateinit var progressBarproductdetails: ProgressBar
    lateinit var productrelatedrecyclerview: RecyclerView
    var listnames: MutableList<String> = mutableListOf<String>()
    lateinit var cart_badge: TextView
    lateinit var backimage_product: ImageView
    lateinit var item: CartItem
    lateinit var cartlayout: FrameLayout

    @SuppressLint("WrongConstant", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Paper.init(this)

        setContentView(R.layout.product_details_screen)

        productrelatedrecyclerview =
            findViewById<RecyclerView>(R.id.productrelatedrecyclerview) as RecyclerView

        sharedPreferences =
            getSharedPreferences(
                "loginprefs",
                Context.MODE_PRIVATE
            )
        addcart_btn = findViewById<Button>(R.id.addcart_btn) as Button
        backimage_product = toolbar.findViewById<ImageView>(R.id.backimage_product) as ImageView
        cartlayout = toolbar.findViewById<FrameLayout>(R.id.cartlayout) as FrameLayout
        procutindetail_imageview =
            findViewById<ImageView>(R.id.procutindetail_imageview) as ImageView
        viewpagerlinear = findViewById<LinearLayout>(R.id.viewpagerlinear) as LinearLayout
        progressBarproductdetails =
            findViewById<ProgressBar>(R.id.progressBarproductdetails) as ProgressBar
        procutindetail_image = findViewById<ViewPager>(R.id.procutindetail_image)

        productcircleindicator = findViewById<CirclePageIndicator>(R.id.productcircleindicator)
        procutindetail_newimage = findViewById<TextView>(R.id.procutindetail_newimage)
        procutindetail_title = findViewById<TextView>(R.id.procutindetail_title)
        procutindetail_subtitle = findViewById<TextView>(R.id.procutindetail_subtitle)
        procutindetail_offerprice = findViewById<TextView>(R.id.procutindetail_offerprice)
        procutindetail_discount = findViewById<TextView>(R.id.procutindetail_discount)
        procutindetail_mainprice = findViewById<TextView>(R.id.procutindetail_mainprice)
        procutindetail_size = findViewById<TextView>(R.id.procutindetail_size)
        procutindetail_weight = findViewById<TextView>(R.id.procutindetail_weight)
        procutindetail_type = findViewById<TextView>(R.id.procutindetail_type)
        procutindetail_grade = findViewById<TextView>(R.id.procutindetail_grade)
        procutindetail_playertype = findViewById<TextView>(R.id.procutindetail_playertype)
        procutindetail_location = findViewById<TextView>(R.id.procutindetail_location)
        procutindetail_brand = findViewById<TextView>(R.id.procutindetail_brand)
        procutindetail_qty = findViewById<TextView>(R.id.procutindetail_qty)
        procutindetail_description = findViewById<TextView>(R.id.procutindetail_description)
        procutindetail_sellerinfo = findViewById<TextView>(R.id.procutindetail_sellerinfo)
        procutindetail_sellerlocation = findViewById<TextView>(R.id.procutindetail_sellerlocation)

        progressBarproductdetails.visibility = View.VISIBLE
        sharedPreferences =
            getSharedPreferences(
                "loginprefs",
                Context.MODE_PRIVATE
            )

        apparelsdetails = sharedPreferences.getInt("product_indetails", 0)

        //adding a layoutmanager
        productrelatedrecyclerview.layoutManager =
            LinearLayoutManager(this, LinearLayout.HORIZONTAL, false)

        backimage_product.setOnClickListener {


            val intent = Intent(
                this@ProductDetails_Activity,
                ProductDetails_Activity::class.java
            )

            startActivity(intent)
            finish()


        }

        cart_badge = toolbar.findViewById(R.id.cart_badge)

        if (ShoppingCart.getShoppingCartSize().toString().equals("0")) {
            cart_badge.visibility = View.GONE
        } else {
            cart_badge.visibility = View.VISIBLE
            cart_badge.text = ShoppingCart.getShoppingCartSize().toString()
        }
        cartlayout.setOnClickListener {

            val intent = Intent(
                this@ProductDetails_Activity,
                Cart_Screen::class.java
            )
            startActivity(intent)
            finish()
        }

        if (apparelsdetails == 11) {

            getuserApparelsList()
            getRelatedAppealProductslist()
        } else if (apparelsdetails == 12) {
            getElectronocsData()
            getRelatedElectrnoicsProductslist()
        } else if (apparelsdetails == 17) {

            getHealthProdcutDetailsData()
            getRelatedHealthProductslist()
        } else if (apparelsdetails == 15) {
            getAccessoriesinDetailsData()
            getRelatedAccessroiesProductslist()
        } else if (apparelsdetails == 13) {
            getuserRecentsList()
            getRelatedProductslist()
        } else if (apparelsdetails == 14) {
            getCategorieslist()
            getRelatedProductslist()

        } else if (apparelsdetails == 16) {
            getFitnessList()
            //    getRelatedProductslist()

        } else if (apparelsdetails == 18) {
            getBrandsList()
            //    getRelatedProductslist()

        }


        addcart_btn.setOnClickListener {

//            startActivity(Intent(this, Cart_Screen::class.java))
//            finish()

            ShoppingCart.addItem(item)
            //notify users

            Toast.makeText(this, "Items Added Sucessfully", Toast.LENGTH_LONG)
                .show()


            ShoppingCart.getCart()
            startActivity(Intent(this, Cart_Screen::class.java))
            finish()

//            try {
//
//
//                Observable.create(ObservableOnSubscribe<MutableList<CartItem>> {
//                    val cart = ShoppingCart.getCart()
//
//
//                    ShoppingCart.addItem(item)
//                    //notify users
//
//                    Toast.makeText(this, "Items Added Sucessfully", Toast.LENGTH_LONG)
//                        .show()
//
//
////                    ShoppingCart.getCart()
//                    startActivity(Intent(this, Cart_Screen::class.java))
//                    finish()
//
//                }).subscribe { cart ->
//
//                    var quantity = 0
//
//                    cart.forEach { cartItem ->
//
//                        quantity += cartItem.quantity
//                    }
//
//
//                    Toast.makeText(this, "Cart size $quantity", Toast.LENGTH_SHORT).show()
//
//
//                }
//            } catch (e: OnErrorNotImplementedException) {
//                e.printStackTrace()
//            } catch (e: UninitializedPropertyAccessException) {
//                e.printStackTrace()
//            }

        }

//        getRelatedProductslist()

    }

    private fun getRelatedAppealProductslist() {

        if (NetWorkConection.isNEtworkConnected(this)) {

            //Set the Adapter to the RecyclerView//


            var apiServices = APIClient.client.create(Api::class.java)

            apparelscat_id = sharedPreferences.getString("apparelscat_id", "").toString()
            val call = apiServices.getRelatedApparealsProductList(apparelscat_id)

            call.enqueue(object : Callback<ApparelsProductsListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<ApparelsProductsListResponse>,
                    response: Response<ApparelsProductsListResponse>
                ) {

                    progressBarproductdetails.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//

                        listOfrecentapparels = response.body()?.response!!

                        val relatedadapter =
                            ApprealsRelated_Adapter(
                                this@ProductDetails_Activity,
                                listOfrecentapparels as ArrayList<ApparelsProductResponse>
                            )
                        productrelatedrecyclerview.adapter = relatedadapter
                        relatedadapter.notifyDataSetChanged()


                        val itemDecorator2 = VerticalSpacingItemDecorator(20)
                        productrelatedrecyclerview.addItemDecoration(itemDecorator2)



                        productrelatedrecyclerview.addOnItemClickListener(object :
                            OnItemClickListener {
                            override fun onItemClicked(position: Int, view: View) {
                                // Your logic
                                val appeals_id =
                                    relatedadapter.userList.get(position).apparels_id

                                val editor = sharedPreferences.edit()
                                editor.putString("apparels_id", appeals_id.toString())
                                editor.putInt("product_indetails", 11)
                                editor.commit()
                                val intent = Intent(
                                    this@ProductDetails_Activity,
                                    ProductDetails_Activity::class.java
                                )

                                startActivity(intent)


                            }
                        })

                    }
                }

                override fun onFailure(call: Call<ApparelsProductsListResponse>?, t: Throwable?) {
                    progressBarproductdetails.visibility = View.GONE
                    Log.e(ContentValues.TAG, t.toString())
                }
            })


        } else {

            Toast.makeText(this, "Please Check your internet", Toast.LENGTH_LONG).show()
        }
    }

    private fun getRelatedAccessroiesProductslist() {

        if (NetWorkConection.isNEtworkConnected(this)) {

            //Set the Adapter to the RecyclerView//


            var apiServices = APIClient.client.create(Api::class.java)

            val accessories_catid = sharedPreferences.getString("accessories_catid", "").toString()
            val call = apiServices.getRelatedAccessoriesList(accessories_catid)

            call.enqueue(object : Callback<AccessoriesProductListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<AccessoriesProductListResponse>,
                    response: Response<AccessoriesProductListResponse>
                ) {

                    progressBarproductdetails.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//

                        lostofrecentaccesories = response.body()?.response!!

                        val relatedadapter =
                            AccessoriesRelated_Adapter(
                                this@ProductDetails_Activity,
                                lostofrecentaccesories as ArrayList<AccessoriesProductResponse>
                            )
                        productrelatedrecyclerview.adapter = relatedadapter
                        relatedadapter.notifyDataSetChanged()


                        val itemDecorator2 = VerticalSpacingItemDecorator(20)
                        productrelatedrecyclerview.addItemDecoration(itemDecorator2)



                        productrelatedrecyclerview.addOnItemClickListener(object :
                            OnItemClickListener {
                            override fun onItemClicked(position: Int, view: View) {
                                // Your logic
                                val acces_categories_id =
                                    relatedadapter.userList.get(position).accessories_id

                                val editor = sharedPreferences.edit()
                                editor.putString(
                                    "acessoriesprod_id",
                                    acces_categories_id.toString()
                                )
                                editor.putInt("product_indetails", 15)
                                editor.commit()
                                val intent = Intent(
                                    this@ProductDetails_Activity,
                                    ProductDetails_Activity::class.java
                                )

                                startActivity(intent)


                            }
                        })

                    }
                }

                override fun onFailure(call: Call<AccessoriesProductListResponse>?, t: Throwable?) {
                    progressBarproductdetails.visibility = View.GONE
                    Log.e(ContentValues.TAG, t.toString())
                }
            })


        } else {

            Toast.makeText(this, "Please Check your internet", Toast.LENGTH_LONG).show()
        }
    }

    private fun getRelatedHealthProductslist() {

        if (NetWorkConection.isNEtworkConnected(this)) {

            //Set the Adapter to the RecyclerView//


            var apiServices = APIClient.client.create(Api::class.java)

            val healthprodcat_id =
                sharedPreferences.getString("healthprodcat_id", "").toString()
            val call = apiServices.getRelatedHealthProductsList(healthprodcat_id)

            call.enqueue(object : Callback<HealthProductsListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<HealthProductsListResponse>,
                    response: Response<HealthProductsListResponse>
                ) {

                    progressBarproductdetails.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//

                        lostofrecenthealth = response.body()?.response!!

                        val relatedadapter =
                            HealthPdocutsRelated_Adapter(
                                this@ProductDetails_Activity,
                                lostofrecenthealth as ArrayList<HealthProductsResponse>
                            )
                        productrelatedrecyclerview.adapter = relatedadapter
                        relatedadapter.notifyDataSetChanged()


                        val itemDecorator2 = VerticalSpacingItemDecorator(20)
                        productrelatedrecyclerview.addItemDecoration(itemDecorator2)



                        productrelatedrecyclerview.addOnItemClickListener(object :
                            OnItemClickListener {
                            override fun onItemClicked(position: Int, view: View) {
                                // Your logic
                                val health_pro_cat_id =
                                    relatedadapter.userList.get(position).health_pro_cat_id

                                val editor = sharedPreferences.edit()
                                editor.putString("healthprod_id", health_pro_cat_id.toString())
                                editor.putInt("product_indetails", 17)
                                editor.commit()
                                val intent = Intent(
                                    this@ProductDetails_Activity,
                                    ProductDetails_Activity::class.java
                                )

                                startActivity(intent)


                            }
                        })

                    }
                }

                override fun onFailure(call: Call<HealthProductsListResponse>?, t: Throwable?) {
                    progressBarproductdetails.visibility = View.GONE
                    Log.e(ContentValues.TAG, t.toString())
                }
            })


        } else {

            Toast.makeText(this, "Please Check your internet", Toast.LENGTH_LONG).show()
        }
    }

    private fun getRelatedElectrnoicsProductslist() {

        if (NetWorkConection.isNEtworkConnected(this)) {

            //Set the Adapter to the RecyclerView//


            var apiServices = APIClient.client.create(Api::class.java)

            val electronocs_cat_id =
                sharedPreferences.getInt("electronocs_cat_id", 0).toString()
            val call = apiServices.getrelatedElectronicsProductsList(electronocs_cat_id)

            call.enqueue(object : Callback<ElectronicsProductListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<ElectronicsProductListResponse>,
                    response: Response<ElectronicsProductListResponse>
                ) {

                    progressBarproductdetails.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//

                        lostofrecentlectronics = response.body()?.response!!

                        val relatedadapter =
                            ElectronicsRelated_Adapter(
                                this@ProductDetails_Activity,
                                lostofrecentlectronics as ArrayList<ElectronicsProductResponse>
                            )
                        productrelatedrecyclerview.adapter = relatedadapter
                        relatedadapter.notifyDataSetChanged()


                        val itemDecorator2 = VerticalSpacingItemDecorator(20)
                        productrelatedrecyclerview.addItemDecoration(itemDecorator2)



                        productrelatedrecyclerview.addOnItemClickListener(object :
                            OnItemClickListener {
                            override fun onItemClicked(position: Int, view: View) {
                                // Your logic
                                val electronic_id =
                                    relatedadapter.userList.get(position).electronics_id

                                val editor = sharedPreferences.edit()
                                editor.putString("electrioncs_id", electronic_id.toString())
                                editor.putInt("product_indetails", 12)
                                editor.commit()
                                val intent = Intent(
                                    applicationContext,
                                    ProductDetails_Activity::class.java
                                )

                                startActivity(intent)


                            }
                        })

                    }
                }

                override fun onFailure(call: Call<ElectronicsProductListResponse>?, t: Throwable?) {
                    progressBarproductdetails.visibility = View.GONE
                    Log.e(ContentValues.TAG, t.toString())
                }
            })


        } else {

            Toast.makeText(this, "Please Check your internet", Toast.LENGTH_LONG).show()
        }
    }

    private fun getRelatedProductslist() {

        if (NetWorkConection.isNEtworkConnected(this)) {

            //Set the Adapter to the RecyclerView//


            var apiServices = APIClient.client.create(Api::class.java)

            val sub_categories_id =
                sharedPreferences.getString("sub_categories_id", "").toString()
            val call = apiServices.getRelatedProductList(sub_categories_id)

            call.enqueue(object : Callback<RelatedproductListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<RelatedproductListResponse>,
                    response: Response<RelatedproductListResponse>
                ) {

                    progressBarproductdetails.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//

                        lostofrecentproducts = response.body()?.response!!

                        val relatedadapter =
                            ProductRelated_Adapter(
                                this@ProductDetails_Activity,
                                lostofrecentproducts as ArrayList<RelatedproductResponse>
                            )
                        productrelatedrecyclerview.adapter = relatedadapter
                        relatedadapter.notifyDataSetChanged()


                        val itemDecorator2 = VerticalSpacingItemDecorator(20)
                        productrelatedrecyclerview.addItemDecoration(itemDecorator2)



                        productrelatedrecyclerview.addOnItemClickListener(object :
                            OnItemClickListener {
                            override fun onItemClicked(position: Int, view: View) {
                                // Your logic
                                val subcategoryid =
                                    relatedadapter.userList.get(position).products_id

                                val editor = sharedPreferences.edit()
                                editor.putString("product_id", subcategoryid.toString())
                                editor.putInt("product_indetails", 14)
                                editor.commit()
                                val intent = Intent(
                                    this@ProductDetails_Activity,
                                    ProductDetails_Activity::class.java
                                )

                                startActivity(intent)


                            }
                        })

                    }
                }

                override fun onFailure(call: Call<RelatedproductListResponse>?, t: Throwable?) {
                    progressBarproductdetails.visibility = View.GONE
                    Log.e(ContentValues.TAG, t.toString())
                }
            })


        } else {

            Toast.makeText(this, "Please Check your internet", Toast.LENGTH_LONG).show()
        }
    }

    private fun getCategorieslist() {

        if (NetWorkConection.isNEtworkConnected(this)) {

            //Set the Adapter to the RecyclerView//


            var apiServices = APIClient.client.create(Api::class.java)
            val product_id = sharedPreferences.getString("product_id", "")

            val call = apiServices.getuserRecentinDetailsProdcts(product_id.toString())

            call.enqueue(object : Callback<RecentInDetailsProductsListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<RecentInDetailsProductsListResponse>,
                    response: Response<RecentInDetailsProductsListResponse>
                ) {

                    progressBarproductdetails.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//

                        listOfindetailscat = response.body()?.response!!

                        var recentlist = listOfindetailscat.map {

                            procutindetail_title.text = it.product_name
                            procutindetail_description.text = it.description
                            procutindetail_weight.text = it.weight_product
                            procutindetail_brand.text = it.manufacturer_brand
                            procutindetail_type.text = it.gender
                            procutindetail_mainprice.text =
                                "\u20B9 " + it.price.toString()

                            //cart items

                            try {


                                item = CartItem(
                                    it.products_id,
                                    it.product_name,
                                    it.product_image,
                                    it.price.toString(),
                                    it.sales_price.toString(),
                                    it.size,
                                    it.sales_price.toString(),
                                    it.quantity.toString(),
                                    0
                                )

                            } catch (e: NotImplementedError) {
                                e.printStackTrace()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
//                            try {
//
//
//                                item = CartItem(
//                                    it.products_id,
//                                    it.product_name,
//                                    it.product_image,
//                                    it.price.toString(),
//                                    it.sales_price.toString(),
//                                    it.size,
//                                    it.sales_price.toString(),
//                                    it.quantity.toString(),
//                                    it.quantity
//                                )
//
//                            } catch (e: NotImplementedError) {
//                                e.printStackTrace()
//                            } catch (e: Exception) {
//                                e.printStackTrace()
//                            }
//                            if (it.size.isEmpty()) {
//                                procutindetail_size.visibility = View.GONE
//                            } else {
//                                procutindetail_size.text = it.size.toString()
//
//                            }
                            var offerprice = it.sales_price

                            if (offerprice == null || offerprice.equals("") || (offerprice.equals("0") || (offerprice == 0))) {
                                procutindetail_offerprice.visibility = View.GONE
                                procutindetail_discount.visibility = View.GONE
                                procutindetail_newimage.visibility = View.GONE
                            } else {
                                procutindetail_offerprice.text = "\u20B9  " + offerprice

                                var regularprice = it.price.toString()
                                var saledprice = it.sales_price.toString()
                                var percentage =
                                    ((regularprice.toInt()
                                        .minus(saledprice.toInt())) / regularprice.toDouble() * 100)
                                val discountformat = DecimalFormat("##")


                                procutindetail_discount.text =
                                    discountformat.format(percentage) + "%"
                                Log.e("percentage", "" + percentage)
                            }
//                        prod.text = listOfapparels.quantity.toString()
//                        procutindetail_brand.text = listOfapparels.brand.toString()
//                        procutindetail_sellerinfo.text =
//                            listOfapparels.additional_info.toString()

                            var product_image = it.product_image

                            var sliderarray = it.slider_images
                            if (sliderarray.equals("") || (sliderarray.isEmpty()) || (sliderarray == null)) {
                                Glide.with(this@ProductDetails_Activity)
                                    .load(it.product_image)
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
                                        this@ProductDetails_Activity,
                                        sliderarray as java.util.ArrayList<String>
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

                        }

                    }
                }

                override fun onFailure(
                    call: Call<RecentInDetailsProductsListResponse>?,
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

    private fun getFitnessList() {

        if (NetWorkConection.isNEtworkConnected(this)) {

            //Set the Adapter to the RecyclerView//


            var apiServices = APIClient.client.create(Api::class.java)
            val fit_exce_id = sharedPreferences.getString("fit_exce_id", "")

            val call = apiServices.getfitnessindetailsList(fit_exce_id.toString())

            call.enqueue(object : Callback<FitnessDetailsListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<FitnessDetailsListResponse>,
                    response: Response<FitnessDetailsListResponse>
                ) {

                    progressBarproductdetails.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//

                        listOfindetailsfitness = response.body()?.response!!

                        var recentlist = listOfindetailsfitness.map {

                            procutindetail_title.text = it.product_name
                            procutindetail_description.text = it.description
                            procutindetail_weight.text = it.weight_product
                            procutindetail_brand.text = it.manufacturer_brand
                            procutindetail_type.text = it.gender
                            procutindetail_mainprice.text =
                                "\u20B9 " + it.price.toString()

                            //cart items


                            var offerprice = it.sales_price

                            if (offerprice == null || offerprice.equals("") || (offerprice.equals("0"))) {
                                procutindetail_offerprice.visibility = View.GONE
                                procutindetail_discount.visibility = View.GONE
                                procutindetail_newimage.visibility = View.GONE
                            } else {
                                procutindetail_offerprice.text = "\u20B9 " + offerprice

                                var regularprice = it.price.toString()
                                var saledprice = it.sales_price.toString()
                                var percentage =
                                    ((regularprice.toInt()
                                        .minus(saledprice.toInt())) / regularprice.toDouble() * 100)
                                val discountformat = DecimalFormat("##")


                                procutindetail_discount.text =
                                    discountformat.format(percentage) + "%"
                                Log.e("percentage", "" + percentage)
                            }


                            try {

                                item = CartItem(
                                    it.fit_exce_id,
                                    it.product_name,
                                    it.product_image,
                                    it.price.toString(),
                                    it.sales_price,
                                    it.size,
                                    it.sales_price.toString(),
                                    "1",
                                    0
                                )

                            } catch (e: NotImplementedError) {
                                e.printStackTrace()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
//                            try {
//
//                                item = CartItem(
//                                    it.categories_id,
//                                    it.product_name,
//                                    it.product_image,
//                                    it.price.toString(),
//                                    it.sales_price.toString(),
//                                    it.manufacturer_brand,
//                                    it.sales_price.toString(),
//                                    "1",
//                                    0
//                                )
//
//                            } catch (e: NotImplementedError) {
//                                e.printStackTrace()
//                            } catch (e: Exception) {
//                                e.printStackTrace()
//                            } catch (e: IllegalArgumentException) {
//                                e.printStackTrace()
//                            }

//                        prod.text = listOfapparels.quantity.toString()
//                        procutindetail_brand.text = listOfapparels.brand.toString()
//                        procutindetail_sellerinfo.text =
//                            listOfapparels.additional_info.toString()

                            var product_image = it.product_image

                            var sliderarray = it.slider_images
                            if (sliderarray.equals("") || (sliderarray.isEmpty()) || (sliderarray == null)) {
                                Glide.with(this@ProductDetails_Activity)
                                    .load(it.product_image)
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
                                        this@ProductDetails_Activity,
                                        sliderarray as java.util.ArrayList<String>
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

                        }

                    }
                }

                override fun onFailure(
                    call: Call<FitnessDetailsListResponse>?,
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

    private fun getBrandsList() {

        if (NetWorkConection.isNEtworkConnected(this)) {

            //Set the Adapter to the RecyclerView//


            var apiServices = APIClient.client.create(Api::class.java)
            val product_id = sharedPreferences.getInt("product_id", 0)

            val call = apiServices.getproductsdetailsList(product_id.toString())

            call.enqueue(object : Callback<BrandsDetailsListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<BrandsDetailsListResponse>,
                    response: Response<BrandsDetailsListResponse>
                ) {

                    progressBarproductdetails.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//

                        listOfindetailsbrands = response.body()?.response!!

                        var recentlist = listOfindetailsbrands.map {

                            procutindetail_title.text = it.product_name
                            procutindetail_description.text = it.description
                            procutindetail_weight.text = it.weight_product
                            procutindetail_brand.text = it.manufacturer_brand
                            procutindetail_type.text = it.gender
                            procutindetail_mainprice.text =
                                "\u20B9 " + it.price.toString()


                            try {


                                item = CartItem(
                                    it.products_id,
                                    it.product_name,
                                    it.product_image,
                                    it.price.toString(),
                                    it.sales_price.toString(),
                                    it.size,
                                    it.sales_price.toString(),
                                    it.quantity.toString(),
                                    0
                                )

                            } catch (e: NotImplementedError) {
                                e.printStackTrace()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                            //cart items
//                            try {
//
//
//                                item = CartItem(
//                                    it.products_id,
//                                    it.product_name,
//                                    it.product_image,
//                                    it.price.toString(),
//                                    it.sales_price.toString(),
//                                    it.size,
//                                    it.sales_price.toString(),
//                                    it.quantity.toString(),
//                                    it.quantity
//                                )
//
//                            } catch (e: NotImplementedError) {
//                                e.printStackTrace()
//                            } catch (e: Exception) {
//                                e.printStackTrace()
//                            }
//                            if (it.size.isEmpty()) {
//                                procutindetail_size.visibility = View.GONE
//                            } else {
//                                procutindetail_size.text = it.size.toString()
//
//                            }
                            var offerprice = it.sales_price

                            if (offerprice == null || offerprice.equals("") || (offerprice.equals("0"))) {
                                procutindetail_offerprice.visibility = View.GONE
                                procutindetail_discount.visibility = View.GONE
                                procutindetail_newimage.visibility = View.GONE
                            } else {
                                procutindetail_offerprice.text = "\u20B9 " + offerprice

                                var regularprice = it.price.toString()
                                var saledprice = it.sales_price.toString()
                                var percentage =
                                    ((regularprice.toInt()
                                        .minus(saledprice.toInt())) / regularprice.toDouble() * 100)
                                val discountformat = DecimalFormat("##")


                                procutindetail_discount.text =
                                    discountformat.format(percentage) + "%"
                                Log.e("percentage", "" + percentage)
                            }
//                        prod.text = listOfapparels.quantity.toString()
//                        procutindetail_brand.text = listOfapparels.brand.toString()
//                        procutindetail_sellerinfo.text =
//                            listOfapparels.additional_info.toString()

                            var product_image = it.product_image

                            var sliderarray = it.slider_images
                            if (sliderarray.equals("") || (sliderarray.isEmpty()) || (sliderarray == null)) {
                                Glide.with(this@ProductDetails_Activity)
                                    .load(it.product_image)
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
                                        this@ProductDetails_Activity,
                                        sliderarray as java.util.ArrayList<String>
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

                        }

                    }
                }

                override fun onFailure(
                    call: Call<BrandsDetailsListResponse>?,
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


    private fun getuserRecentsList() {

        if (NetWorkConection.isNEtworkConnected(this)) {

            //Set the Adapter to the RecyclerView//


            var apiServices = APIClient.client.create(Api::class.java)
            val recentprod_id = sharedPreferences.getString("recentprod_id", "").toString()


            val call = apiServices.getuserRecentinDetailsProdcts(recentprod_id)

            Log.e("recentprod_id ", "" + recentprod_id)
            call.enqueue(object : Callback<RecentInDetailsProductsListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<RecentInDetailsProductsListResponse>,
                    response: Response<RecentInDetailsProductsListResponse>
                ) {

                    progressBarproductdetails.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//

                        listOfrecent = response.body()?.response!!

                        var recentlist = listOfrecent.map {


                            procutindetail_title.text = it.product_name
                            procutindetail_description.text = it.description
                            procutindetail_weight.text = it.weight_product
                            procutindetail_brand.text = it.manufacturer_brand
                            procutindetail_type.text = it.gender
                            procutindetail_mainprice.text =
                                "\u20B9 " + it.price.toString()


//                            if (it.size.isEmpty()) {
//                                procutindetail_size.visibility = View.GONE
//                            } else {
//                                procutindetail_size.text = it.size.toString()
//
//                            }
                            var offerprice = it.sales_price

                            if (offerprice == null || offerprice.equals("")) {
                                procutindetail_offerprice.visibility = View.GONE
                                procutindetail_discount.visibility = View.GONE
                                procutindetail_newimage.visibility = View.GONE
                            } else {
                                procutindetail_offerprice.text = "\u20B9 " + offerprice

                                var regularprice = it.price.toString()
                                var saledprice = it.sales_price.toString()
                                var percentage =
                                    ((regularprice.toInt()
                                        .minus(saledprice.toInt())) / regularprice.toDouble() * 100)
                                val discountformat = DecimalFormat("##")


                                procutindetail_discount.text =
                                    discountformat.format(percentage) + "%"
                                Log.e("percentage", "" + percentage)
                            }

                            try {


                                item = CartItem(
                                    it.products_id,
                                    it.product_name,
                                    it.product_image,
                                    it.price.toString(),
                                    it.sales_price.toString(),
                                    it.size,
                                    it.sales_price.toString(),
                                    it.quantity.toString(),
                                    0
                                )

                            } catch (e: NotImplementedError) {
                                e.printStackTrace()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
//                            try {
//
//
//                                item = CartItem(
//                                    it.products_id,
//                                    it.product_name,
//                                    it.product_image,
//                                    it.price.toString(),
//                                    it.sales_price.toString(),
//                                    it.size,
//                                    it.sales_price.toString(),
//                                    it.quantity.toString(),
//                                    0
//                                )
//                            } catch (e: IllegalArgumentException) {
//                                e.printStackTrace()
//                            } catch (e1: Exception) {
//                                e1.printStackTrace()
//                            }
//                        prod.text = listOfapparels.quantity.toString()
//                        procutindetail_brand.text = listOfapparels.brand.toString()
//                        procutindetail_sellerinfo.text =
//                            listOfapparels.additional_info.toString()

                            var sliderarray = it.slider_images
                            if (sliderarray.equals("") || (sliderarray.isEmpty()) || (sliderarray == null)) {
                                Glide.with(this@ProductDetails_Activity)
                                    .load(it.product_image)
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
                                        this@ProductDetails_Activity,
                                        sliderarray as java.util.ArrayList<String>
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

                        }


                    }
                }

                override fun onFailure(
                    call: Call<RecentInDetailsProductsListResponse>?,
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

    private fun getuserApparelsList() {

        if (NetWorkConection.isNEtworkConnected(this)) {

            //Set the Adapter to the RecyclerView//


            var apiServices = APIClient.client.create(Api::class.java)
            apparels_id = sharedPreferences.getString("apparels_id", "").toString()


            val call = apiServices.getApparealsInDetailsProductList(apparels_id)

            Log.e("apparel id", "" + apparels_id)
            call.enqueue(object : Callback<ApparealsDetailsListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<ApparealsDetailsListResponse>,
                    response: Response<ApparealsDetailsListResponse>
                ) {

                    progressBarproductdetails.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//

                        listOfapparels = response.body()?.response!!

                        item = CartItem(
                            listOfapparels.apparels_id,
                            listOfapparels.name,
                            listOfapparels.product_image,
                            listOfapparels.regular_price.toString(),
                            listOfapparels.sales_price.toString(),
                            listOfapparels.size,
                            listOfapparels.sales_price.toString(),
                            listOfapparels.quantity.toString(),
                            0
                        )

                        procutindetail_title.text = listOfapparels.name
                        procutindetail_description.text = listOfapparels.description
                        procutindetail_brand.text = listOfapparels.brand
                        procutindetail_type.text = listOfapparels.gender
                        procutindetail_mainprice.text =
                            "\u20B9 " + listOfapparels.regular_price.toString()
                        var offerprice = listOfapparels.sales_price

                        if (offerprice == null || offerprice.equals("")) {
                            procutindetail_offerprice.visibility = View.GONE
                            procutindetail_discount.visibility = View.GONE
                            procutindetail_newimage.visibility = View.GONE
                        } else {
                            procutindetail_offerprice.text = "\u20B9 " + offerprice

                            var regularprice = listOfapparels.regular_price.toString()
                            var saledprice = listOfapparels.sales_price.toString()
                            var percentage =
                                ((regularprice.toInt()
                                    .minus(saledprice.toInt())) / regularprice.toDouble() * 100)
                            val discountformat = DecimalFormat("##")


                            procutindetail_discount.text =
                                discountformat.format(percentage) + "%"
                            Log.e("percentage", "" + percentage)
                        }
                        procutindetail_size.text = listOfapparels.size.toString()
//                        procutindetail_qty.text = listOfapparels.quantity.toString()
//                        procutindetail_brand.text = listOfapparels.brand.toString()
//                        procutindetail_sellerinfo.text =
//                            listOfapparels.additional_info.toString()


                        var sliderarray = listOfapparels.slider_images
                        if (sliderarray.equals("") || (sliderarray.isEmpty()) || (sliderarray == null)) {
                            Glide.with(this@ProductDetails_Activity)
                                .load(listOfapparels.product_image)
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
                                    this@ProductDetails_Activity,
                                    sliderarray as java.util.ArrayList<String>
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


                    }
                }

                override fun onFailure(
                    call: Call<ApparealsDetailsListResponse>?,
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

    private fun getElectronocsData() {

        if (NetWorkConection.isNEtworkConnected(this)) {

            //Set the Adapter to the RecyclerView//


            var apiServices = APIClient.client.create(Api::class.java)
            electrioncs_id = sharedPreferences.getInt("electrioncs_id", 0)
            Log.e("electrioncs_id", "" + electrioncs_id)

            val call = apiServices.getElectronicsinDetails(electrioncs_id.toString())

            call.enqueue(object : Callback<ElectronicsDetailsListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<ElectronicsDetailsListResponse>,
                    response: Response<ElectronicsDetailsListResponse>
                ) {

                    progressBarproductdetails.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//

                        listOfelectronics = response.body()?.response!!


                        try {


                            item = CartItem(
                                listOfelectronics.electronics_id,
                                listOfelectronics.name,
                                listOfelectronics.product_image,
                                listOfelectronics.regular_price.toString(),
                                listOfelectronics.sales_price.toString(),
                                listOfelectronics.size,
                                listOfelectronics.sales_price.toString(),
                                listOfelectronics.quantity.toString(),
                                0
                            )

                        } catch (e: NotImplementedError) {
                            e.printStackTrace()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                        procutindetail_title.text = listOfelectronics.name
                        procutindetail_description.text = listOfelectronics.description
                        procutindetail_brand.text = listOfelectronics.brand
                        procutindetail_type.text = listOfelectronics.gender
                        procutindetail_mainprice.text =
                            "\u20B9 " + listOfelectronics.regular_price.toString()

                        procutindetail_size.text = listOfelectronics.size.toString()

                        var offerprice = listOfelectronics.sales_price

                        if (offerprice == null || offerprice.equals("") || (offerprice.equals("0") || (offerprice == 0))) {
                            procutindetail_offerprice.visibility = View.GONE
                            procutindetail_discount.visibility = View.GONE
                            procutindetail_newimage.visibility = View.GONE
                        } else {
                            procutindetail_offerprice.text = "\u20B9 " + offerprice

                            var regularprice = listOfelectronics.regular_price.toString()
                            var saledprice = listOfelectronics.sales_price.toString()
                            var percentage =
                                ((regularprice.toInt()
                                    .minus(saledprice.toInt())) / regularprice.toDouble() * 100)
                            val discountformat = DecimalFormat("##")


                            procutindetail_discount.text =
                                discountformat.format(percentage) + "%"
                            Log.e("percentage", "" + percentage)
                        }
//                        procutindetail_qty.text = listOfelectronics.quantity.toString()
//                        procutindetail_brand.text = listOfelectronics.brand.toString()
//                        procutindetail_sellerinfo.text =
//                            listOfelectronics.additional_info.toString()

                        var sliderarray = listOfelectronics.slider_images


                        if (sliderarray.equals("") || (sliderarray.isEmpty()) || (sliderarray == null)) {
                            Glide.with(this@ProductDetails_Activity)
                                .load(listOfelectronics.product_image)
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
                                    this@ProductDetails_Activity,
                                    sliderarray as java.util.ArrayList<String>
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
//                        Glide.with(this@ProductDetails_Activity)
//                            .load(listOfelectronics.product_image)
//                            .apply(RequestOptions().centerCrop())
//                            .error(R.drawable.logo)
//                            .into(procutindetail_image)


                    }
                }

                override fun onFailure(
                    call: Call<ElectronicsDetailsListResponse>?,
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

    private fun getHealthProdcutDetailsData() {

        if (NetWorkConection.isNEtworkConnected(this)) {

            //Set the Adapter to the RecyclerView//


            var apiServices = APIClient.client.create(Api::class.java)
            healthprod_id = sharedPreferences.getString("healthprod_id", "").toString()
            Log.e("healthprod_id", "" + healthprod_id)

            val call = apiServices.getHealthProductsinDetails(healthprod_id)

            call.enqueue(object : Callback<HealthProductDetailsListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<HealthProductDetailsListResponse>,
                    response: Response<HealthProductDetailsListResponse>
                ) {

                    progressBarproductdetails.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//

                        listOfhealth = response.body()?.response!!

                        try {

                            item = CartItem(
                                listOfhealth.health_products_id,
                                listOfhealth.name,
                                listOfhealth.product_image,
                                listOfhealth.regular_price.toString(),
                                listOfhealth.sales_price.toString(),
                                listOfhealth.size,
                                listOfhealth.sales_price.toString(),
                                listOfhealth.quantity.toString(),
                                0
                            )

                        } catch (e: NotImplementedError) {
                            e.printStackTrace()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }





                        procutindetail_title.text = listOfhealth.name
                        procutindetail_description.text = listOfhealth.description
                        procutindetail_brand.text = listOfhealth.brand
                        procutindetail_type.text = listOfhealth.gender
                        procutindetail_mainprice.text =
                            "\u20B9 " + listOfhealth.regular_price.toString()

                        var offerprice = listOfhealth.sales_price

                        if (offerprice == null || offerprice.equals("") || (offerprice.equals("0") || (offerprice == 0))) {
                            procutindetail_offerprice.visibility = View.GONE
                            procutindetail_discount.visibility = View.GONE
                            procutindetail_newimage.visibility = View.GONE
                        } else {
                            procutindetail_offerprice.text = "\u20B9 " + offerprice

                            var regularprice = listOfhealth.regular_price.toString()
                            var saledprice = listOfhealth.sales_price.toString()
                            var percentage =
                                ((regularprice.toInt()
                                    .minus(saledprice.toInt())) / regularprice.toDouble() * 100)
                            val discountformat = DecimalFormat("##")


                            procutindetail_discount.text =
                                discountformat.format(percentage) + "%"
                            Log.e("percentage", "" + percentage)
                        }
                        procutindetail_size.text = listOfhealth.size.toString()
//                        procutindetail_qty.text = listOfhealth.quantity.toString()
//                        procutindetail_brand.text = listOfhealth.brand.toString()
//                        procutindetail_sellerinfo.text =
//                            listOfhealth.additional_info.toString()


                        var sliderarray = listOfhealth.slider_images

                        if (sliderarray.equals("") || (sliderarray.isEmpty()) || (sliderarray == null)) {
                            Glide.with(this@ProductDetails_Activity)
                                .load(listOfhealth.product_image)
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
                                    this@ProductDetails_Activity,
                                    sliderarray as java.util.ArrayList<String>
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


                    }
                }

                override fun onFailure(
                    call: Call<HealthProductDetailsListResponse>?,
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

    private fun getAccessoriesinDetailsData() {

        if (NetWorkConection.isNEtworkConnected(this)) {

            //Set the Adapter to the RecyclerView//


            var apiServices = APIClient.client.create(Api::class.java)
            accesoriesprod_id = sharedPreferences.getString("acessoriesprod_id", "").toString()
            Log.e("acessoriesprod_id", "" + accesoriesprod_id)

            val call = apiServices.getAccessoriesinDetailsList(accesoriesprod_id)

            call.enqueue(object : Callback<AccessoriesinDetailsListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<AccessoriesinDetailsListResponse>,
                    response: Response<AccessoriesinDetailsListResponse>
                ) {

                    progressBarproductdetails.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//

                        listOfaccessories = response.body()?.response!!

                        try {

                            item = CartItem(
                                listOfaccessories.accessories_id,
                                listOfaccessories.name,
                                listOfaccessories.product_image,
                                listOfaccessories.regular_price.toString(),
                                listOfaccessories.sales_price.toString(),
                                listOfaccessories.size,
                                listOfaccessories.sales_price.toString(),
                                listOfaccessories.quantity,
                                0
                            )

                        } catch (e: NotImplementedError) {
                            e.printStackTrace()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }


                        procutindetail_title.text = listOfaccessories.name
                        procutindetail_description.text = listOfaccessories.description
                        procutindetail_brand.text = listOfaccessories.brand
                        procutindetail_type.text = listOfaccessories.gender
                        procutindetail_mainprice.text =
                            "\u20B9 " + listOfaccessories.regular_price.toString()

                        procutindetail_size.text = listOfaccessories.size.toString()
//                        procutindetail_brand.text = listOfaccessories.brand.toString()
//                        procutindetail_sellerinfo.text =
//                            listOfaccessories.additional_info.toString()

                        var offerprice = listOfaccessories.sales_price

                        if (offerprice == null || offerprice.equals("") || (offerprice.equals("0") || (offerprice == 0))) {
                            procutindetail_offerprice.visibility = View.GONE
                            procutindetail_discount.visibility = View.GONE
                            procutindetail_newimage.visibility = View.GONE
                        } else {
                            procutindetail_offerprice.text = "\u20B9 " + offerprice

                            var regularprice = listOfaccessories.regular_price.toString()
                            var saledprice = listOfaccessories.sales_price.toString()
                            var percentage =
                                ((regularprice.toInt()
                                    .minus(saledprice.toInt())) / regularprice.toDouble() * 100)
                            val discountformat = DecimalFormat("##")


                            procutindetail_discount.text =
                                discountformat.format(percentage) + "%"
                            Log.e("percentage", "" + percentage)
                        }

                        var sliderarray = listOfaccessories.slider_images

                        if (sliderarray.equals("") || (sliderarray.isEmpty()) || (sliderarray == null)) {
                            Glide.with(this@ProductDetails_Activity)
                                .load(listOfaccessories.product_image)
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
                                    this@ProductDetails_Activity,
                                    sliderarray as java.util.ArrayList<String>
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


                    }
                }

                override fun onFailure(
                    call: Call<AccessoriesinDetailsListResponse>?,
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