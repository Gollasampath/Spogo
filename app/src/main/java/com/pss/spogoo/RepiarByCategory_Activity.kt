package com.pss.spogoo

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pss.spogoo.adapter.RepairCategory_Adapter
import com.pss.spogoo.api.APIClient
import com.pss.spogoo.api.Api
import com.pss.spogoo.models.RepairsCategoriesResponse
import com.pss.spogoo.models.RepairsCategoryListResponse
import com.pss.spogoo.util.NetWorkConection
import com.pss.spogoo.util.VerticalSpacingItemDecorator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class RepiarByCategory_Activity : AppCompatActivity() {
    lateinit var repaircaterecyclerView: RecyclerView
    lateinit var progressBarcategory: ProgressBar
    var subcategory_id: Int = 0
    var categories_id: Int = 0
    lateinit var listOfcategory: List<RepairsCategoriesResponse>

    lateinit var backcategory_image: ImageView
    lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.repairbycategory_screen)

        repaircaterecyclerView = findViewById<RecyclerView>(R.id.repaircaterecyclerView)

        //adding a layoutmanager
        repaircaterecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        progressBarcategory = findViewById(R.id.progressBarcategory)
        backcategory_image = findViewById<ImageView>(R.id.backcategory_image)

        backcategory_image.setOnClickListener {
            startActivity(Intent(this, Home_Screen::class.java))
            finish()
        }


        sharedPreferences = getSharedPreferences("loginprefs", Context.MODE_PRIVATE)



        getRepairsCategoriesList()


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

                    progressBarcategory.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//

                        listOfcategory = response.body()?.response!!

                        val repaircategoryadapter =
                            RepairCategory_Adapter(
                                this@RepiarByCategory_Activity,
                                listOfcategory as ArrayList<RepairsCategoriesResponse>
                            )
                        repaircaterecyclerView.adapter = repaircategoryadapter
                        repaircategoryadapter.notifyDataSetChanged()


                        val itemDecorator2 = VerticalSpacingItemDecorator(20)
                        repaircaterecyclerView.addItemDecoration(itemDecorator2)

//                        repaircaterecyclerView.addOnItemClickListener(object :
//                            OnItemClickListener {
//                            override fun onItemClicked(position: Int, view: View) {
//                                // Your logic
//
//                                val intent =
//                                    Intent(
//                                        this@RepiarByCategory_Activity,
//                                        CategoryActivity::class.java
//                                    )
//
//                                startActivity(intent)
//
//
//                            }
//                        })

                    }
                }

                override fun onFailure(call: Call<RepairsCategoryListResponse>?, t: Throwable?) {
                    progressBarcategory.visibility = View.GONE
                    Log.e(ContentValues.TAG, t.toString())
                }
            })


        } else {

            Toast.makeText(this, "Please Check your internet", Toast.LENGTH_LONG)
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
