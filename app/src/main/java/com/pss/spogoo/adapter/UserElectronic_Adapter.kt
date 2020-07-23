package com.pss.spogoo.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.pss.spogoo.R
import com.pss.spogoo.models.CartItem
import com.pss.spogoo.models.ElectronicsProductResponse
import com.pss.spogoo.models.ShoppingCart
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import java.text.DecimalFormat

class UserElectronic_Adapter(
    context: Context,
    arrayListImage: ArrayList<ElectronicsProductResponse>

) :
    BaseAdapter() {

    //Passing Values to Local Variables

    var context = context
    var arrayListImage = arrayListImage


    //Auto Generated Method
    @SuppressLint("CheckResult")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var myView = convertView
        var holder: ViewHolder


        if (myView == null) {

            //If our View is Null than we Inflater view using Layout Inflater

            val mInflater = (context as Activity).layoutInflater

            //Inflating our grid_item.
            myView = mInflater.inflate(R.layout.product_adapter, parent, false)

            //Create Object of ViewHolder Class and set our View to it

            holder = ViewHolder()


            //Find view By Id For all our Widget taken in grid_item.

            /*Here !! are use for non-null asserted two prevent From null.
             you can also use Only Safe (?.)

            */


            holder.addcart_btn = myView!!.findViewById<TextView>(R.id.addcart_btn) as TextView
            holder.productimage = myView.findViewById<ImageView>(R.id.productimage) as ImageView
            holder.productname = myView.findViewById<TextView>(R.id.productname) as TextView
            holder.prodcut_newimage =
                myView.findViewById<TextView>(R.id.prodcut_newimage) as TextView
            holder.discount_prodcut =
                myView.findViewById<TextView>(R.id.discount_prodcut) as TextView
            holder.mainprice_product =
                myView.findViewById<TextView>(R.id.mainprice_product) as TextView
            holder.offerprice_product =
                myView.findViewById<TextView>(R.id.offerprice_product) as TextView


            //Set A Tag to Identify our view.
            myView.tag = holder


        } else {

            //If Our View in not Null than Just get View using Tag and pass to holder Object.

            holder = myView.tag as ViewHolder

        }

        //Setting Image to ImageView by position
        Glide.with(context).load(arrayListImage.get(position).product_image)
            .apply(RequestOptions().centerCrop())
            .transform(CenterCrop(), RoundedCorners(20))

            .error(R.drawable.logo)
            .into(holder.productimage!!)

        //Setting name to TextView by position
        holder.productname!!.text = arrayListImage.get(position).name

        holder.mainprice_product!!.text =
            "\u20B9  " + arrayListImage.get(position).regular_price.toString()
        var offerprice = arrayListImage.get(position).sales_price

        if (offerprice == null || offerprice.equals("")) {
            holder.offerprice_product!!.visibility = View.GONE
            holder.discount_prodcut!!.visibility = View.GONE
            holder.prodcut_newimage!!.visibility = View.GONE
        } else {
            holder.offerprice_product!!.text = "\u20B9  " + arrayListImage.get(position).sales_price

            var regularprice = arrayListImage.get(position).regular_price.toString()
            var saledprice = arrayListImage.get(position).sales_price.toString()
            var percentage =
                ((regularprice.toInt().minus(saledprice.toInt())) / regularprice.toDouble() * 100)
            val discountformat = DecimalFormat("##")


            holder.discount_prodcut!!.text = discountformat.format(percentage) + "%"
            Log.e("percentage", "" + percentage)
        }

        Observable.create(ObservableOnSubscribe<MutableList<CartItem>> {


            holder.addcart_btn!!.setOnClickListener { view ->

                val item = CartItem(
                    arrayListImage.get(position).electronics_id,
                    arrayListImage.get(position).name,
                    arrayListImage.get(position).product_image,
                    arrayListImage.get(position).regular_price.toString(),
                    arrayListImage.get(position).sales_price.toString(),
                    arrayListImage.get(position).size,
                    arrayListImage.get(position).sales_price.toString(),
                    arrayListImage.get(position).quantity.toString(),
                    0
                )
//                val item = CartItem(arrayListImage.get(position))

                ShoppingCart.addItem(item)
//                //notify users
//
                Toast.makeText(context, "item Added elacronics", Toast.LENGTH_LONG)
                    .show()
//
//
                it.onNext(ShoppingCart.getCart())
//
            }

//            itemView.removeItem.setOnClickListener { view ->
//
//                val item = CartItem(product)
//
//                ShoppingCart.removeItem(item, itemView.context)
//
//                Snackbar.make(
//                    (itemView.context as MainActivity).coordinator,
//                    "${product.name} removed from your cart",
//                    Snackbar.LENGTH_LONG
//                ).show()
//
//                it.onNext(ShoppingCart.getCart())
//
//            }


        }).subscribe { cart ->

            var quantity = 0

            cart.forEach { cartItem ->

                quantity += cartItem.quantity

            }


            Toast.makeText(context, "Cart size $quantity", Toast.LENGTH_SHORT).show()


        }


        return myView

    }

    //Auto Generated Method
    override fun getItem(p0: Int): Any {
        return arrayListImage.get(p0)
    }

    //Auto Generated Method
    override fun getItemId(p0: Int): Long {
        return 0
    }

    //Auto Generated Method
    override fun getCount(): Int {
        return arrayListImage.size
    }


    //Create A class To hold over View like we taken in grid_item.xml

    class ViewHolder {

        var addcart_btn: TextView? = null
        var productimage: ImageView? = null
        var productname: TextView? = null
        var discount_prodcut: TextView? = null
        var mainprice_product: TextView? = null
        var offerprice_product: TextView? = null
        var prodcut_newimage: TextView? = null

    }

}