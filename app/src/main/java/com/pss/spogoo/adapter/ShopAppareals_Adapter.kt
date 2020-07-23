package com.pss.spogoo.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
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
import com.pss.spogoo.models.ApparealsCategoryResponse

class ShopAppareals_Adapter(
    var context: Context,
    val userList: ArrayList<ApparealsCategoryResponse>
) :
    RecyclerView.Adapter<ShopAppareals_Adapter.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShopAppareals_Adapter.ViewHolder {
        val v =
            LayoutInflater.from(context).inflate(R.layout.apparealsadapter_screen, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    @SuppressLint("WrongConstant")
    override fun onBindViewHolder(holder: ShopAppareals_Adapter.ViewHolder, position: Int) {


        holder.appareraltext.text = userList.get(position).category_name
        Log.e("image", userList.get(position).category_image)
        Glide.with(context).load(userList.get(position).category_image)
            .apply(RequestOptions().centerCrop())
            .transform(CenterCrop(), RoundedCorners(20))

            .error(R.drawable.logo)
            .into(holder.appareralimage)
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val appareralimage = itemView.findViewById(R.id.appareralimage) as ImageView
        val appareraltext = itemView.findViewById(R.id.appareraltext) as TextView


    }


}