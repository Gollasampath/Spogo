package com.pss.spogoo.ui.search

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pss.spogoo.R
import com.pss.spogoo.UserProdctList_Screen
import com.pss.spogoo.adapter.Search_Adapter
import com.pss.spogoo.api.APIClient
import com.pss.spogoo.api.Api
import com.pss.spogoo.models.SearchListResponse
import com.pss.spogoo.models.SearchResponse
import com.pss.spogoo.util.NetWorkConection
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment() {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var listOfsearch: List<SearchResponse>
    var searchAdapter: Search_Adapter? = null
    lateinit var searchrecyclerView: RecyclerView
    lateinit var searchdatarecyclerView: RecyclerView

    lateinit var searchedit: EditText

    @SuppressLint("WrongConstant")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val root = inflater.inflate(R.layout.search_screen, container, false)

        sharedPreferences =
            requireContext().getSharedPreferences(
                "loginprefs",
                Context.MODE_PRIVATE
            )
        searchedit = root.findViewById(R.id.searchedit) as EditText
        searchrecyclerView = root.findViewById(R.id.searchrecyclerView) as RecyclerView
        searchdatarecyclerView = root.findViewById(R.id.searchdatarecyclerView) as RecyclerView

        searchrecyclerView.layoutManager =
            LinearLayoutManager(activity, LinearLayout.VERTICAL, false)

        searchdatarecyclerView.layoutManager =
            LinearLayoutManager(activity, LinearLayout.VERTICAL, false)

        getSearchCardsResponse()
        searchedit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

                searchAdapter!!.filter.filter(charSequence.toString())

                Log.e("newtext", charSequence.toString())
                val editor = sharedPreferences.edit()
                editor.putString("filtername", charSequence.toString())
                editor.commit()
                getSearchWordsResponse()
            }

            override fun afterTextChanged(editable: Editable) {
                //after the change calling the method and passing the search input

                searchAdapter!!.filter.filter(editable.toString())

                Log.e("newtext", editable.toString())
                val editor = sharedPreferences.edit()
                editor.putString("filtername", editable.toString())
                editor.commit()

//                getSearchCardsResponse()
            }
        })

        return root
    }

    private fun getSearchCardsResponse() {

        if (context?.let { NetWorkConection.isNEtworkConnected(it as Activity) }!!) {


            val filtername = sharedPreferences.getString("filtername", "")

            Log.e("filtername", filtername)


            var apiServices = APIClient.client.create(Api::class.java)


            val call = apiServices.getSearchlist()

            call.enqueue(object : Callback<SearchListResponse> {
                override fun onResponse(
                    call: Call<SearchListResponse>,
                    response: Response<SearchListResponse>
                ) {

//                    progressBarhome.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {
                        try {

                            //Set the Adapter to the RecyclerView//

                            listOfsearch = response.body()!!.response

                            searchAdapter = context?.let { Search_Adapter(it, listOfsearch) }
                            searchrecyclerView.adapter = searchAdapter
                            searchAdapter!!.notifyDataSetChanged()

                        } catch (e: NullPointerException) {
                            e.printStackTrace()
                        }

                    }
                }

                override fun onFailure(call: Call<SearchListResponse>?, t: Throwable?) {
//                    progressBarhome.visibility = View.GONE
                    Log.e(ContentValues.TAG, t.toString())
                }
            })


        } else {


            Toast.makeText(context, "Please Check your internet", Toast.LENGTH_LONG).show()
        }

    }

    private fun getSearchWordsResponse() {

        if (context?.let { NetWorkConection.isNEtworkConnected(it as Activity) }!!) {


            val filtername = sharedPreferences.getString("filtername", "")

            Log.e("filtername", filtername)


            var apiServices = APIClient.client.create(Api::class.java)


            val call = apiServices.getwordSearchlist(filtername.toString())

            call.enqueue(object : Callback<SearchListResponse> {
                override fun onResponse(
                    call: Call<SearchListResponse>,
                    response: Response<SearchListResponse>
                ) {

//                    progressBarhome.visibility = View.GONE
                    Log.e(ContentValues.TAG, response.toString())

                    if (response.isSuccessful) {

                        //Set the Adapter to the RecyclerView//

                        listOfsearch = response.body()!!.response

                        searchAdapter = context?.let { Search_Adapter(it, listOfsearch) }
                        searchdatarecyclerView.adapter = searchAdapter
                        searchAdapter!!.notifyDataSetChanged()

                        searchdatarecyclerView.addOnItemClickListener(object :
                            OnItemClickListener {
                            override fun onItemClicked(position: Int, view: View) {
                                // Your logic

                                val editor = sharedPreferences.edit()
//
                                var categorieid =
                                    searchAdapter!!.userList.get(position).categories_id
                                var categoriename =
                                    searchAdapter!!.userList.get(position).sub_category_name

                                Log.e("categorieid", "" + categorieid)
////
                                editor.putInt("category_id", categorieid)
                                editor.putString("searchcategoriename", categoriename)
                                editor.putInt("indetailswitch", 3)
                                editor.commit()
                                startActivity(Intent(context, UserProdctList_Screen::class.java))

                            }

                        })
                    }
                }

                override fun onFailure(call: Call<SearchListResponse>?, t: Throwable?) {
//                    progressBarhome.visibility = View.GONE
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
