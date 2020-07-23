package com.pss.spogoo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pss.spogoo.adapter.ShoppingCartAdapter
import com.pss.spogoo.models.ShoppingCart
import io.paperdb.Paper
import kotlinx.android.synthetic.main.cart_list_screen.*

class Cart_Screen : AppCompatActivity() {

    lateinit var adapter: ShoppingCartAdapter
    lateinit var recyclerviewcart: RecyclerView
    lateinit var totalprice: Button
    lateinit var cart_badge: TextView
    lateinit var cartback_image: ImageView
    lateinit var cartlayout: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Paper.init(this)

        setContentView(R.layout.cart_list_screen)

        recyclerviewcart = findViewById<RecyclerView>(R.id.recyclerviewcart)
        totalprice = findViewById<Button>(R.id.totalprice)
        cartback_image = toolbar.findViewById<ImageView>(R.id.backcart_image)

        recyclerviewcart.layoutManager = LinearLayoutManager(this)

        adapter = ShoppingCartAdapter(this, ShoppingCart.getCart())
//        adapter = ShoppingCartAdapter(this, ShoppingCart.getCartElectronics())


        recyclerviewcart.adapter = adapter
        adapter.notifyDataSetChanged()

        cartback_image.setOnClickListener {
            startActivity(Intent(this, Home_Screen::class.java))
            finish()
        }
        var itemcount = ShoppingCart.getCart().size
        Log.e("itemcount size", "" + itemcount)
        var totalPrice = ShoppingCart.getCart()
            .fold(0.toDouble()) { acc, cartItem ->
                acc + cartItem.offerprice.toDouble().times(cartItem.item_qty.toDouble())
            }

        totalprice.text = "\u20B9 " + totalPrice

    }


}