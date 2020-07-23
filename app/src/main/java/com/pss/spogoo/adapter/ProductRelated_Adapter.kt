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
import com.pss.spogoo.models.RelatedproductResponse
import java.text.DecimalFormat

class ProductRelated_Adapter(
    var context: Context,
    val userList: ArrayList<RelatedproductResponse>
) :
    RecyclerView.Adapter<ProductRelated_Adapter.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductRelated_Adapter.ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.product_adapter, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    @SuppressLint("WrongConstant")
    override fun onBindViewHolder(
        holder: ProductRelated_Adapter.ViewHolder,
        position: Int
    ) {


        holder.productname.text = userList.get(position).product_name
        Glide.with(context).load(userList.get(position).product_image)
            .apply(RequestOptions().centerCrop())
            .error(R.drawable.logo)
            .into(holder.productimage)
        var offerprice = userList.get(position).sales_price
        if (offerprice == null || offerprice.equals("")) {
            holder.offerprice_product.visibility = View.GONE
            holder.discount_prodcut.visibility = View.GONE
            holder.prodcut_newimage.visibility = View.GONE
        } else {
            holder.offerprice_product.text = "\u20B9 " + userList.get(position).sales_price

            var regularprice = userList.get(position).price.toString()
            var saledprice = userList.get(position).sales_price

            var percentage =
                ((regularprice.toInt().minus(saledprice.toInt())) / regularprice.toDouble() * 100)
            val discountformat = DecimalFormat("##")

            Log.e("percentage", "" + percentage)

            holder.discount_prodcut.text = discountformat.format(percentage) + "%"
        }
        holder.mainprice_product.text = "\u20B9 " + userList.get(position).price.toString()

    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val productimage = itemView.findViewById(R.id.productimage) as ImageView
        val productname = itemView.findViewById(R.id.productname) as TextView
        val prodcut_newimage = itemView.findViewById(R.id.prodcut_newimage) as TextView
        val offerprice_product = itemView.findViewById(R.id.offerprice_product) as TextView
        val mainprice_product = itemView.findViewById(R.id.mainprice_product) as TextView
        val discount_prodcut = itemView.findViewById(R.id.discount_prodcut) as TextView
        val addcart_btn = itemView.findViewById(R.id.addcart_btn) as TextView


    }


}