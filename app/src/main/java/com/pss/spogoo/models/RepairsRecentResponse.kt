package com.pss.spogoo.models

import com.google.gson.annotations.SerializedName

data class RepairsRecentResponse(

    @SerializedName("repairs_id") val repairs_id: Int,
    @SerializedName("product_name") val product_name: String,
    @SerializedName("repair_categories_id") val repair_categories_id: Int,
    @SerializedName("repair_sub_categories_id") val repair_sub_categories_id: Int,
    @SerializedName("description") val description: String,
    @SerializedName("slider_images") val slider_images: String,
    @SerializedName("product_image") val product_image: String,
    @SerializedName("product_model") val product_model: String,
    @SerializedName("color") val color: String,
    @SerializedName("regular_price") val regular_price: String,
    @SerializedName("special_price") val special_price: String,
    @SerializedName("size") val size: String,
    @SerializedName("properties") val properties: String,
    @SerializedName("gauge") val gauge: String,
    @SerializedName("status") val status: String,
    @SerializedName("material_info") val material_info: String,
    @SerializedName("additional_info") val additional_info: String,
    @SerializedName("tension") val tension: String,
    @SerializedName("deleted") val deleted: Int,
    @SerializedName("created_date") val created_date: String,
    @SerializedName("update_date") val update_date: String
)
