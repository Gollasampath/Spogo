package com.pss.spogoo.ui.home

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.pss.spogoo.R
import com.pss.spogoo.adapter.BuildProfile_Adapter
import com.pss.spogoo.api.APIClient
import com.pss.spogoo.api.Api
import com.pss.spogoo.models.ShopAllCategoryResponse
import com.pss.spogoo.models.ShopListResponse
import com.pss.spogoo.util.NetWorkConection
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BuildProfile_Fragment : Fragment() {

    lateinit var gvsportslist: GridView
    var biuldadapter: BuildProfile_Adapter? = null

    lateinit var listOfallcategories: List<ShopAllCategoryResponse>
    lateinit var progressBarshop: ProgressBar

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.buildprofile_screen, container, false)

        gvsportslist = root.findViewById<GridView>(R.id.gvsportslist) as GridView
        progressBarshop = root.findViewById(R.id.progressBarprofile)

        progressBarshop.visibility = View.VISIBLE


        getBuildProfileData()

        return root
    }


    private fun getBuildProfileData() {

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

                    progressBarshop.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//

                        listOfallcategories = response.body()?.response!!

                        biuldadapter =
                            context?.let {
                                BuildProfile_Adapter(
                                    it,
                                    listOfallcategories as ArrayList<ShopAllCategoryResponse>
                                )
                            }

                        gvsportslist.adapter = biuldadapter
                        biuldadapter!!.notifyDataSetChanged()

//                        gvsportslist.setOnItemClickListener { adapterView, parent, position, l ->
//                            Toast.makeText(
//                                activity,
//                                "Click on : " + name[position],
//                                Toast.LENGTH_LONG
//                            ).show()
//                        }

//                        val itemDecorator2 = VerticalSpacingItemDecorator(20)
//                        gvsportslist.addItemDecoration(itemDecorator2)
                    }
                }

                override fun onFailure(call: Call<ShopListResponse>?, t: Throwable?) {
                    progressBarshop.visibility = View.GONE
                    Log.e(ContentValues.TAG, t.toString())
                }
            })


        } else {

            Toast.makeText(context, "Please Check your internet", Toast.LENGTH_LONG).show()
        }


    }

}
