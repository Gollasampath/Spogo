package com.pss.spogoo.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.pss.spogoo.R
import com.pss.spogoo.models.BrandsProductsResponse

class ShopBrandsList_Adapter(
    var context: Context,
    val userList: ArrayList<BrandsProductsResponse>
) :
    RecyclerView.Adapter<ShopBrandsList_Adapter.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShopBrandsList_Adapter.ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.brandsadapter_screen, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    @SuppressLint("WrongConstant")
    override fun onBindViewHolder(holder: ShopBrandsList_Adapter.ViewHolder, position: Int) {


        holder.buildtext.text = userList.get(position).brand_name
//        Log.e("image", userList.get(position).product_image)
        Glide.with(context).load(userList.get(position).brand_image)
            .apply(RequestOptions().centerCrop())
            .transform(CenterCrop(), RoundedCorners(20))
            .error(R.drawable.logo)
            .into(holder.buildimage)
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val buildimage = itemView.findViewById(R.id.buildimage) as ImageView
        val buildtext = itemView.findViewById(R.id.buildtext) as TextView


    }


}