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
import com.pss.spogoo.models.RepairsCategoriesResponse
import com.pss.spogoo.models.RepairsSubCategoriesResponse
import com.pss.spogoo.models.RepairsSubCategoryListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RepairCategory_Adapter(
    var context: Context,
    val userList: ArrayList<RepairsCategoriesResponse>
) :
    RecyclerView.Adapter<RepairCategory_Adapter.ViewHolder>() {
    lateinit var subuserslist: List<RepairsSubCategoriesResponse>
    var listopend: Boolean = false

    //this method is binding the data on the list
    @SuppressLint("WrongConstant")
    override fun onBindViewHolder(holder: RepairCategory_Adapter.ViewHolder, position: Int) {


        holder.repaircattext.text = userList.get(position).category_name
        var categoryid = userList.get(position).repair_categories_id
        Glide.with(context).load(userList.get(position).category_icon)
            .apply(RequestOptions().centerCrop())
            .error(R.drawable.logo)
            .into(holder.repaircatimage)
        holder.repairlinearlayout.setOnClickListener {
            holder.repairsubcaterecyclerView.visibility = View.VISIBLE


            holder.repairlinearlayout.setOnClickListener {
                holder.repairsubcaterecyclerView.visibility = View.GONE
                Log.e("open", "open")
            }

            var apiServices = APIClient.client.create(Api::class.java)


            val call = apiServices.getrepairssucategories(categoryid.toString())

            call.enqueue(object : Callback<RepairsSubCategoryListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<RepairsSubCategoryListResponse>,
                    response: Response<RepairsSubCategoryListResponse>
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
                            holder.repairsubcaterecyclerView.context,
                            LinearLayout.VERTICAL,
                            false
                        )

                        holder.repairsubcaterecyclerView.apply {
                            layoutManager = childLayoutManager
                            val adapter = RepairSubCategory_Adapter(context, subuserslist)
                            holder.repairsubcaterecyclerView.adapter = adapter


//                        childLayoutManager.initialPrefetchItemCount = 4
                        }


                    }
                }

                override fun onFailure(
                    call: Call<RepairsSubCategoryListResponse>?,
                    t: Throwable?
                ) {
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

        val repaircatimage = itemView.findViewById(R.id.repaircatimage) as ImageView
        val repaircatarrow = itemView.findViewById(R.id.repaircatarrow) as ImageView
        val repaircattext = itemView.findViewById(R.id.repaircattext) as TextView
        val repairlinearlayout = itemView.findViewById(R.id.repairlinearlayout) as LinearLayout
        val repairsubcaterecyclerView =
            itemView.findViewById<RecyclerView>(R.id.subrepaircaterecyclerView)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(context).inflate(R.layout.reapircategory_adapter, parent, false)
        return ViewHolder(v)
    }


}