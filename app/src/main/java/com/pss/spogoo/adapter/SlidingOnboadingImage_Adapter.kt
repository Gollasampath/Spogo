package com.example.parsaniahardik.kotlin_image_slider

import android.content.Context
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.pss.spogoo.R
import com.pss.spogoo.models.ImageModel
import java.util.*

/**
 * Created by suchipeddinti on 10/07/2019.
 */
class SlidingOnboadingImage_Adapter(
    private val context: Context,
    private val imageModelArrayList: ArrayList<ImageModel>
) : PagerAdapter() {
    private val inflater: LayoutInflater


    init {
        inflater = LayoutInflater.from(context)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return imageModelArrayList.size
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val imageLayout = inflater.inflate(R.layout.viewpageronboarding_activity, view, false)!!

        val imageView = imageLayout
            .findViewById(R.id.onboadingimage) as ImageView


        imageView.setImageResource(imageModelArrayList[position].getImage_drawables())

        view.addView(imageLayout, 0)

        return imageLayout
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {}

    override fun saveState(): Parcelable? {
        return null
    }


}