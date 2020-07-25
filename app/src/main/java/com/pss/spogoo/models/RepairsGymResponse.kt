package com.pss.spogoo.models

import com.google.gson.annotations.SerializedName

data class RepairsGymResponse(
    @SerializedName("repair_sub_categories_id") val repair_sub_categories_id : Int,
    @SerializedName("repair_categories_id") val repair_categories_id : Int,
    @SerializedName("sub_category_name") val sub_category_name : String,
    @SerializedName("sub_category_image") val sub_category_image : String,
    @SerializedName("status") val status : String,
    @SerializedName("created_date") val created_date : String,
    @SerializedName("updated_date") val updated_date : String
)
