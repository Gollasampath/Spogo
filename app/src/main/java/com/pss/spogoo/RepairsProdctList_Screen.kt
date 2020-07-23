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
import com.pss.spogoo.ServiceDetails_Activity
import com.pss.spogoo.adapter.RepairProduct_Adapter
import com.pss.spogoo.api.APIClient
import com.pss.spogoo.api.Api
import com.pss.spogoo.models.RepairsProductsListResponse
import com.pss.spogoo.models.RepairsProductsResponse
import com.pss.spogoo.models.ShoppingCart
import com.pss.spogoo.util.NetWorkConection
import io.paperdb.Paper
import kotlinx.android.synthetic.main.userproduct_screen.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RepairsProdctList_Screen : AppCompatActivity() {
    lateinit var listOfproducts: List<RepairsProductsResponse>

    lateinit var gvrepairproductdetsils: GridView

    lateinit var productname_text: TextView
    lateinit var noproducts: TextView
    lateinit var producttitle: TextView
    lateinit var sort_repair: TextView
    lateinit var productback_image: ImageView
    lateinit var gvuserelectronics: GridView

    lateinit var repairsubcategory_id: String
    lateinit var repairsubcategory_name: String
    lateinit var cart_badge: TextView
    lateinit var cartlayout: FrameLayout

    var apparels: Int = 0
    var electronics: Int = 0
    lateinit var sharedPreferences: SharedPreferences
    lateinit var progressBarproduct: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Paper.init(this)

        setContentView(R.layout.repairproduct_screen)

        gvrepairproductdetsils =
            findViewById<GridView>(R.id.gvrepairproductdetsils) as GridView
        noproducts = findViewById<TextView>(R.id.noproducts) as TextView
        sort_repair = findViewById<TextView>(R.id.sort_repair) as TextView
        productname_text = findViewById<TextView>(R.id.productname_text) as TextView
        cart_badge = toolbar.findViewById<TextView>(R.id.cart_badge) as TextView
        cartlayout = toolbar.findViewById<TextView>(R.id.cartlayout) as FrameLayout
        productback_image = toolbar.findViewById<ImageView>(R.id.productback_image) as ImageView

        progressBarproduct = findViewById<ProgressBar>(R.id.progressBarproduct) as ProgressBar

        sharedPreferences =
            getSharedPreferences("loginprefs", Context.MODE_PRIVATE)
        apparels = sharedPreferences.getInt("repair_indetails", 0)
        repairsubcategory_name = sharedPreferences.getString("repairsubcategory_name", "")!!

        if (ShoppingCart.getShoppingCartSize().toString().equals("0")) {
            cart_badge.visibility = View.GONE
        } else {
            cart_badge.visibility = View.VISIBLE

            cart_badge.text = ShoppingCart.getShoppingCartSize().toString()

        }
        cartlayout.setOnClickListener {
            startActivity(Intent(this, Cart_Screen::class.java))
        }

        productback_image.setOnClickListener {
            startActivity(Intent(this, RepiarByCategory_Activity::class.java))
        }
        progressBarproduct.visibility = View.VISIBLE
        Log.e("apparels indetails", "" + apparels)

        sort_repair.setOnClickListener {

            val dialog = BottomSheetDialog(this)
            dialog.setContentView(R.layout.sort_screen)
            var newproducts = dialog.findViewById<TextView>(R.id.newproducts)
            var lowtohigh = dialog.findViewById<TextView>(R.id.lowtohigh)
            var hightolow = dialog.findViewById<TextView>(R.id.hightolow)

            lowtohigh!!.setOnClickListener {
                getSortlowtohighRepairsProductsList()
            }
            hightolow!!.setOnClickListener {
                getSorthightolowRepairsProductsList()
            }
            dialog.show()

        }
        getRepairsProductsList()

        productname_text.text = repairsubcategory_name


    }

    private fun getRepairsProductsList() {

        if (NetWorkConection.isNEtworkConnected(this)) {

            //Set the Adapter to the RecyclerView//


            var apiServices = APIClient.client.create(Api::class.java)
            repairsubcategory_id =
                sharedPreferences.getString("repairsubcategory_id", "").toString()


            val call = apiServices.getrepairsproductsList(repairsubcategory_id)

            call.enqueue(object : Callback<RepairsProductsListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<RepairsProductsListResponse>,
                    response: Response<RepairsProductsListResponse>
                ) {

                    progressBarproduct.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//

                        listOfproducts = response.body()?.response!!


                        if (listOfproducts.isEmpty()) {

                            noproducts.visibility = View.VISIBLE
                            gvrepairproductdetsils.visibility = View.GONE

                        } else {

                            noproducts.visibility = View.GONE
                            gvrepairproductdetsils.visibility = View.VISIBLE

                            val adapter =
                                RepairProduct_Adapter(
                                    this@RepairsProdctList_Screen,
                                    listOfproducts as ArrayList<RepairsProductsResponse>
                                )

                            gvrepairproductdetsils.adapter = adapter

                            gvrepairproductdetsils.setOnItemClickListener { adapterView, parent, position, l ->

                                var repair_id = adapter.arrayListImage.get(position).repairs_id
                                val editor = sharedPreferences.edit()
                                editor.putString("repair_id", repair_id.toString())
                                editor.putInt("repair_indetails", 12)
                                editor.commit()
//
                                startActivity(
                                    Intent(
                                        this@RepairsProdctList_Screen,
                                        ServiceDetails_Activity::class.java
                                    )
                                )
                                finish()
                            }
                        }

                    }
                }

                override fun onFailure(
                    call: Call<RepairsProductsListResponse>?,
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

    private fun getSortlowtohighRepairsProductsList() {

        if (NetWorkConection.isNEtworkConnected(this)) {

            //Set the Adapter to the RecyclerView//


            var apiServices = APIClient.client.create(Api::class.java)
            repairsubcategory_id =
                sharedPreferences.getString("repairsubcategory_id", "").toString()


            val call = apiServices.getsortrepairsproductsList(repairsubcategory_id, "", "ASC")

            call.enqueue(object : Callback<RepairsProductsListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<RepairsProductsListResponse>,
                    response: Response<RepairsProductsListResponse>
                ) {

                    progressBarproduct.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//

                        listOfproducts = response.body()?.response!!


                        if (listOfproducts.isEmpty()) {

                            noproducts.visibility = View.VISIBLE
                            gvrepairproductdetsils.visibility = View.GONE

                        } else {

                            noproducts.visibility = View.GONE
                            gvrepairproductdetsils.visibility = View.VISIBLE

                            val adapter =
                                RepairProduct_Adapter(
                                    this@RepairsProdctList_Screen,
                                    listOfproducts as ArrayList<RepairsProductsResponse>
                                )

                            gvrepairproductdetsils.adapter = adapter

                            gvrepairproductdetsils.setOnItemClickListener { adapterView, parent, position, l ->

                                var repair_id = adapter.arrayListImage.get(position).repairs_id
                                val editor = sharedPreferences.edit()
                                editor.putString("repair_id", repair_id.toString())
                                editor.putInt("repair_indetails", 12)
                                editor.commit()
//
                                startActivity(
                                    Intent(
                                        this@RepairsProdctList_Screen,
                                        ServiceDetails_Activity::class.java
                                    )
                                )
                                finish()
                            }
                        }

                    }
                }

                override fun onFailure(
                    call: Call<RepairsProductsListResponse>?,
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

    private fun getSorthightolowRepairsProductsList() {

        if (NetWorkConection.isNEtworkConnected(this)) {

            //Set the Adapter to the RecyclerView//


            var apiServices = APIClient.client.create(Api::class.java)
            repairsubcategory_id =
                sharedPreferences.getString("repairsubcategory_id", "").toString()


            val call = apiServices.getsortrepairsproductsList(repairsubcategory_id, "", "DESC")

            call.enqueue(object : Callback<RepairsProductsListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<RepairsProductsListResponse>,
                    response: Response<RepairsProductsListResponse>
                ) {

                    progressBarproduct.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//

                        listOfproducts = response.body()?.response!!


                        if (listOfproducts.isEmpty()) {

                            noproducts.visibility = View.VISIBLE
                            gvrepairproductdetsils.visibility = View.GONE

                        } else {

                            noproducts.visibility = View.GONE
                            gvrepairproductdetsils.visibility = View.VISIBLE

                            val adapter =
                                RepairProduct_Adapter(
                                    this@RepairsProdctList_Screen,
                                    listOfproducts as ArrayList<RepairsProductsResponse>
                                )

                            gvrepairproductdetsils.adapter = adapter

                            gvrepairproductdetsils.setOnItemClickListener { adapterView, parent, position, l ->

                                var repair_id = adapter.arrayListImage.get(position).repairs_id
                                val editor = sharedPreferences.edit()
                                editor.putString("repair_id", repair_id.toString())
                                editor.putInt("repair_indetails", 12)
                                editor.commit()
//
                                startActivity(
                                    Intent(
                                        this@RepairsProdctList_Screen,
                                        ServiceDetails_Activity::class.java
                                    )
                                )
                                finish()
                            }
                        }

                    }
                }

                override fun onFailure(
                    call: Call<RepairsProductsListResponse>?,
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