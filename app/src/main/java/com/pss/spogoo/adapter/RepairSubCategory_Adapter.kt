package com.pss.spogoo.adapter

import android.app.Activity
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
import com.pss.spogoo.RepairsProdctList_Screen
import com.pss.spogoo.models.RepairsSubCategoriesResponse


class RepairSubCategory_Adapter(
    var context: Context,
    val userList: List<RepairsSubCategoriesResponse>
) :
    RecyclerView.Adapter<RepairSubCategory_Adapter.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RepairSubCategory_Adapter.ViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.repairsubcategory_adapter, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: RepairSubCategory_Adapter.ViewHolder, position: Int) {

        holder.repairsubcattext.text = userList[position].sub_category_name

        Log.e("subcatid ", "" + userList.get(position).repair_sub_categories_id)
        var sharedPreferences =
            context.getSharedPreferences("loginprefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString(
            "repairsubcategory_id",
            userList.get(position).repair_sub_categories_id.toString()
        )
        editor.putString("repairsubcategory_name", userList.get(position).sub_category_name)
        editor.putInt("repair_indetails", 12)
        editor.putInt("repairlist", 5)
        editor.commit()

        holder.linearsubcatgory.setOnClickListener {

            context.startActivity(Intent(context, RepairsProdctList_Screen::class.java))
            (context as Activity).finish()
        }
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val repairsubcattext = itemView.findViewById(R.id.repairsubcattext) as TextView
        val linearsubcatgory = itemView.findViewById(R.id.linearsubcatgory) as LinearLayout


    }
}