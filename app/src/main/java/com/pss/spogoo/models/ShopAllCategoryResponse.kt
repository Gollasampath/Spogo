package com.pss.spogoo.models

import com.google.gson.annotations.SerializedName

data class ShopAllCategoryResponse(
    @SerializedName("categories_id") val categories_id: Int,
    @SerializedName("category_name") val category_name: String,
    @SerializedName("category_image") val category_image: String,
    @SerializedName("zoom_image") val zoom_image: String,
    @SerializedName("category_icon") val category_icon: String,
    @SerializedName("status") val status: String,
    @SerializedName("created_date") val created_date: String,
    @SerializedName("updated_date") val updated_date: String,
    @SerializedName("sub_category_list") val sub_category_list: List<ShopSubCategoryResponse>
)
