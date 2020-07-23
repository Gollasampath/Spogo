package com.pss.spogoo.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pss.spogoo.R
import com.pss.spogoo.UserProdctList_Screen
import com.pss.spogoo.models.SubProductResponse

class ShopSubCategory_Adapter(
    var context: Context,
    val userList: List<SubProductResponse>
) :
    RecyclerView.Adapter<ShopSubCategory_Adapter.ViewHolder>() {
    lateinit var subcatname: String

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShopSubCategory_Adapter.ViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.shopsubcategory_adapter, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ShopSubCategory_Adapter.ViewHolder, position: Int) {
//        val subcatnamelist = userList.get(position).sub_category_list.map {
//            subcatname = it.sub_category_image
//            Log.e("subcatname inside", subcatname)
//
//        }

        holder.shopsubcattext.text = userList.get(position).sub_category_name

        Log.e("subcatname", userList.get(position).sub_category_name)
        Log.e("subcatid ", "" + userList.get(position).sub_categories_id)
        var sharedPreferences =
            context.getSharedPreferences("loginprefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putInt("subcategory_id", userList.get(position).sub_categories_id)
        editor.commit()
        holder.shopsubcategory.setOnClickListener {

//            val navController =
//                Navigation.findNavController(context as Activity, R.id.nav_host_fragment)
//            navController.navigate(R.id.action_nav_home_to_nav_product)

            val sharedPreferences =
                context.getSharedPreferences(
                    "loginprefs",
                    Context.MODE_PRIVATE
                )
            val editor = sharedPreferences.edit()
            val subcatid = userList.get(position).sub_categories_id.toString()
            val subcatname = userList.get(position).sub_category_name.toString()
            editor.putString("subcategories_id", subcatid)
            editor.putString("subcategory_name", subcatname)
            editor.putInt("indetailswitch", 6)
            editor.commit()
            Log.e("subcatid", subcatid)
            Log.e("subcatname", subcatname)
//
            val intent =
                Intent(context, UserProdctList_Screen::class.java)
            context.startActivity(intent)
        }
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val shopsubcattext = itemView.findViewById(R.id.shopsubcattext) as TextView
        val shopsubcategory = itemView.findViewById(R.id.shopsubcategory) as LinearLayout


    }
}