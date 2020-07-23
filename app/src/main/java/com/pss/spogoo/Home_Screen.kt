package com.pss.spogoo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.iid.FirebaseInstanceId
import com.pss.spogoo.models.ShoppingCart
import io.paperdb.Paper


class Home_Screen : AppCompatActivity() {

    lateinit var cart_badge: TextView
    lateinit var cartlayout: FrameLayout
    var doubleBackToExitOnce: Boolean = false

    @SuppressLint("StringFormatInvalid")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Paper.init(this)

        setContentView(R.layout.activity_home__screen)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        cartlayout = findViewById<FrameLayout>(R.id.cartlayout)
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayShowTitleEnabled(false)
        cart_badge = toolbar.findViewById(R.id.cart_badge)


        if (ShoppingCart.getShoppingCartSize().toString().equals("0")) {
            cart_badge.visibility = View.GONE
        } else {
            cart_badge.visibility = View.VISIBLE

            cart_badge.text = ShoppingCart.getShoppingCartSize().toString()

        }
        cartlayout.setOnClickListener {
            startActivity(Intent(this, Cart_Screen::class.java))
        }
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_categories,
                R.id.navigation_search,
                R.id.navigation_offers,
                R.id.navigation_account
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("TAG", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.id

                Log.e("firebase token", token)
                // Log and toast
                val msg = getString(R.string.app_name, token)
                Log.d("TAG", msg)
//                Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
            })


    }

    override fun onBackPressed() {


        if (doubleBackToExitOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitOnce = true

        //displays a toast message when user clicks exit button
        Toast.makeText(applicationContext, "please press again to exit ", Toast.LENGTH_LONG).show()

        //displays the toast message for a while
        Handler().postDelayed({
            kotlin.run {
                doubleBackToExitOnce = false

                finish()
            }
        }, 2000)


    }


//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.cartbutton_menu, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here.
//        val id = item.itemId
////
////        if (id == R.id.action_one) {
////            Toast.makeText(this, "Item One Clicked", Toast.LENGTH_LONG).show()
////            return true
////        }
//
//
//        return super.onOptionsItemSelected(item)
//
//    }
}
