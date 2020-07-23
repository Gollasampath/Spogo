package com.pss.spogoo

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.GridView
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pss.spogoo.adapter.AddGripProduct_Adapter
import com.pss.spogoo.api.APIClient
import com.pss.spogoo.api.Api
import com.pss.spogoo.models.AddGripProdcutsListResponse
import com.pss.spogoo.models.AddGripProductsResponse
import com.pss.spogoo.util.NetWorkConection
import kotlinx.android.synthetic.main.servicelist_screen.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddGripList_Activity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var listOfgriplist: List<AddGripProductsResponse>

    lateinit var gvproductdetailsgridview: GridView
    lateinit var backcategory_image: ImageView
    lateinit var progressBaraddgrip: ProgressBar
    lateinit var repaircategoryid: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.servicelist_screen)


        gvproductdetailsgridview = findViewById<GridView>(R.id.servicelist) as GridView
        backcategory_image = toolbar.findViewById(R.id.backcategory_image) as ImageView
        progressBaraddgrip = findViewById<ProgressBar>(R.id.progressBaraddgrip) as ProgressBar

        sharedPreferences =
            getSharedPreferences(
                "loginprefs",
                Context.MODE_PRIVATE
            )

        backcategory_image.setOnClickListener {
            startActivity(Intent(Intent(this, ServiceDetails_Activity::class.java)))
            finish()
        }
        getGripProductsList()

    }


    private fun getGripProductsList() {

        if (NetWorkConection.isNEtworkConnected(this)) {

            //Set the Adapter to the RecyclerView//


            var apiServices = APIClient.client.create(Api::class.java)
            repaircategoryid =
                sharedPreferences.getString("repaircategoryid", "").toString()


            val call = apiServices.getgripproductslist(repaircategoryid)

            call.enqueue(object : Callback<AddGripProdcutsListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<AddGripProdcutsListResponse>,
                    response: Response<AddGripProdcutsListResponse>
                ) {

                    progressBaraddgrip.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//

                        listOfgriplist = response.body()?.response!!

//
//                        if (listOfgriplist.isEmpty()) {
//
//                            noproducts.visibility = View.VISIBLE
//                            gvrepairproductdetsils.visibility = View.GONE
//
//                        } else {

//                            noproducts.visibility = View.GONE

                        val adapter =
                            AddGripProduct_Adapter(
                                this@AddGripList_Activity,
                                listOfgriplist as ArrayList<AddGripProductsResponse>
                            )

                        gvproductdetailsgridview.adapter = adapter

                        gvproductdetailsgridview.setOnItemClickListener { adapterView, parent, position, l ->

                            var repair_grip_id = adapter.arrayListImage.get(position).repair_grip_id
                            val editor = sharedPreferences.edit()
                            editor.putString("repair_grip_id", repair_grip_id.toString())
                            editor.putInt("product_indetails", 13)
                            editor.commit()
//
                            startActivity(
                                Intent(
                                    this@AddGripList_Activity,
                                    ServiceDetails_Activity::class.java
                                )
                            )
                            finish()

                        }

                    }
                }

                override fun onFailure(
                    call: Call<AddGripProdcutsListResponse>?,
                    t: Throwable?
                ) {
                    progressBaraddgrip.visibility = View.GONE
                    Log.e(ContentValues.TAG, t.toString())
                }
            })


        } else {

            Toast.makeText(this, "Please Check your internet", Toast.LENGTH_LONG).show()
        }


    }


}
