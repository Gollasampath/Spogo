package com.pss.spogoo.adapter

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pss.spogoo.R
import com.pss.spogoo.api.APIClient
import com.pss.spogoo.api.Api
import com.pss.spogoo.models.ShopAllCategoryResponse
import com.pss.spogoo.models.SubProductListResponse
import com.pss.spogoo.models.SubProductResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShopCategory_Adapter(var context: Context, val userList: ArrayList<ShopAllCategoryResponse>) :
    RecyclerView.Adapter<ShopCategory_Adapter.ViewHolder>() {
    lateinit var subuserslist: List<SubProductResponse>


    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShopCategory_Adapter.ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.shopcategory_adapter, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    @SuppressLint("WrongConstant")
    override fun onBindViewHolder(holder: ShopCategory_Adapter.ViewHolder, position: Int) {


        holder.shopcattext.text = userList.get(position).category_name

        var categoryid = userList.get(position).categories_id
        Log.e("categoryname", "" + userList.get(position).category_name)
        Log.e("categoryid", "" + userList.get(position).categories_id)

        Glide.with(context).load(userList.get(position).category_icon)
            .apply(RequestOptions().centerCrop())
            .error(R.drawable.logo)
            .into(holder.shopcatimage)
        holder.linearcategory.setOnClickListener {


            var apiServices = APIClient.client.create(Api::class.java)


            val call = apiServices.getusersubproductList(categoryid.toString())

            call.enqueue(object : Callback<SubProductListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<SubProductListResponse>,
                    response: Response<SubProductListResponse>
                ) {

//                    progressBarcategory.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//


                        subuserslist = response.body()?.response!!
//                        subcat_list = response.body()?.response!!.map {
////                            subuserslist = it.sub_category_list
//                        }
                        Log.e("subprodoctlist", "" + subuserslist)


                        val childLayoutManager = LinearLayoutManager(
                            holder.shopsubcaterecyclerView.context, LinearLayout.VERTICAL, false
                        )

                        holder.shopsubcaterecyclerView.apply {
                            layoutManager = childLayoutManager
                            val adapter = ShopSubCategory_Adapter(context, subuserslist)
                            holder.shopsubcaterecyclerView.adapter = adapter


//                        childLayoutManager.initialPrefetchItemCount = 4
                        }


                    }
                }

                override fun onFailure(call: Call<SubProductListResponse>?, t: Throwable?) {
//                    progressBarcategory.visibility = View.GONE
                    Log.e(ContentValues.TAG, t.toString())
                }
            })


        }

    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val shopcatimage = itemView.findViewById(R.id.shopcatimage) as ImageView
        val shopcatarrow = itemView.findViewById(R.id.shopcatarrow) as ImageView
        val shopcattext = itemView.findViewById(R.id.shopcattext) as TextView
        val linearcategory = itemView.findViewById(R.id.linearcategory) as LinearLayout
        val shopsubcaterecyclerView =
            itemView.findViewById<RecyclerView>(R.id.subshopcaterecyclerView)


    }


}