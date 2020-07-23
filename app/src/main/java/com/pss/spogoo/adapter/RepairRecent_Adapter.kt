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
import com.pss.spogoo.models.RepairsRecentResponse
import java.text.DecimalFormat

class RepairRecent_Adapter(var context: Context, val userList: ArrayList<RepairsRecentResponse>) :
    RecyclerView.Adapter<RepairRecent_Adapter.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RepairRecent_Adapter.ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.repairrecent_adpter, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    @SuppressLint("WrongConstant")
    override fun onBindViewHolder(holder: RepairRecent_Adapter.ViewHolder, position: Int) {
        try {


            holder.repairrecenttext.text = userList.get(position).product_name
            var offerprice = userList.get(position).special_price
            if (offerprice == null || offerprice.equals("") || offerprice.isEmpty()) {
                holder.offerprice.visibility = View.GONE
                holder.discount.visibility = View.GONE
                holder.newtag.visibility = View.GONE
            } else {

                holder.offerprice.text = "\u20B9 " + userList.get(position).special_price

                var regularprice = userList.get(position).regular_price.toString()
                var saledprice = userList.get(position).special_price

                var percentage =
                    ((regularprice.toInt()
                        .minus(saledprice.toInt())) / regularprice.toDouble() * 100)
                val discountformat = DecimalFormat("##")

                Log.e("percentage", "" + percentage)

                holder.discount.text = discountformat.format(percentage) + "%"
            }
            holder.mainprice.text = "\u20B9 " + userList.get(position).regular_price.toString()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

        Glide.with(context).load(userList.get(position).product_image)
            .apply(RequestOptions().centerCrop())
            .error(R.drawable.logo)
            .into(holder.repairrecentimage)

    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val repairrecentimage = itemView.findViewById(R.id.repairrecentimage) as ImageView
        val repairrecenttext = itemView.findViewById(R.id.repairrecenttext) as TextView
        val offerprice = itemView.findViewById(R.id.offerprice) as TextView
        val mainprice = itemView.findViewById(R.id.mainprice) as TextView
        val discount = itemView.findViewById(R.id.discount) as TextView
        val newtag = itemView.findViewById(R.id.newtag) as TextView


    }


}