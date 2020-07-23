package com.pss.spogoo.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pss.spogoo.R
import com.pss.spogoo.models.CartItem
import com.pss.spogoo.models.ShoppingCart
import com.pss.spogoo.models.SubCategoryResponse
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import kotlinx.android.synthetic.main.cart_list_adapter.view.*
import java.text.DecimalFormat

class ShoppingCartAdapter(var context: Context, var cartItems: MutableList<CartItem>) :
    RecyclerView.Adapter<ShoppingCartAdapter.ViewHolder>() {

    lateinit var mcontext: Context
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ShoppingCartAdapter.ViewHolder {

        val layout = LayoutInflater.from(context).inflate(R.layout.cart_list_adapter, parent, false)

        return ViewHolder(layout)
    }

    override fun getItemCount(): Int = cartItems.size

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(viewHolder: ShoppingCartAdapter.ViewHolder, position: Int) {


        viewHolder.bindItem(cartItems[position])

        Glide.with(context).load(cartItems.get(position).productimage)
            .apply(RequestOptions().centerCrop())
            .error(R.drawable.logo)
            .into(viewHolder.cartadapter_image)

        viewHolder.cartadapter_quantity.text = cartItems.get(position).quantity.toString()


        Observable.create(ObservableOnSubscribe<MutableList<CartItem>> {

            var productpos: SubCategoryResponse

            viewHolder.plusitem.setOnClickListener { view ->

                val item = CartItem(
                    cartItems.get(position).products_id,
                    cartItems.get(position).productname,
                    cartItems.get(position).productimage,
                    cartItems.get(position).mainprice,
                    cartItems.get(position).offerprice,
                    cartItems.get(position).size,
                    cartItems.get(position).discount,
                    cartItems.get(position).item_qty
                )
                ShoppingCart.addItem(item)
                //notify users

                Toast.makeText(context, "Items Added Sucessfully", Toast.LENGTH_LONG)
                    .show()

//                viewHolder.cartadapter_quantity.text = item.toString()

                Log.e("cartqty", "" + item)
                it.onNext(ShoppingCart.getCart())

            }

            viewHolder.removeitem.setOnClickListener { view ->

                val item = CartItem(
                    cartItems.get(position).products_id,
                    cartItems.get(position).productname,
                    cartItems.get(position).productimage,
                    cartItems.get(position).mainprice,
                    cartItems.get(position).offerprice,
                    cartItems.get(position).size,
                    cartItems.get(position).discount,
                    cartItems.get(position).item_qty
                )
                ShoppingCart.removeItem(item, context)

                Toast.makeText(context, "Items Removed", Toast.LENGTH_LONG)
                    .show()
//                viewHolder.cartadapter_quantity.text = item.toString()

                it.onNext(ShoppingCart.getCart())

            }


        }).subscribe { cart ->

            var quantity = 0

            cart.forEach { cartItem ->

                quantity += cartItem.quantity
            }


            Toast.makeText(context, "Cart size $quantity", Toast.LENGTH_SHORT).show()


        }
        viewHolder.cartadapter_delete.setOnClickListener {
            val newPosition: Int = viewHolder.adapterPosition
            try {


                removeAt(viewHolder.adapterPosition)
                notifyDataSetChanged()
                val item = CartItem(
                    cartItems.get(position).products_id,
                    cartItems.get(position).productname,
                    cartItems.get(position).productimage,
                    cartItems.get(position).mainprice,
                    cartItems.get(position).offerprice,
                    cartItems.get(position).size,
                    cartItems.get(position).discount,
                    cartItems.get(position).item_qty
                )
                ShoppingCart.dleteCart(item, context)


                Log.e("delete pos", "" + newPosition)
                Log.e("delete item", "" + cartItems.get(position).products_id.toString())
            } catch (e: IndexOutOfBoundsException) {
                e.printStackTrace()
            }
//            ShoppingCart.removeItem(item, context)
//            (ShoppingCart.getCart())


        }

    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        @SuppressLint("CheckResult")
        fun bindItem(cartItem: CartItem) {


            itemView.cartadapter_text.text = cartItem.productname
            itemView.cartadapter_offerprice.text = "\u20B9 " + cartItem.offerprice.toString()
            itemView.categorysize.text = "Size : " + cartItem.size
            itemView.cartadapter_mainprice.text = "\u20B9  " + cartItem.mainprice
            itemView.cartadapter_quantity.text = cartItem.quantity.toString()

            var regularprice = cartItem.mainprice
            var saledprice = cartItem.offerprice
            var percentage =
                ((regularprice.toInt()
                    .minus(saledprice.toInt())) / regularprice.toDouble() * 100)
            val discountformat = DecimalFormat("##")

            Log.e("percentage", "" + percentage)

            itemView.cartadapter_discount.text = discountformat.format(percentage) + "%"

        }

        val cartadapter_image = itemView.findViewById(R.id.cartadapter_image) as ImageView
        val removeitem = itemView.findViewById(R.id.removeitem) as ImageView
        val cartadapter_quantity = itemView.findViewById(R.id.cartadapter_quantity) as TextView
        val plusitem = itemView.findViewById(R.id.plusitem) as ImageView
        val cartadapter_delete = itemView.findViewById(R.id.cartadapter_delete) as ImageView
        val cartadapter_discount = itemView.findViewById(R.id.cartadapter_discount) as TextView


    }

    fun removeAt(position: Int) {
        try {
            cartItems.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, cartItems.size)
        } catch (e: ArrayIndexOutOfBoundsException) {
            e.printStackTrace()
        }
    }


}