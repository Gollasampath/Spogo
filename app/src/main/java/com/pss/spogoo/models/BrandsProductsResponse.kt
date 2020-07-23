package com.pss.spogoo.models

import com.google.gson.annotations.SerializedName

data class BrandsProductsResponse(
    @SerializedName("brands_id") val brands_id: Int,
    @SerializedName("brand_name") val brand_name: String,
    @SerializedName("brand_image") val brand_image: String,
    @SerializedName("status") val status: String,
    @SerializedName("created_date") val created_date: String,
    @SerializedName("updated_date") val updated_date: String
)
