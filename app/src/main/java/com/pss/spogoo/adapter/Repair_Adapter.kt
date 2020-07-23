package com.pss.spogoo.adapter

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.pss.spogoo.R

class Repair_Adapter(context: Context, arrayListImage: ArrayList<Int>, name: Array<String>) :
    BaseAdapter() {

    //Passing Values to Local Variables

    var context = context
    var arrayListImage = arrayListImage
    var name = name


    //Auto Generated Method
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var myView = convertView
        var holder: ViewHolder


        if (myView == null) {

            //If our View is Null than we Inflater view using Layout Inflater

            val mInflater = (context as Activity).layoutInflater

            //Inflating our grid_item.
            myView = mInflater.inflate(R.layout.repairrecent_adpter, parent, false)

            //Create Object of ViewHolder Class and set our View to it

            holder = ViewHolder()


            //Find view By Id For all our Widget taken in grid_item.

            /*Here !! are use for non-null asserted two prevent From null.
             you can also use Only Safe (?.)

            */


            holder.repairrecentimage =
                myView!!.findViewById<ImageView>(R.id.repairrecentimage) as ImageView
            holder.repairrecenttext =
                myView.findViewById<TextView>(R.id.repairrecenttext) as TextView


            //Set A Tag to Identify our view.
            myView.tag = holder


        } else {

            //If Our View in not Null than Just get View using Tag and pass to holder Object.

            holder = myView.tag as ViewHolder

        }

        //Setting Image to ImageView by position
        holder.repairrecentimage!!.setImageResource(arrayListImage.get(position))

        //Setting name to TextView by position
        holder.repairrecenttext!!.text = name.get(position)

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

        var repairrecentimage: ImageView? = null
        var repairrecenttext: TextView? = null

    }

}