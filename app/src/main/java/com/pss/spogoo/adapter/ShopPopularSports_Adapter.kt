package com.pss.spogoo.adapter

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.pss.spogoo.R
import com.pss.spogoo.models.ShopAllCategoryResponse

class ShopPopularSports_Adapter(
    context: Context,
    arrayListImage: ArrayList<ShopAllCategoryResponse>
) :
    BaseAdapter() {

    //Passing Values to Local Variables

    var context = context
    var arrayListImage = arrayListImage


    //Auto Generated Method
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var myView = convertView
        var holder: ViewHolder


        if (myView == null) {

            //If our View is Null than we Inflater view using Layout Inflater

            val mInflater = (context as Activity).layoutInflater

            //Inflating our grid_item.
            myView = mInflater.inflate(R.layout.shoppopularsport_adapter, parent, false)

            //Create Object of ViewHolder Class and set our View to it

            holder = ViewHolder()


            //Find view By Id For all our Widget taken in grid_item.

            /*Here !! are use for non-null asserted two prevent From null.
             you can also use Only Safe (?.)

            */


            holder.shoppopularimage =
                myView?.findViewById<ImageView>(R.id.shoppopularimage) as ImageView
            holder.shoppopulartext = myView.findViewById<TextView>(R.id.shoppopulartext) as TextView


            //Set A Tag to Identify our view.
            myView.tag = holder


        } else {

            //If Our View in not Null than Just get View using Tag and pass to holder Object.

            holder = myView.tag as ViewHolder

        }

        //Setting Image to ImageView by position
        Glide.with(context).load(arrayListImage.get(position).category_image)
            .apply(RequestOptions().centerCrop())
            .error(R.drawable.logo)
            .transform(CenterCrop(), RoundedCorners(20))

            .into(holder.shoppopularimage!!)

        //Setting name to TextView by position
        holder.shoppopulartext!!.text = arrayListImage.get(position).category_name

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

        var shoppopularimage: ImageView? = null
        var shoppopulartext: TextView? = null

    }

}