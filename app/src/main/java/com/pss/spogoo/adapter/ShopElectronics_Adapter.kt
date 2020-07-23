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
import com.bumptech.glide.request.RequestOptions
import com.pss.spogoo.R
import com.pss.spogoo.models.ElectronicsCategoriesResponse

class ShopElectronics_Adapter(
    var context: Context,
    val userList: ArrayList<ElectronicsCategoriesResponse>
) :
    RecyclerView.Adapter<ShopElectronics_Adapter.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShopElectronics_Adapter.ViewHolder {
        val v =
            LayoutInflater.from(context).inflate(R.layout.apparealsadapter_screen, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    @SuppressLint("WrongConstant")
    override fun onBindViewHolder(holder: ShopElectronics_Adapter.ViewHolder, position: Int) {


        holder.appareraltext.text = userList.get(position).category_name
        Log.e("image", userList.get(position).category_image)
        Glide.with(context).load(userList.get(position).category_image)
            .apply(RequestOptions().centerCrop())
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