package com.pss.spogoo.ui.Category_Screen

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
import com.pss.spogoo.R
import com.pss.spogoo.Shop_Activity
import com.pss.spogoo.adapter.ShopCategory_Adapter
import com.pss.spogoo.api.APIClient
import com.pss.spogoo.api.Api
import com.pss.spogoo.models.ShopAllCategoryResponse
import com.pss.spogoo.models.ShopListResponse
import com.pss.spogoo.util.NetWorkConection
import com.pss.spogoo.util.VerticalSpacingItemDecorator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryMainActivity : AppCompatActivity() {

    lateinit var progressBarcategory: ProgressBar
    var subcategory_id: Int = 0
    var categories_id: Int = 0

    lateinit var shopcaterecyclerView: RecyclerView
    lateinit var backcategory_image: ImageView
    lateinit var listOfallcategories: List<ShopAllCategoryResponse>
    lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.category_screen)

        progressBarcategory = findViewById(R.id.progressBarcategory)
        backcategory_image = findViewById<ImageView>(R.id.backcategory_image)
        backcategory_image = findViewById<ImageView>(R.id.backcategory_image)

        backcategory_image.setOnClickListener {
            startActivity(Intent(this, Shop_Activity::class.java))
            finish()

        }

        shopcaterecyclerView = findViewById<RecyclerView>(R.id.shopcaterecyclerView) as RecyclerView

        sharedPreferences = getSharedPreferences("loginprefs", Context.MODE_PRIVATE)

        //adding a layoutmanager
        shopcaterecyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)


        getAllCategoriesList()


    }


    private fun getAllCategoriesList() {

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

                    progressBarcategory.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//

                        listOfallcategories = response.body()?.response!!


                        //creating our adapter
                        val adapter =

                            ShopCategory_Adapter(
                                this@CategoryMainActivity,
                                listOfallcategories as ArrayList<ShopAllCategoryResponse>
                            )


                        //now adding the adapter to recyclerview
                        shopcaterecyclerView.adapter = adapter

                        adapter.notifyDataSetChanged()

//                        shopcaterecyclerView.addOnItemClickListener(object :
//                            OnItemClickListener {
//                            override fun onItemClicked(position: Int, view: View) {
//                                // Your logic
//                                val subcatgeory =
//                                    adapter?.userList!!.get(position).sub_category_list.map {
//                                        subcategory_id = it.sub_categories_id
//                                        categories_id = it.categories_id
//
//                                        Log.e("subcat", "" + subcategory_id)
//                                        Log.e("cat id", "" + categories_id)
//
//                                    }
//
//                            }
//                        })

                        val itemDecorator2 = VerticalSpacingItemDecorator(20)
                        shopcaterecyclerView.addItemDecoration(itemDecorator2)
                    }
                }

                override fun onFailure(call: Call<ShopListResponse>?, t: Throwable?) {
                    progressBarcategory.visibility = View.GONE
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
