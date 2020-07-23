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
import com.pss.spogoo.models.HealthProductsResponse
import java.text.DecimalFormat

class ShopHealthPrdct_Adapter(
    var context: Context,
    val userList: ArrayList<HealthProductsResponse>
) :
    RecyclerView.Adapter<ShopHealthPrdct_Adapter.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v =
            LayoutInflater.from(context).inflate(R.layout.shophealthproduct_adpter, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    @SuppressLint("WrongConstant")
    override fun onBindViewHolder(holder: ShopHealthPrdct_Adapter.ViewHolder, position: Int) {


        holder.healthproducttext.text = userList.get(position).name
        var offerprice = userList.get(position).sales_price
        if (offerprice == null || offerprice.equals("") || offerprice.isEmpty()) {
            holder.healthproductofferprice.visibility = View.GONE
            holder.healthproductdiscount.visibility = View.GONE
            holder.healthproductnewtag.visibility = View.GONE
        } else {
            holder.healthproductofferprice.text = "\u20B9 " + userList.get(position).sales_price
            var regularprice = userList.get(position).regular_price.toString()
            var saledprice = userList.get(position).sales_price

            var percentage =
                ((regularprice.toInt().minus(saledprice.toInt())) / regularprice.toDouble() * 100)
            val discountformat = DecimalFormat("##")

            Log.e("percentage", "" + percentage)

            holder.healthproductdiscount.text = discountformat.format(percentage) + "%"
        }
        holder.healthproductmainprice.text =
            "\u20B9 " + userList.get(position).regular_price.toString()
        Glide.with(context).load(userList.get(position).product_image)
            .apply(RequestOptions().centerCrop())
            .error(R.drawable.logo)
            .into(holder.healthproductimage)

    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val healthproductimage = itemView.findViewById(R.id.healthproductimage) as ImageView
        val healthproducttext = itemView.findViewById(R.id.healthproducttext) as TextView
        val healthproductofferprice =
            itemView.findViewById(R.id.healthproductofferprice) as TextView
        val healthproductmainprice = itemView.findViewById(R.id.healthproductmainprice) as TextView
        val healthproductdiscount = itemView.findViewById(R.id.healthproductdiscount) as TextView
        val healthproductnewtag = itemView.findViewById(R.id.healthproductnewtag) as TextView


    }


}