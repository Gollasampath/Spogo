package com.pss.spogoo

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.pss.spogoo.adapter.*
import com.pss.spogoo.api.APIClient
import com.pss.spogoo.api.Api
import com.pss.spogoo.models.*
import com.pss.spogoo.util.NetWorkConection
import io.paperdb.Paper
import kotlinx.android.synthetic.main.userproduct_screen.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UserProdctList_Screen : AppCompatActivity() {
    lateinit var listOfapparels: List<ApparelsProductResponse>
    lateinit var listOfsortapparels: List<SortApparelsResponse>
    lateinit var listOfelectronics: List<ElectronicsProductResponse>
    lateinit var listOfcategories: List<SubCategoryResponse>
    lateinit var listOfrelatedsearch: List<SearchRelatedResponse>
    lateinit var listOfbrands: List<BrandsProductsDeResponse>

    lateinit var gvuserproductdetsils: GridView
    lateinit var gvfitness: GridView
    lateinit var gvsearchproduct: GridView
    lateinit var gvusercategory: GridView
    lateinit var productname_text: TextView
    lateinit var noproducts: TextView
    lateinit var producttitle: TextView
    lateinit var productback_image: ImageView
    lateinit var gvuserelectronics: GridView
    lateinit var apparelscatid: String
    lateinit var subcategories_id: String
    lateinit var apparelcat_name: String
    lateinit var elec_catname: String
    lateinit var category_catname: String
    lateinit var brand_name: String
    lateinit var searchcat_name: String
    lateinit var elec_catid: String
    lateinit var brands_id: String
    lateinit var sortitems_text: TextView
    var apparels: Int = 0
    var electronics: Int = 0
    lateinit var sharedPreferences: SharedPreferences
    lateinit var progressBarproduct: ProgressBar
    lateinit var cartlayout: FrameLayout
    lateinit var cart_badge: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Paper.init(this)

        setContentView(R.layout.userproduct_screen)

        gvuserproductdetsils =
            findViewById<GridView>(R.id.gvuserproductdetsils) as GridView
        noproducts = findViewById<TextView>(R.id.noproducts) as TextView
        sortitems_text = findViewById<TextView>(R.id.sortitems_text) as TextView
        productname_text = findViewById<TextView>(R.id.productname_text) as TextView
        gvusercategory = findViewById<GridView>(R.id.gvusercategory) as GridView
        gvfitness = findViewById<GridView>(R.id.gvfitness) as GridView
        productback_image = toolbar.findViewById<ImageView>(R.id.productback_image) as ImageView
        cartlayout = toolbar.findViewById<FrameLayout>(R.id.cartlayout) as FrameLayout
        producttitle = findViewById<TextView>(R.id.producttitle) as TextView
        gvuserelectronics = findViewById<GridView>(R.id.gvuserelectronics) as GridView
        gvsearchproduct = findViewById<GridView>(R.id.gvsearchproduct) as GridView
        progressBarproduct = findViewById<ProgressBar>(R.id.progressBarproduct) as ProgressBar

        sharedPreferences =
            getSharedPreferences("loginprefs", Context.MODE_PRIVATE)
        apparels = sharedPreferences.getInt("indetailswitch", 0)
        apparelcat_name = sharedPreferences.getString("apparelcat_name", "")!!
        elec_catname = sharedPreferences.getString("elec_catname", "")!!
        category_catname = sharedPreferences.getString("subcategory_name", "")!!
        searchcat_name = sharedPreferences.getString("searchcategoriename", "")!!
        brand_name = sharedPreferences.getString("brand_name", "")!!

        productback_image.setOnClickListener {
            startActivity(Intent(this, Shop_Activity::class.java))
            finish()
        }

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

        sortitems_text.setOnClickListener {

            val dialog = BottomSheetDialog(this)
            dialog.setContentView(R.layout.sort_screen)
            var newproducts = dialog.findViewById<TextView>(R.id.newproducts)
            var lowtohigh = dialog.findViewById<TextView>(R.id.lowtohigh)
            var hightolow = dialog.findViewById<TextView>(R.id.hightolow)

            lowtohigh!!.setOnClickListener {

                if (apparels == 2) {
                    getsortlowtohighApparelsList()
                    dialog.dismiss()
                } else if (apparels == 1) {
                    getsortElectronicsList()
                    dialog.dismiss()

                } else if (apparels == 6) {
                    getsortCategoriesList()
                    dialog.dismiss()

                }
            }

            hightolow!!.setOnClickListener {

                if (apparels == 2) {
                    getsorthightolowApparelsList()
                    dialog.dismiss()
                } else if (apparels == 1) {
                    getsorthightolowElectronicsList()
                    dialog.dismiss()

                } else if (apparels == 6) {
                    getsorthightolowCategoriesList()
                    dialog.dismiss()

                }
            }

            dialog.show()
        }
        progressBarproduct.visibility = View.VISIBLE
        Log.e("apparels indetails", "" + apparels)

        if (apparels == 1) {

            getuserElectronicsList()
            gvuserelectronics.visibility = View.VISIBLE
            gvuserproductdetsils.visibility = View.GONE
            gvsearchproduct.visibility = View.GONE
            productname_text.text = elec_catname
            producttitle.text = elec_catname
            gvfitness.visibility = View.GONE


        } else if (apparels == 2) {
            getuserApparelsList()
            productname_text.text = apparelcat_name
            producttitle.text = apparelcat_name

            gvuserelectronics.visibility = View.GONE
            gvsearchproduct.visibility = View.GONE
            gvuserproductdetsils.visibility = View.VISIBLE
            gvfitness.visibility = View.GONE

        } else if (apparels == 6) {
            getuserCategoriesList()
            productname_text.text = category_catname
            producttitle.text = category_catname

            gvuserelectronics.visibility = View.GONE
            gvsearchproduct.visibility = View.GONE
            gvusercategory.visibility = View.VISIBLE
            gvfitness.visibility = View.GONE

            gvuserproductdetsils.visibility = View.GONE
        } else if (apparels == 3) {
            getuserSearchList()
            productname_text.text = searchcat_name
            producttitle.text = searchcat_name

            gvuserelectronics.visibility = View.GONE
            gvusercategory.visibility = View.GONE
            gvuserproductdetsils.visibility = View.GONE
            gvsearchproduct.visibility = View.VISIBLE
            gvfitness.visibility = View.GONE

        } else if (apparels == 4) {
            getuserbrndsList()
            productname_text.text = brand_name
            producttitle.text = brand_name

            gvuserelectronics.visibility = View.GONE
            gvusercategory.visibility = View.GONE
            gvuserproductdetsils.visibility = View.GONE
            gvsearchproduct.visibility = View.GONE
            gvfitness.visibility = View.VISIBLE
        }


    }

    private fun getuserApparelsList() {

        if (NetWorkConection.isNEtworkConnected(this)) {

            //Set the Adapter to the RecyclerView//


            var apiServices = APIClient.client.create(Api::class.java)
            apparelscatid = sharedPreferences.getString("apparelcat_id", "").toString()

            Log.e("apprelscatid", apparelscatid)
            val call = apiServices.getApparealsProductList(apparelscatid.toString())

            call.enqueue(object : Callback<ApparelsProductsListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<ApparelsProductsListResponse>,
                    response: Response<ApparelsProductsListResponse>
                ) {

                    progressBarproduct.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//

                        listOfapparels = response.body()?.response!!

                        if (listOfapparels.isEmpty()) {
                            noproducts.visibility = View.VISIBLE
                            gvuserelectronics.visibility = View.GONE
                            gvusercategory.visibility = View.GONE
                            gvsearchproduct.visibility = View.GONE
                            gvuserproductdetsils.visibility = View.GONE
                            gvfitness.visibility = View.GONE
                        } else {
                            noproducts.visibility = View.GONE


                            gvuserelectronics.visibility = View.GONE
                            gvusercategory.visibility = View.GONE
                            gvfitness.visibility = View.GONE

                            gvsearchproduct.visibility = View.GONE
                            gvuserproductdetsils.visibility = View.VISIBLE

                            val adapter =
                                UserProduct_Adapter(
                                    this@UserProdctList_Screen,
                                    listOfapparels as ArrayList<ApparelsProductResponse>
                                )

                            gvuserproductdetsils.adapter = adapter

                            gvuserproductdetsils.setOnItemClickListener { adapterView, parent, position, l ->

                                var apparels_id = adapter.arrayListImage.get(position).apparels_id
                                var apparelscat_id =
                                    adapter.arrayListImage.get(position).apparel_cat_id
                                val editor = sharedPreferences.edit()
                                editor.putString("apparels_id", apparels_id.toString())
                                editor.putString("apparelscat_id", apparelscat_id.toString())
                                editor.putInt("product_indetails", 11)
                                editor.commit()
//
                                startActivity(
                                    Intent(
                                        this@UserProdctList_Screen,
                                        ProductDetails_Activity::class.java
                                    )
                                )
                                finish()
                            }
                        }
//                        val itemDecorator2 = VerticalSpacingItemDecorator(20)
//                        gvsportslist.addItemDecoration(itemDecorator2)
                    }
                }

                override fun onFailure(
                    call: Call<ApparelsProductsListResponse>?,
                    t: Throwable?
                ) {
                    progressBarproduct.visibility = View.GONE
                    Log.e(ContentValues.TAG, t.toString())
                }
            })


        } else {

            Toast.makeText(this, "Please Check your internet", Toast.LENGTH_LONG).show()
        }


    }

    private fun getsortlowtohighApparelsList() {

        if (NetWorkConection.isNEtworkConnected(this)) {

            //Set the Adapter to the RecyclerView//


            var apiServices = APIClient.client.create(Api::class.java)
            apparelscatid = sharedPreferences.getString("apparelcat_id", "").toString()

            Log.e("apprelscatid", apparelscatid)
            val call = apiServices.sortapparelslist(apparelscatid.toString(), "", "ASC")

            call.enqueue(object : Callback<ApparelsProductsListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<ApparelsProductsListResponse>,
                    response: Response<ApparelsProductsListResponse>
                ) {

                    progressBarproduct.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//

                        listOfapparels = response.body()?.response!!

                        if (listOfapparels.isEmpty()) {
                            noproducts.visibility = View.VISIBLE
                            gvuserelectronics.visibility = View.GONE
                            gvusercategory.visibility = View.GONE
                            gvsearchproduct.visibility = View.GONE
                            gvfitness.visibility = View.GONE

                            gvuserproductdetsils.visibility = View.GONE
                        } else {
                            noproducts.visibility = View.GONE
                            gvfitness.visibility = View.GONE

                            gvuserelectronics.visibility = View.GONE
                            gvusercategory.visibility = View.GONE
                            gvsearchproduct.visibility = View.GONE
                            gvuserproductdetsils.visibility = View.VISIBLE

                            val adapter =
                                UserProduct_Adapter(
                                    this@UserProdctList_Screen,
                                    listOfapparels as ArrayList<ApparelsProductResponse>
                                )

                            gvuserproductdetsils.adapter = adapter

                            gvuserproductdetsils.setOnItemClickListener { adapterView, parent, position, l ->

                                var apparels_id = adapter.arrayListImage.get(position).apparels_id
                                val editor = sharedPreferences.edit()
                                editor.putString("apparels_id", apparels_id.toString())
                                editor.putInt("product_indetails", 11)
                                editor.commit()
//
                                startActivity(
                                    Intent(
                                        this@UserProdctList_Screen,
                                        ProductDetails_Activity::class.java
                                    )
                                )
                                finish()
                            }
                        }
//                        val itemDecorator2 = VerticalSpacingItemDecorator(20)
//                        gvsportslist.addItemDecoration(itemDecorator2)
                    }
                }

                override fun onFailure(
                    call: Call<ApparelsProductsListResponse>?,
                    t: Throwable?
                ) {
                    progressBarproduct.visibility = View.GONE
                    Log.e(ContentValues.TAG, t.toString())
                }
            })


        } else {

            Toast.makeText(this, "Please Check your internet", Toast.LENGTH_LONG).show()
        }


    }

    private fun getsorthightolowApparelsList() {

        if (NetWorkConection.isNEtworkConnected(this)) {

            //Set the Adapter to the RecyclerView//


            var apiServices = APIClient.client.create(Api::class.java)
            apparelscatid = sharedPreferences.getString("apparelcat_id", "").toString()

            Log.e("apprelscatid", apparelscatid)
            val call = apiServices.sortapparelslist(apparelscatid.toString(), "", "DESC")

            call.enqueue(object : Callback<ApparelsProductsListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<ApparelsProductsListResponse>,
                    response: Response<ApparelsProductsListResponse>
                ) {

                    progressBarproduct.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//

                        listOfapparels = response.body()?.response!!

                        if (listOfapparels.isEmpty()) {
                            noproducts.visibility = View.VISIBLE
                            gvuserelectronics.visibility = View.GONE
                            gvusercategory.visibility = View.GONE
                            gvsearchproduct.visibility = View.GONE
                            gvfitness.visibility = View.GONE

                            gvuserproductdetsils.visibility = View.GONE
                        } else {
                            noproducts.visibility = View.GONE

                            gvfitness.visibility = View.GONE

                            gvuserelectronics.visibility = View.GONE
                            gvusercategory.visibility = View.GONE
                            gvsearchproduct.visibility = View.GONE
                            gvuserproductdetsils.visibility = View.VISIBLE

                            val adapter =
                                UserProduct_Adapter(
                                    this@UserProdctList_Screen,
                                    listOfapparels as ArrayList<ApparelsProductResponse>
                                )

                            gvuserproductdetsils.adapter = adapter

                            gvuserproductdetsils.setOnItemClickListener { adapterView, parent, position, l ->

                                var apparels_id = adapter.arrayListImage.get(position).apparels_id
                                val editor = sharedPreferences.edit()
                                editor.putString("apparels_id", apparels_id.toString())
                                editor.putInt("product_indetails", 11)
                                editor.commit()
//
                                startActivity(
                                    Intent(
                                        this@UserProdctList_Screen,
                                        ProductDetails_Activity::class.java
                                    )
                                )
                                finish()
                            }
                        }
//                        val itemDecorator2 = VerticalSpacingItemDecorator(20)
//                        gvsportslist.addItemDecoration(itemDecorator2)
                    }
                }

                override fun onFailure(
                    call: Call<ApparelsProductsListResponse>?,
                    t: Throwable?
                ) {
                    progressBarproduct.visibility = View.GONE
                    Log.e(ContentValues.TAG, t.toString())
                }
            })


        } else {

            Toast.makeText(this, "Please Check your internet", Toast.LENGTH_LONG).show()
        }


    }


    private fun getuserCategoriesList() {

        if (NetWorkConection.isNEtworkConnected(this)) {

            //Set the Adapter to the RecyclerView//


            var apiServices = APIClient.client.create(Api::class.java)
            subcategories_id = sharedPreferences.getString("subcategories_id", "").toString()

            Log.e("response subcatid", subcategories_id)
            val call = apiServices.getSubCategoryList(subcategories_id)

            call.enqueue(object : Callback<SubCategoryListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<SubCategoryListResponse>,
                    response: Response<SubCategoryListResponse>
                ) {

                    progressBarproduct.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//

                        listOfcategories = response.body()?.response!!

                        if (listOfcategories.isEmpty()) {
                            noproducts.visibility = View.VISIBLE
                            gvuserelectronics.visibility = View.GONE
                            gvuserproductdetsils.visibility = View.GONE
                            gvsearchproduct.visibility = View.GONE
                            gvusercategory.visibility = View.GONE
                            gvfitness.visibility = View.GONE

                        } else {
                            noproducts.visibility = View.GONE

                            gvuserelectronics.visibility = View.GONE
                            gvuserproductdetsils.visibility = View.GONE
                            gvsearchproduct.visibility = View.GONE
                            gvfitness.visibility = View.GONE

                            gvusercategory.visibility = View.VISIBLE

                            val adapter =
                                Product_Adapter(
                                    this@UserProdctList_Screen,
                                    listOfcategories as ArrayList<SubCategoryResponse>
                                )

                            gvusercategory.adapter = adapter

                            gvusercategory.setOnItemClickListener { adapterView, parent, position, l ->

                                var product_id = adapter.arrayListImage.get(position).products_id
                                var sub_categories_id =
                                    adapter.arrayListImage.get(position).sub_categories_id
                                val editor = sharedPreferences.edit()
                                editor.putString("product_id", product_id.toString())
                                editor.putString("sub_categories_id", sub_categories_id.toString())
                                editor.putInt("product_indetails", 14)
                                editor.commit()
//
                                startActivity(
                                    Intent(
                                        this@UserProdctList_Screen,
                                        ProductDetails_Activity::class.java
                                    )
                                )
                                finish()
                            }
                        }
//                        val itemDecorator2 = VerticalSpacingItemDecorator(20)
//                        gvsportslist.addItemDecoration(itemDecorator2)
                    }
                }

                override fun onFailure(
                    call: Call<SubCategoryListResponse>?,
                    t: Throwable?
                ) {
                    progressBarproduct.visibility = View.GONE
                    Log.e(ContentValues.TAG, t.toString())
                }
            })


        } else {

            Toast.makeText(this, "Please Check your internet", Toast.LENGTH_LONG).show()
        }


    }

    private fun getsortCategoriesList() {

        if (NetWorkConection.isNEtworkConnected(this)) {

            //Set the Adapter to the RecyclerView//


            var apiServices = APIClient.client.create(Api::class.java)
            subcategories_id = sharedPreferences.getString("subcategories_id", "").toString()

            Log.e("response subcatid", subcategories_id)
            val call = apiServices.getsortSubCategoryList(subcategories_id, "", "ASC")

            call.enqueue(object : Callback<SubCategoryListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<SubCategoryListResponse>,
                    response: Response<SubCategoryListResponse>
                ) {

                    progressBarproduct.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//

                        listOfcategories = response.body()?.response!!

                        if (listOfcategories.isEmpty()) {

                            noproducts.visibility = View.VISIBLE
                            gvuserelectronics.visibility = View.GONE
                            gvuserproductdetsils.visibility = View.GONE
                            gvsearchproduct.visibility = View.GONE
                            gvusercategory.visibility = View.GONE
                            gvfitness.visibility = View.GONE


                        } else {


                            noproducts.visibility = View.GONE

                            gvuserelectronics.visibility = View.GONE
                            gvuserproductdetsils.visibility = View.GONE
                            gvsearchproduct.visibility = View.GONE
                            gvusercategory.visibility = View.VISIBLE
                            gvfitness.visibility = View.GONE


                            val adapter =
                                Product_Adapter(
                                    this@UserProdctList_Screen,
                                    listOfcategories as ArrayList<SubCategoryResponse>
                                )

                            gvusercategory.adapter = adapter

                            gvusercategory.setOnItemClickListener { adapterView, parent, position, l ->

                                var product_id = adapter.arrayListImage.get(position).products_id
                                val editor = sharedPreferences.edit()
                                editor.putString("product_id", product_id.toString())
                                editor.putInt("product_indetails", 14)
                                editor.commit()
//
                                startActivity(
                                    Intent(
                                        this@UserProdctList_Screen,
                                        ProductDetails_Activity::class.java
                                    )
                                )
                                finish()
                            }
                        }
//                        val itemDecorator2 = VerticalSpacingItemDecorator(20)
//                        gvsportslist.addItemDecoration(itemDecorator2)
                    }
                }

                override fun onFailure(
                    call: Call<SubCategoryListResponse>?,
                    t: Throwable?
                ) {
                    progressBarproduct.visibility = View.GONE
                    Log.e(ContentValues.TAG, t.toString())
                }
            })


        } else {

            Toast.makeText(this, "Please Check your internet", Toast.LENGTH_LONG).show()
        }


    }

    private fun getsorthightolowCategoriesList() {

        if (NetWorkConection.isNEtworkConnected(this)) {

            //Set the Adapter to the RecyclerView//


            var apiServices = APIClient.client.create(Api::class.java)
            subcategories_id = sharedPreferences.getString("subcategories_id", "").toString()

            Log.e("response subcatid", subcategories_id)
            val call = apiServices.getsortSubCategoryList(subcategories_id, "", "DESC")

            call.enqueue(object : Callback<SubCategoryListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<SubCategoryListResponse>,
                    response: Response<SubCategoryListResponse>
                ) {

                    progressBarproduct.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//

                        listOfcategories = response.body()?.response!!

                        if (listOfcategories.isEmpty()) {
                            noproducts.visibility = View.VISIBLE
                            gvuserelectronics.visibility = View.GONE
                            gvuserproductdetsils.visibility = View.GONE
                            gvsearchproduct.visibility = View.GONE
                            gvusercategory.visibility = View.GONE
                            gvfitness.visibility = View.GONE

                        } else {


                            noproducts.visibility = View.GONE

                            gvuserelectronics.visibility = View.GONE
                            gvuserproductdetsils.visibility = View.GONE
                            gvsearchproduct.visibility = View.GONE
                            gvusercategory.visibility = View.VISIBLE
                            gvfitness.visibility = View.GONE

                            val adapter =
                                Product_Adapter(
                                    this@UserProdctList_Screen,
                                    listOfcategories as ArrayList<SubCategoryResponse>
                                )

                            gvusercategory.adapter = adapter

                            gvusercategory.setOnItemClickListener { adapterView, parent, position, l ->

                                var product_id = adapter.arrayListImage.get(position).products_id
                                val editor = sharedPreferences.edit()
                                editor.putString("product_id", product_id.toString())
                                editor.putInt("product_indetails", 14)
                                editor.commit()
//
                                startActivity(
                                    Intent(
                                        this@UserProdctList_Screen,
                                        ProductDetails_Activity::class.java
                                    )
                                )
                                finish()
                            }
                        }
//                        val itemDecorator2 = VerticalSpacingItemDecorator(20)
//                        gvsportslist.addItemDecoration(itemDecorator2)
                    }
                }

                override fun onFailure(
                    call: Call<SubCategoryListResponse>?,
                    t: Throwable?
                ) {
                    progressBarproduct.visibility = View.GONE
                    Log.e(ContentValues.TAG, t.toString())
                }
            })


        } else {

            Toast.makeText(this, "Please Check your internet", Toast.LENGTH_LONG).show()
        }


    }

    private fun getuserSearchList() {

        if (NetWorkConection.isNEtworkConnected(this)) {

            //Set the Adapter to the RecyclerView//


            var apiServices = APIClient.client.create(Api::class.java)
            var catid = sharedPreferences.getInt("category_id", 0).toString()

            Log.e("response catid", catid)
            val call = apiServices.getwordSearchrelatedlist(catid.toString())

            call.enqueue(object : Callback<SearchRelatedListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<SearchRelatedListResponse>,
                    response: Response<SearchRelatedListResponse>
                ) {

                    progressBarproduct.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//


                        listOfrelatedsearch = response.body()?.response!!



                        if (listOfrelatedsearch.equals("") || (listOfrelatedsearch.isEmpty())) {
//                            Toast.makeText(
//                                this@UserProdctDetails_Screen,
//                                "No data",
//                                Toast.LENGTH_LONG
//                            ).show()
                            noproducts.visibility = View.VISIBLE
                            gvuserelectronics.visibility = View.GONE
                            gvuserproductdetsils.visibility = View.GONE
                            gvsearchproduct.visibility = View.GONE
                            gvusercategory.visibility = View.GONE
                            gvfitness.visibility = View.GONE


                        } else {
                            gvsearchproduct.visibility = View.VISIBLE
                            gvuserelectronics.visibility = View.GONE
                            gvusercategory.visibility = View.GONE
                            gvfitness.visibility = View.GONE

                            gvuserproductdetsils.visibility = View.GONE
                            noproducts.visibility = View.GONE

                            val adapter =
                                SearchProduct_Adapter(
                                    this@UserProdctList_Screen,
                                    listOfrelatedsearch as ArrayList<SearchRelatedResponse>
                                )

                            gvsearchproduct.adapter = adapter

                        }
//                        gvusercategory.setOnItemClickListener { adapterView, parent, position, l ->
//
//                            var product_id = adapter.arrayListImage.get(position).products_id
//                            val editor = sharedPreferences.edit()
//                            editor.putString("product_id", product_id.toString())
//                            editor.putInt("product_indetails", 14)
//                            editor.commit()
////
//                            startActivity(
//                                Intent(
//                                    this@UserProdctDetails_Screen,
//                                    ProductDetails_Activity::class.java
//                                )
//                            )
//
//                        }
//                        val itemDecorator2 = VerticalSpacingItemDecorator(20)
//                        gvsportslist.addItemDecoration(itemDecorator2)
                    }
                }

                override fun onFailure(
                    call: Call<SearchRelatedListResponse>?,
                    t: Throwable?
                ) {
                    progressBarproduct.visibility = View.GONE
                    Log.e(ContentValues.TAG, t.toString())
                }
            })


        } else {

            Toast.makeText(this, "Please Check your internet", Toast.LENGTH_LONG).show()
        }


    }

    private fun getuserbrndsList() {

        if (NetWorkConection.isNEtworkConnected(this)) {

            //Set the Adapter to the RecyclerView//

            brands_id = sharedPreferences.getString("brands_id", "").toString()

            var apiServices = APIClient.client.create(Api::class.java)


            val call = apiServices.getproductsList(brands_id.toString())

            call.enqueue(object : Callback<BrandsProductsListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<BrandsProductsListResponse>,
                    response: Response<BrandsProductsListResponse>
                ) {

                    progressBarproduct.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//

                        listOfbrands = response.body()?.response!!

                        if (listOfbrands.isEmpty()) {
                            noproducts.visibility = View.VISIBLE
                            gvuserproductdetsils.visibility = View.GONE
                            gvusercategory.visibility = View.GONE
                            gvsearchproduct.visibility = View.GONE
                            gvuserelectronics.visibility = View.GONE
                            gvfitness.visibility = View.GONE
                        } else {
                            noproducts.visibility = View.GONE


                            gvuserproductdetsils.visibility = View.GONE
                            gvusercategory.visibility = View.GONE
                            gvsearchproduct.visibility = View.GONE
                            gvuserelectronics.visibility = View.GONE
                            gvuserelectronics.visibility = View.GONE
                            gvfitness.visibility = View.VISIBLE


                            val adapter =
                                UserBrands_Adapter(
                                    this@UserProdctList_Screen,
                                    listOfbrands as ArrayList<BrandsProductsDeResponse>
                                )

                            gvfitness.adapter = adapter

                            gvfitness.setOnItemClickListener { adapterView, parent, position, l ->

                                var product_id =
                                    adapter.arrayListImage.get(position).products_id
                                var cat_id =
                                    adapter.arrayListImage.get(position).brands_id
                                val editor = sharedPreferences.edit()
                                editor.putInt("product_id", product_id)
                                editor.putInt("cat_id", cat_id)
                                editor.putInt("product_indetails", 18)
                                editor.commit()
//
                                startActivity(
                                    Intent(
                                        this@UserProdctList_Screen,
                                        ProductDetails_Activity::class.java
                                    )
                                )
                                finish()

                            }
                        }
                    }
                }

                override fun onFailure(
                    call: Call<BrandsProductsListResponse>?,
                    t: Throwable?
                ) {
                    progressBarproduct.visibility = View.GONE
                    Log.e(ContentValues.TAG, t.toString())
                }
            })


        } else {

            Toast.makeText(this, "Please Check your internet", Toast.LENGTH_LONG).show()
        }


    }


    private fun getuserElectronicsList() {

        if (NetWorkConection.isNEtworkConnected(this)) {

            //Set the Adapter to the RecyclerView//

            elec_catid = sharedPreferences.getString("electroniccatid", "").toString()

            var apiServices = APIClient.client.create(Api::class.java)


            val call = apiServices.getElectronicsProductsList(elec_catid)

            call.enqueue(object : Callback<ElectronicsProductListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<ElectronicsProductListResponse>,
                    response: Response<ElectronicsProductListResponse>
                ) {

                    progressBarproduct.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//

                        listOfelectronics = response.body()?.response!!

                        if (listOfelectronics.isEmpty()) {
                            noproducts.visibility = View.VISIBLE
                            gvuserproductdetsils.visibility = View.GONE
                            gvusercategory.visibility = View.GONE
                            gvsearchproduct.visibility = View.GONE
                            gvuserelectronics.visibility = View.GONE
                            gvfitness.visibility = View.GONE

                        } else {
                            noproducts.visibility = View.GONE
                            gvfitness.visibility = View.GONE


                            gvuserproductdetsils.visibility = View.GONE
                            gvusercategory.visibility = View.GONE
                            gvsearchproduct.visibility = View.GONE
                            gvuserelectronics.visibility = View.VISIBLE


                            val adapter =
                                UserElectronic_Adapter(
                                    this@UserProdctList_Screen,
                                    listOfelectronics as ArrayList<ElectronicsProductResponse>
                                )

                            gvuserelectronics.adapter = adapter

                            gvuserelectronics.setOnItemClickListener { adapterView, parent, position, l ->

                                var electrioncs_id =
                                    adapter.arrayListImage.get(position).electronics_id
                                var electronocs_cat_id =
                                    adapter.arrayListImage.get(position).elec_categories_id
                                val editor = sharedPreferences.edit()
                                editor.putInt("electrioncs_id", electrioncs_id)
                                editor.putInt("electronocs_cat_id", electronocs_cat_id)
                                editor.putInt("product_indetails", 12)
                                editor.commit()
//
                                startActivity(
                                    Intent(
                                        this@UserProdctList_Screen,
                                        ProductDetails_Activity::class.java
                                    )
                                )
                                finish()

                            }
                        }
                    }
                }

                override fun onFailure(
                    call: Call<ElectronicsProductListResponse>?,
                    t: Throwable?
                ) {
                    progressBarproduct.visibility = View.GONE
                    Log.e(ContentValues.TAG, t.toString())
                }
            })


        } else {

            Toast.makeText(this, "Please Check your internet", Toast.LENGTH_LONG).show()
        }


    }

    private fun getsortElectronicsList() {

        if (NetWorkConection.isNEtworkConnected(this)) {

            //Set the Adapter to the RecyclerView//

            elec_catid = sharedPreferences.getString("electroniccatid", "").toString()

            var apiServices = APIClient.client.create(Api::class.java)


            val call = apiServices.getsortElectronicsList(elec_catid, "", "ASC")

            call.enqueue(object : Callback<ElectronicsProductListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<ElectronicsProductListResponse>,
                    response: Response<ElectronicsProductListResponse>
                ) {

                    progressBarproduct.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//

                        listOfelectronics = response.body()?.response!!

                        if (listOfelectronics.isEmpty()) {
                            noproducts.visibility = View.VISIBLE
                            gvuserproductdetsils.visibility = View.GONE
                            gvusercategory.visibility = View.GONE
                            gvsearchproduct.visibility = View.GONE
                            gvuserelectronics.visibility = View.GONE
                            gvfitness.visibility = View.GONE

                        } else {
                            noproducts.visibility = View.GONE

                            gvfitness.visibility = View.GONE

                            gvuserproductdetsils.visibility = View.GONE
                            gvusercategory.visibility = View.GONE
                            gvsearchproduct.visibility = View.GONE
                            gvuserelectronics.visibility = View.VISIBLE


                            val adapter =
                                UserElectronic_Adapter(
                                    this@UserProdctList_Screen,
                                    listOfelectronics as ArrayList<ElectronicsProductResponse>
                                )

                            gvuserelectronics.adapter = adapter

                            gvuserelectronics.setOnItemClickListener { adapterView, parent, position, l ->

                                var electrioncs_id =
                                    adapter.arrayListImage.get(position).electronics_id
                                val editor = sharedPreferences.edit()
                                editor.putInt("electrioncs_id", electrioncs_id)
                                editor.putInt("product_indetails", 12)
                                editor.commit()
//
                                startActivity(
                                    Intent(
                                        this@UserProdctList_Screen,
                                        ProductDetails_Activity::class.java
                                    )
                                )
                                finish()

                            }
                        }
                    }
                }

                override fun onFailure(
                    call: Call<ElectronicsProductListResponse>?,
                    t: Throwable?
                ) {
                    progressBarproduct.visibility = View.GONE
                    Log.e(ContentValues.TAG, t.toString())
                }
            })


        } else {

            Toast.makeText(this, "Please Check your internet", Toast.LENGTH_LONG).show()
        }


    }

    private fun getsorthightolowElectronicsList() {

        if (NetWorkConection.isNEtworkConnected(this)) {

            //Set the Adapter to the RecyclerView//

            elec_catid = sharedPreferences.getString("electroniccatid", "").toString()

            var apiServices = APIClient.client.create(Api::class.java)


            val call = apiServices.getsortElectronicsList(elec_catid, "", "DESC")

            call.enqueue(object : Callback<ElectronicsProductListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<ElectronicsProductListResponse>,
                    response: Response<ElectronicsProductListResponse>
                ) {

                    progressBarproduct.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//

                        listOfelectronics = response.body()?.response!!

                        if (listOfelectronics.isEmpty()) {
                            noproducts.visibility = View.VISIBLE
                            gvuserproductdetsils.visibility = View.GONE
                            gvusercategory.visibility = View.GONE
                            gvsearchproduct.visibility = View.GONE
                            gvfitness.visibility = View.GONE

                            gvuserelectronics.visibility = View.GONE
                        } else {
                            noproducts.visibility = View.GONE
                            gvfitness.visibility = View.GONE


                            gvuserproductdetsils.visibility = View.GONE
                            gvusercategory.visibility = View.GONE
                            gvsearchproduct.visibility = View.GONE
                            gvuserelectronics.visibility = View.VISIBLE


                            val adapter =
                                UserElectronic_Adapter(
                                    this@UserProdctList_Screen,
                                    listOfelectronics as ArrayList<ElectronicsProductResponse>
                                )

                            gvuserelectronics.adapter = adapter

                            gvuserelectronics.setOnItemClickListener { adapterView, parent, position, l ->

                                var electrioncs_id =
                                    adapter.arrayListImage.get(position).electronics_id
                                val editor = sharedPreferences.edit()
                                editor.putInt("electrioncs_id", electrioncs_id)
                                editor.putInt("product_indetails", 12)
                                editor.commit()
//
                                startActivity(
                                    Intent(
                                        this@UserProdctList_Screen,
                                        ProductDetails_Activity::class.java
                                    )
                                )
                                finish()

                            }
                        }
                    }
                }

                override fun onFailure(
                    call: Call<ElectronicsProductListResponse>?,
                    t: Throwable?
                ) {
                    progressBarproduct.visibility = View.GONE
                    Log.e(ContentValues.TAG, t.toString())
                }
            })


        } else {

            Toast.makeText(this, "Please Check your internet", Toast.LENGTH_LONG).show()
        }


    }


}