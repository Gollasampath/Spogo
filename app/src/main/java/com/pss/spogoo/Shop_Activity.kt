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
import com.pss.spogoo.ui.Category_Screen.CategoryMainActivity
import com.pss.spogoo.util.NetWorkConection
import com.pss.spogoo.util.VerticalSpacingItemDecorator
import com.viewpagerindicator.CirclePageIndicator
import io.paperdb.Paper
import kotlinx.android.synthetic.main.product_details_screen.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class Shop_Activity : AppCompatActivity() {

    var allcategoryAdapter: ShopAllCategory_Adapter? = null
    var brandslist: ShopBrandsList_Adapter? = null
    var apperealsAdapter: ShopAppareals_Adapter? = null
    var recentproductsAdapter: ShopRepair_Adapter? = null
    var popularsports: ShopPopularSports_Adapter? = null
    var shopaccessoriesAdapter: ShopAccessories_Adapter? = null
    var electronicsadapter: ShopElectronics_Adapter? = null
    var healthproductsadapter: ShopHealthPrdct_Adapter? = null
    var shopfitnessAdapter: ShopFitness_Adapter? = null

    var adapter: BuildProfile_Adapter? = null

    lateinit var listOfrecentproducts: List<RecentProdcutsResponse>
    lateinit var listOfitnessrecent: List<FitnessExcerseResponse>
    lateinit var listOfbrands: List<BrandsProductsResponse>

    lateinit var listOfallcategories: List<ShopAllCategoryResponse>
    lateinit var listofappareals: List<ApparealsCategoryResponse>
    lateinit var listofaccessories: List<AccessoriesProductResponse>
    lateinit var listofelectronics: List<ElectronicsCategoriesResponse>
    lateinit var listofhealthprocts: List<HealthProductsResponse>
    lateinit var gvallcategories: RecyclerView
    lateinit var gvpopularsports: GridView
    lateinit var gvrecent: RecyclerView
    lateinit var gvfitness: RecyclerView
    lateinit var gvapparel: RecyclerView
    lateinit var gvacessories: RecyclerView
    lateinit var gvtopbrands: RecyclerView
    lateinit var gvelectronics: RecyclerView
    lateinit var gvhealthprodcuts: RecyclerView
    lateinit var backshop_image: ImageView
    lateinit var exploreall_shop: TextView
    lateinit var cartlayout: FrameLayout
    lateinit var progressBarshop: ProgressBar
    lateinit var sharedPreferences: SharedPreferences
    lateinit var shopcircleindicator: CirclePageIndicator
    private var imageModelArrayList: ArrayList<ImageModel>? = null

    private val myImageList = intArrayOf(
        R.drawable.sli4,
        R.drawable.sli5,
        R.drawable.sli1
    )
    lateinit var cart_badge: TextView

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Paper.init(this)

        setContentView(R.layout.shop_screen)


        sharedPreferences =
            getSharedPreferences(
                "loginprefs",
                Context.MODE_PRIVATE
            )
        progressBarshop = findViewById(R.id.progressBarshop)

        progressBarshop.visibility = View.VISIBLE

        cart_badge = toolbar.findViewById(R.id.cart_badge)

        Log.e("cartsize in shop", "" + ShoppingCart.getShoppingCartSize())
        if (ShoppingCart.getShoppingCartSize().toString().equals("0")) {
            cart_badge.visibility = View.GONE
        } else {
            cart_badge.visibility = View.VISIBLE

            cart_badge.text = ShoppingCart.getShoppingCartSize().toString()

        }
        gvallcategories = findViewById<RecyclerView>(R.id.gvallcategories) as RecyclerView
        gvfitness = findViewById<RecyclerView>(R.id.gvfitness) as RecyclerView
        backshop_image = findViewById<ImageView>(R.id.backshop_image) as ImageView
        exploreall_shop = findViewById<TextView>(R.id.exploreall_shop) as TextView
        gvrecent = findViewById<RecyclerView>(R.id.gvrecent) as RecyclerView
        cartlayout = findViewById<FrameLayout>(R.id.cartlayout) as FrameLayout
        gvtopbrands = findViewById<GridView>(R.id.gvtopbrands) as RecyclerView

        gvpopularsports = findViewById<GridView>(R.id.gvpopularsports) as GridView
        gvapparel = findViewById<RecyclerView>(R.id.gvapparel) as RecyclerView
        gvacessories = findViewById<RecyclerView>(R.id.gvacessories) as RecyclerView
        gvelectronics = findViewById<RecyclerView>(R.id.gvelectronics) as RecyclerView
        gvhealthprodcuts =
            findViewById<RecyclerView>(R.id.gvhealthprodcuts) as RecyclerView


        shopsliderimages = findViewById<ViewPager>(R.id.shopsliderimages)

        shopcircleindicator = findViewById<CirclePageIndicator>(R.id.shopcircleindicator)

        backshop_image.setOnClickListener {
            startActivity(Intent(this, Home_Screen::class.java))
            finish()
        }

        exploreall_shop.setOnClickListener {
            startActivity(Intent(this, CategoryMainActivity::class.java))
        }

        cartlayout.setOnClickListener {
            startActivity(Intent(this, Cart_Screen::class.java))
            finish()
        }

        //adding a layoutmanager
        gvallcategories.layoutManager =
            LinearLayoutManager(this, LinearLayout.HORIZONTAL, false)

        //adding a layoutmanager
        gvrecent.layoutManager =
            LinearLayoutManager(this, LinearLayout.HORIZONTAL, false)

        //adding a layoutmanager
        gvfitness.layoutManager =
            LinearLayoutManager(this, LinearLayout.HORIZONTAL, false)
        //appeal
        //adding a layoutmanager
        gvapparel.layoutManager =
            LinearLayoutManager(this, LinearLayout.HORIZONTAL, false)

        //adding a layoutmanager
        gvacessories.layoutManager =
            LinearLayoutManager(this, LinearLayout.HORIZONTAL, false)

        //adding a layoutmanager
        gvelectronics.layoutManager =
            LinearLayoutManager(this, LinearLayout.HORIZONTAL, false)


        //adding a layoutmanager
        gvhealthprodcuts.layoutManager =
            LinearLayoutManager(this, LinearLayout.HORIZONTAL, false)
        //adding a layoutmanager
        gvtopbrands.layoutManager =
            LinearLayoutManager(this, LinearLayout.HORIZONTAL, false)

        imageModelArrayList = ArrayList()
        imageModelArrayList = populateList()

//        var typeface = ResourcesCompat.getFont(this, R.font.poppins_bold)
//        fontText.setTypeface(typeface)

        imageSliderView()

        shopsliderimages!!.clipToPadding = false
        shopsliderimages!!.pageMargin = 10
        shopsliderimages!!.setPadding(40, 0, 40, 0)

        getCategoriesList()
        getRecentProducts()
        getPopularSports()
        getApparelsList()
        getAccessoriesData()
        getElectronicsData()
        getHealthProducts()
        getfitnessexcersie()
        getBrandsList()

    }


    private fun getBrandsList() {

        if (NetWorkConection.isNEtworkConnected(this)) {

            //Set the Adapter to the RecyclerView//


            var apiServices = APIClient.client.create(Api::class.java)


            val call = apiServices.getbrands_List()

            call.enqueue(object : Callback<BrandsListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<BrandsListResponse>,
                    response: Response<BrandsListResponse>
                ) {

                    progressBarshop.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//

                        listOfbrands = response.body()?.response!!

                        brandslist =
                            ShopBrandsList_Adapter(
                                this@Shop_Activity,
                                listOfbrands as ArrayList<BrandsProductsResponse>
                            )
                        gvtopbrands.adapter = brandslist
                        brandslist?.notifyDataSetChanged()


                        val itemDecorator2 = VerticalSpacingItemDecorator(20)
                        gvtopbrands.addItemDecoration(itemDecorator2)

                        gvtopbrands.addOnItemClickListener(object :
                            OnItemClickListener {
                            override fun onItemClicked(position: Int, view: View) {
                                // Your logic
                                val brands_id =
                                    brandslist!!.userList.get(position).brands_id
                                val brand_name =
                                    brandslist!!.userList.get(position).brand_name

                                val editor = sharedPreferences.edit()
                                editor.putString("brands_id", brands_id.toString())
                                editor.putString("brand_name", brand_name)
                                editor.putInt("indetailswitch", 4)
                                editor.commit()
                                val intent =
                                    Intent(this@Shop_Activity, UserProdctList_Screen::class.java)

//                                intent.putExtra("apparelscatid", apparelscatid)
                                startActivity(intent)


                            }
                        })

                    }
                }

                override fun onFailure(call: Call<BrandsListResponse>?, t: Throwable?) {
                    progressBarshop.visibility = View.GONE
                    Log.e(ContentValues.TAG, t.toString())
                }
            })


        } else {

            Toast.makeText(this@Shop_Activity, "Please Check your internet", Toast.LENGTH_LONG)
                .show()
        }


    }

    private fun getCategoriesList() {

        if (NetWorkConection.isNEtworkConnected(this)) {

            //Set the Adapter to the RecyclerView//


            var apiServices = APIClient.client.create(Api::class.java)


            val call = apiServices.getuserCategoriesList()

            call.enqueue(object : Callback<ShopListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<ShopListResponse>,
                    response: Response<ShopListResponse>
                ) {

                    progressBarshop.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//

                        listOfallcategories = response.body()?.response!!

                        allcategoryAdapter =
                            ShopAllCategory_Adapter(
                                this@Shop_Activity,
                                listOfallcategories as ArrayList<ShopAllCategoryResponse>
                            )
                        gvallcategories.adapter = allcategoryAdapter
                        allcategoryAdapter?.notifyDataSetChanged()


                        val itemDecorator2 = VerticalSpacingItemDecorator(20)
                        gvallcategories.addItemDecoration(itemDecorator2)

                        gvallcategories.addOnItemClickListener(object :
                            OnItemClickListener {
                            override fun onItemClicked(position: Int, view: View) {
                                // Your logic

                                val intent =
                                    Intent(this@Shop_Activity, AllProductsList_Activity::class.java)

                                startActivity(intent)
                                var productname =
                                    allcategoryAdapter!!.userList.get(position).category_name
                                var zoomimage =
                                    allcategoryAdapter!!.userList.get(position).zoom_image
                                var productcat_id =
                                    allcategoryAdapter!!.userList.get(position).categories_id
                                val editor = sharedPreferences.edit()
                                editor.putString("productname", productname)
                                editor.putString("zoomimage", zoomimage)
                                editor.putString("productcat_id", productcat_id.toString())
                                editor.commit()

                            }
                        })

                    }
                }

                override fun onFailure(call: Call<ShopListResponse>?, t: Throwable?) {
                    progressBarshop.visibility = View.GONE
                    Log.e(ContentValues.TAG, t.toString())
                }
            })


        } else {

            Toast.makeText(this@Shop_Activity, "Please Check your internet", Toast.LENGTH_LONG)
                .show()
        }


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


        shopsliderimages!!.adapter =
            ShopSlideImage_Adapter(this, imageModelArrayList!!)

        shopcircleindicator.setViewPager(shopsliderimages)

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
            shopsliderimages!!.setCurrentItem(
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

        private var shopsliderimages: ViewPager? = null
        private var currentPage = 0
        private var NUM_PAGES = 0
    }

    private fun getRecentProducts() {

        if (NetWorkConection.isNEtworkConnected(this)) {

            //Set the Adapter to the RecyclerView//


            var apiServices = APIClient.client.create(Api::class.java)


            val call = apiServices.getuserRecentProdctsList()

            call.enqueue(object : Callback<RecentProductsListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<RecentProductsListResponse>,
                    response: Response<RecentProductsListResponse>
                ) {

                    progressBarshop.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//

                        listOfrecentproducts = response.body()?.response!!

                        recentproductsAdapter =
                            ShopRepair_Adapter(
                                this@Shop_Activity,
                                listOfrecentproducts as ArrayList<RecentProdcutsResponse>
                            )
                        gvrecent.adapter = recentproductsAdapter
                        recentproductsAdapter?.notifyDataSetChanged()


                        val itemDecorator2 = VerticalSpacingItemDecorator(20)
                        gvrecent.addItemDecoration(itemDecorator2)

                        gvrecent.addOnItemClickListener(object :
                            OnItemClickListener {
                            override fun onItemClicked(position: Int, view: View) {
                                // Your logic
                                val recentproduct_id =
                                    recentproductsAdapter!!.userList.get(position).products_id
                                val recentcat_id =
                                    recentproductsAdapter!!.userList.get(position).sub_categories_id

                                val editor = sharedPreferences.edit()
                                editor.putString("recentprod_id", recentproduct_id.toString())
                                editor.putString("sub_categories_id", recentcat_id.toString())
                                editor.putInt("product_indetails", 13)
                                editor.commit()

                                val intent =
                                    Intent(this@Shop_Activity, ProductDetails_Activity::class.java)

                                startActivity(intent)


                            }
                        })

                    }
                }

                override fun onFailure(call: Call<RecentProductsListResponse>?, t: Throwable?) {
                    progressBarshop.visibility = View.GONE
                    Log.e(ContentValues.TAG, t.toString())
                }
            })


        } else {

            Toast.makeText(this, "Please Check your internet", Toast.LENGTH_LONG).show()
        }


    }

    private fun getfitnessexcersie() {

        if (NetWorkConection.isNEtworkConnected(this)) {

            //Set the Adapter to the RecyclerView//


            var apiServices = APIClient.client.create(Api::class.java)


            val call = apiServices.getfitnessList()

            call.enqueue(object : Callback<FitnesExcerseListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<FitnesExcerseListResponse>,
                    response: Response<FitnesExcerseListResponse>
                ) {

                    progressBarshop.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//

                        listOfitnessrecent = response.body()?.response!!

                        shopfitnessAdapter =
                            ShopFitness_Adapter(
                                this@Shop_Activity,
                                listOfitnessrecent as ArrayList<FitnessExcerseResponse>
                            )
                        gvfitness.adapter = shopfitnessAdapter
                        shopfitnessAdapter?.notifyDataSetChanged()


                        val itemDecorator2 = VerticalSpacingItemDecorator(20)
                        gvfitness.addItemDecoration(itemDecorator2)

                        gvfitness.addOnItemClickListener(object :
                            OnItemClickListener {
                            override fun onItemClicked(position: Int, view: View) {
                                // Your logic
                                val fit_exce_id =
                                    shopfitnessAdapter!!.userList.get(position).fit_exce_id
                                val recentcat_id =
                                    recentproductsAdapter!!.userList.get(position).sub_categories_id

                                val editor = sharedPreferences.edit()
                                editor.putString("fit_exce_id", fit_exce_id.toString())
                                editor.putInt("product_indetails", 16)
                                editor.commit()

                                val intent =
                                    Intent(this@Shop_Activity, ProductDetails_Activity::class.java)

                                startActivity(intent)


                            }
                        })

                    }
                }

                override fun onFailure(call: Call<FitnesExcerseListResponse>?, t: Throwable?) {
                    progressBarshop.visibility = View.GONE
                    Log.e(ContentValues.TAG, t.toString())
                }
            })


        } else {

            Toast.makeText(this, "Please Check your internet", Toast.LENGTH_LONG).show()
        }


    }

    private fun getApparelsList() {

        if (NetWorkConection.isNEtworkConnected(this)) {

            //Set the Adapter to the RecyclerView//


            var apiServices = APIClient.client.create(Api::class.java)


            val call = apiServices.getApparealsList()

            call.enqueue(object : Callback<ApparealsListCategoryResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<ApparealsListCategoryResponse>,
                    response: Response<ApparealsListCategoryResponse>
                ) {

                    progressBarshop.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//

                        listofappareals = response.body()?.response!!

                        apperealsAdapter =
                            ShopAppareals_Adapter(
                                this@Shop_Activity,
                                listofappareals as ArrayList<ApparealsCategoryResponse>
                            )
                        gvapparel.adapter = apperealsAdapter
                        apperealsAdapter?.notifyDataSetChanged()
                        val itemDecorator2 = VerticalSpacingItemDecorator(20)
                        gvapparel.addItemDecoration(itemDecorator2)
                        //listener

                        gvapparel.addOnItemClickListener(object :
                            OnItemClickListener {
                            override fun onItemClicked(position: Int, view: View) {
                                // Your logic
                                val apparelscatid =
                                    apperealsAdapter!!.userList.get(position).apparel_cat_id
                                val apparelscatname =
                                    apperealsAdapter!!.userList.get(position).category_name

                                val editor = sharedPreferences.edit()
                                editor.putString("apparelcat_id", apparelscatid.toString())
                                editor.putString("apparelcat_name", apparelscatname)
                                editor.putInt("indetailswitch", 2)
                                editor.commit()
                                val intent =
                                    Intent(this@Shop_Activity, UserProdctList_Screen::class.java)

//                                intent.putExtra("apparelscatid", apparelscatid)
                                startActivity(intent)


                            }
                        })

                    }
                }

                override fun onFailure(call: Call<ApparealsListCategoryResponse>?, t: Throwable?) {
                    progressBarshop.visibility = View.GONE
                    Log.e(ContentValues.TAG, t.toString())
                }
            })


        } else {

            Toast.makeText(this, "Please Check your internet", Toast.LENGTH_LONG).show()
        }


    }


    private fun getAccessoriesData() {

        if (NetWorkConection.isNEtworkConnected(this@Shop_Activity)) {

            //Set the Adapter to the RecyclerView//


            var apiServices = APIClient.client.create(Api::class.java)


            val call = apiServices.getAccessoriesList("")

            call.enqueue(object : Callback<AccessoriesProductListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<AccessoriesProductListResponse>,
                    response: Response<AccessoriesProductListResponse>
                ) {

                    progressBarshop.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//

                        listofaccessories = response.body()?.response!!

                        shopaccessoriesAdapter =
                            ShopAccessories_Adapter(
                                this@Shop_Activity,
                                listofaccessories as ArrayList<AccessoriesProductResponse>
                            )
                        gvacessories.adapter = shopaccessoriesAdapter
                        shopaccessoriesAdapter?.notifyDataSetChanged()

                        val itemDecorator2 = VerticalSpacingItemDecorator(20)
                        gvacessories.addItemDecoration(itemDecorator2)

                        gvacessories.addOnItemClickListener(object :
                            OnItemClickListener {
                            override fun onItemClicked(position: Int, view: View) {
                                // Your logic
                                val accessoriesid =
                                    shopaccessoriesAdapter!!.userList.get(position).accessories_id
                                val accessories_catid =
                                    shopaccessoriesAdapter!!.userList.get(position).acces_categories_id

                                val editor = sharedPreferences.edit()
                                editor.putString("acessoriesprod_id", accessoriesid.toString())
                                editor.putString("accessories_catid", accessories_catid.toString())
                                editor.putInt("product_indetails", 15)
                                editor.commit()
                                val intent =
                                    Intent(this@Shop_Activity, ProductDetails_Activity::class.java)

                                startActivity(intent)


                            }
                        })

                    }
                }

                override fun onFailure(call: Call<AccessoriesProductListResponse>?, t: Throwable?) {
                    progressBarshop.visibility = View.GONE
                    Log.e(ContentValues.TAG, t.toString())
                }
            })


        } else {

            Toast.makeText(this@Shop_Activity, "Please Check your internet", Toast.LENGTH_LONG)
                .show()
        }


    }

    private fun getElectronicsData() {

        if (NetWorkConection.isNEtworkConnected(this@Shop_Activity)) {

            //Set the Adapter to the RecyclerView//


            var apiServices = APIClient.client.create(Api::class.java)


            val call = apiServices.getElectronicsList()

            call.enqueue(object : Callback<ElectronicsCategoriesListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<ElectronicsCategoriesListResponse>,
                    response: Response<ElectronicsCategoriesListResponse>
                ) {

                    progressBarshop.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//

                        listofelectronics = response.body()?.response!!

                        electronicsadapter =
                            ShopElectronics_Adapter(
                                this@Shop_Activity,
                                listofelectronics as ArrayList<ElectronicsCategoriesResponse>
                            )
                        gvelectronics.adapter = electronicsadapter
                        electronicsadapter?.notifyDataSetChanged()

                        val itemDecorator2 = VerticalSpacingItemDecorator(20)
                        gvelectronics.addItemDecoration(itemDecorator2)


                        gvelectronics.addOnItemClickListener(object :
                            OnItemClickListener {
                            override fun onItemClicked(position: Int, view: View) {
                                // Your logic
                                val editor = sharedPreferences.edit()

                                val elec_catid =
                                    electronicsadapter!!.userList.get(position).elec_categories_id
                                val elec_catname =
                                    electronicsadapter!!.userList.get(position).category_name

                                Log.e("electronic_catid", "" + elec_catid)
                                editor.putString("electroniccatid", elec_catid.toString())
                                editor.putString("elec_catname", elec_catname)
                                editor.putInt("indetailswitch", 1)
                                editor.commit()
                                val intent =
                                    Intent(this@Shop_Activity, UserProdctList_Screen::class.java)


                                startActivity(intent)


                            }
                        })

                    }
                }

                override fun onFailure(
                    call: Call<ElectronicsCategoriesListResponse>?,
                    t: Throwable?
                ) {
                    progressBarshop.visibility = View.GONE
                    Log.e(ContentValues.TAG, t.toString())
                }
            })


        } else {

            Toast.makeText(this@Shop_Activity, "Please Check your internet", Toast.LENGTH_LONG)
                .show()
        }


    }


    private fun getHealthProducts() {

        if (NetWorkConection.isNEtworkConnected(this@Shop_Activity)) {

            //Set the Adapter to the RecyclerView//


            var apiServices = APIClient.client.create(Api::class.java)


            val call = apiServices.getHealthProductsList("")

            call.enqueue(object : Callback<HealthProductsListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<HealthProductsListResponse>,
                    response: Response<HealthProductsListResponse>
                ) {

                    progressBarshop.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//

                        listofhealthprocts = response.body()?.response!!

                        healthproductsadapter =
                            ShopHealthPrdct_Adapter(
                                this@Shop_Activity,
                                listofhealthprocts as ArrayList<HealthProductsResponse>
                            )
                        gvhealthprodcuts.adapter = healthproductsadapter
                        healthproductsadapter?.notifyDataSetChanged()


                        val itemDecorator2 = VerticalSpacingItemDecorator(20)
                        gvhealthprodcuts.addItemDecoration(itemDecorator2)

                        gvhealthprodcuts.addOnItemClickListener(object :
                            OnItemClickListener {
                            override fun onItemClicked(position: Int, view: View) {
                                // Your logic
                                val healthprod_id =
                                    healthproductsadapter!!.userList.get(position).health_pro_cat_id
                                val healthprodcat_id =
                                    healthproductsadapter!!.userList.get(position).health_pro_cat_id

                                val editor = sharedPreferences.edit()
                                editor.putString("healthprod_id", healthprod_id.toString())
                                editor.putString("healthprodcat_id", healthprodcat_id.toString())
                                editor.putInt("product_indetails", 17)
                                editor.commit()
                                val intent =
                                    Intent(this@Shop_Activity, ProductDetails_Activity::class.java)

                                startActivity(intent)


                            }
                        })

                    }
                }

                override fun onFailure(call: Call<HealthProductsListResponse>?, t: Throwable?) {
                    progressBarshop.visibility = View.GONE
                    Log.e(ContentValues.TAG, t.toString())
                }
            })


        } else {

            Toast.makeText(this@Shop_Activity, "Please Check your internet", Toast.LENGTH_LONG)
                .show()
        }


    }


    private fun getPopularSports() {

        if (NetWorkConection.isNEtworkConnected(this@Shop_Activity)) {

            //Set the Adapter to the RecyclerView//


            var apiServices = APIClient.client.create(Api::class.java)


            val call = apiServices.getuserCategoriesList()

            call.enqueue(object : Callback<ShopListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<ShopListResponse>,
                    response: Response<ShopListResponse>
                ) {

                    progressBarshop.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//

                        listOfallcategories = response.body()?.response!!

                        popularsports =
                            ShopPopularSports_Adapter(
                                this@Shop_Activity,
                                listOfallcategories as ArrayList<ShopAllCategoryResponse>
                            )


                        gvpopularsports.adapter = popularsports
                        popularsports!!.notifyDataSetChanged()

                        gvpopularsports.setOnItemClickListener { adapterView, parent, position, l ->
//                            Toast.makeText(
//                                this@Shop_Activity,
//                                "Click on : " + gvpopularsports,
//                                Toast.LENGTH_LONG
//                            ).show()
                            val intent =
                                Intent(this@Shop_Activity, AllProductsList_Activity::class.java)

                            startActivity(intent)
                            var productname =
                                popularsports!!.arrayListImage.get(position).category_name
                            var productcat_id =
                                popularsports!!.arrayListImage.get(position).categories_id
                            val editor = sharedPreferences.edit()
                            editor.putString("productname", productname)
                            editor.putString("productcat_id", productcat_id.toString())
                            editor.commit()

                        }

//                        val itemDecorator2 = VerticalSpacingItemDecorator(20)
//                        gvsportslist.addItemDecoration(itemDecorator2)
                    }
                }

                override fun onFailure(call: Call<ShopListResponse>?, t: Throwable?) {
                    progressBarshop.visibility = View.GONE
                    Log.e(ContentValues.TAG, t.toString())
                }
            })


        } else {

            Toast.makeText(this@Shop_Activity, "Please Check your internet", Toast.LENGTH_LONG)
                .show()
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
