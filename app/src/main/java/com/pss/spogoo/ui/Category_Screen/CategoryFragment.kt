package com.pss.spogoo.ui.Category_Screen

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pss.spogoo.R
import com.pss.spogoo.adapter.ShopCategory_Adapter
import com.pss.spogoo.api.APIClient
import com.pss.spogoo.api.Api
import com.pss.spogoo.models.ShopAllCategoryResponse
import com.pss.spogoo.models.ShopListResponse
import com.pss.spogoo.util.NetWorkConection
import com.pss.spogoo.util.VerticalSpacingItemDecorator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryFragment : Fragment() {

    lateinit var progressBarcategory: ProgressBar
    var subcategory_id: Int = 0
    var categories_id: Int = 0

    lateinit var shopcaterecyclerView: RecyclerView
    lateinit var listOfallcategories: List<ShopAllCategoryResponse>
    lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("WrongConstant", "UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.category_fragment, container, false)



        progressBarcategory = root.findViewById(R.id.progressBarcategory)

        shopcaterecyclerView =
            root.findViewById<RecyclerView>(R.id.shopcaterecyclerView) as RecyclerView

        sharedPreferences =
            activity!!.getSharedPreferences("loginprefs", Context.MODE_PRIVATE)

        //adding a layoutmanager
        shopcaterecyclerView.layoutManager =
            LinearLayoutManager(activity, LinearLayout.VERTICAL, false)


        getAllCategoriesList()


        return root
    }


    private fun getAllCategoriesList() {

        if (context?.let { activity?.let { it1 -> NetWorkConection.isNEtworkConnected(it1) } }!!) {

            //Set the Adapter to the RecyclerView//


            var apiServices = APIClient.client.create(Api::class.java)


            val call = apiServices.getuserCategoriesList()

            call.enqueue(object : Callback<ShopListResponse> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<ShopListResponse>,
                    response: Response<ShopListResponse>
                ) {

                    progressBarcategory.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//

                        listOfallcategories = response.body()?.response!!
                        if (listOfallcategories.isEmpty()) {
                            Log.e("list empty", "" + listOfallcategories.isEmpty())

                        } else {


                            //creating our adapter
                            val adapter =
                                activity?.let {
                                    ShopCategory_Adapter(
                                        it,
                                        listOfallcategories as ArrayList<ShopAllCategoryResponse>
                                    )
                                }

                            //now adding the adapter to recyclerview
                            shopcaterecyclerView.adapter = adapter

                            adapter?.notifyDataSetChanged()

//                        shopcaterecyclerView.addOnItemClickListener(object :
//                            OnItemClickListener {
//                            override fun onItemClicked(position: Int, view: View) {
//                                // Your logic
//                                val subcatgeory =
//                                    adapter?.userList!!.get(position).sub_category_list.map {
//                                        subcategory_id = it.sub_categories_id
//                                        categories_id = it.categories_id
//
//                                        Log.e("subcat", "" + subcategory_id)
//                                        Log.e("cat id", "" + categories_id)
//
//                                    }
//
//                            }
//                        })

                            val itemDecorator2 = VerticalSpacingItemDecorator(20)
                            shopcaterecyclerView.addItemDecoration(itemDecorator2)
                        }
                    }
                }

                override fun onFailure(call: Call<ShopListResponse>?, t: Throwable?) {
                    progressBarcategory.visibility = View.GONE
                    Log.e(ContentValues.TAG, t.toString())
                }
            })


        } else {

            Toast.makeText(context, "Please Check your internet", Toast.LENGTH_LONG).show()
        }


    }


    //on item click interface
    interface OnItemClickListener {
        fun onItemClicked(position: Int, view: View)
    }

    fun RecyclerView.addOnItemClickListener(onClickListener: OnItemClickListener) {
        this.addOnChildAttachStateChangeListener(object :
            RecyclerView.OnChildAttachStateChangeListener {
            override fun onChildViewDetachedFromWindow(view: View) {
            }


            override fun onChildViewAttachedToWindow(view: View) {
                view.setOnClickListener {
                    val holder = getChildViewHolder(view)
                    onClickListener.onItemClicked(holder.adapterPosition, view)
                }
            }
        })
    }
}
