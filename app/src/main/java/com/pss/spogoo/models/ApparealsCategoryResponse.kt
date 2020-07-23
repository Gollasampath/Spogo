package com.pss.spogoo.models

import com.google.gson.annotations.SerializedName

data class ApparealsCategoryResponse(
    @SerializedName("apparel_cat_id") val apparel_cat_id: Int,
    @SerializedName("category_name") val category_name: String,
    @SerializedName("category_image") val category_image: String,
    @SerializedName("status") val status: String,
    @SerializedName("created_date") val created_date: String,
    @SerializedName("updated_date") val updated_date: String
)


