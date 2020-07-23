package com.pss.spogoo

import android.content.Intent
import android.os.Bundle
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ServiceList_Activity : AppCompatActivity() {

    val arrayListproduct = ArrayList<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.servicelist_screen)


        val gvproductdetailsgridview =
            findViewById<GridView>(R.id.servicelist) as GridView

        // load featured
//        arrayListproduct.add(R.drawable.racketsnmall)
//        arrayListproduct.add(R.drawable.string)


        val name = arrayOf(
            "Replace string with grip",
            "Replace string with grip"
        )

//        val adapter = Product_Adapter(this, arrayListproduct, name)
//        gvproductdetailsgridview.adapter = adapter


        gvproductdetailsgridview.setOnItemClickListener { adapterView, parent, position, l ->
            Toast.makeText(this, "Click on : " + name[position], Toast.LENGTH_LONG).show()

//            val navController =
//                Navigation.findNavController(context as Activity, R.id.nav_host_fragment)
//            navController.navigate(R.id.action_nav_home_to_nav_productdetails)

            startActivity(Intent(this, ServiceDetails_Activity::class.java))

        }


    }


}
