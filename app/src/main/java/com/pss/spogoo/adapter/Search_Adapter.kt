package com.pss.spogoo.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pss.spogoo.R
import com.pss.spogoo.models.SearchResponse

class Search_Adapter(var context: Context, val userList: List<SearchResponse>) :
    RecyclerView.Adapter<Search_Adapter.ViewHolder>(), Filterable {
    lateinit var subuserslist: List<SearchResponse>


    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Search_Adapter.ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.search_adapter, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    @SuppressLint("WrongConstant")
    override fun onBindViewHolder(holder: Search_Adapter.ViewHolder, position: Int) {


        holder.search_text.text = userList.get(position).sub_category_name

        var subcat_id = userList.get(position).categories_id
        Log.e("categoryname", "" + userList.get(position).sub_category_name)
        Log.e("categoryid", "" + userList.get(position).categories_id)


    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val search_text = itemView.findViewById(R.id.search_text) as TextView


    }

    override fun getFilter(): Filter {
        return object : Filter() {
            private val filterResults = FilterResults()

            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {

                    subuserslist = userList

                } else {
                    val filteredList: ArrayList<SearchResponse> = ArrayList<SearchResponse>()


                    for (row: SearchResponse in userList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.sub_category_name.toLowerCase()
                                .contains(charString.toLowerCase())
                        ) {

                            Log.e("filter name", row.sub_category_name)
                            filteredList.add(row)

                        }

                    }

                    subuserslist = filteredList
                }

                val filterResults = Filter.FilterResults()
                filterResults.values = subuserslist
                return filterResults
            }

            override fun publishResults(
                charSequence: CharSequence,
                filterResults: Filter.FilterResults
            ) {

                if (filterResults.count == 0)
                else {
                    subuserslist = filterResults.values as List<SearchResponse>
                    notifyDataSetChanged()
                }


            }
        }
    }


}