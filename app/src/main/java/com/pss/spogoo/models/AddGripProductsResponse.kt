package com.pss.spogoo.models

import com.google.gson.annotations.SerializedName

data class AddGripProductsResponse(
    @SerializedName("repair_grip_id") val repair_grip_id: Int,
    @SerializedName("repair_categories_id") val repair_categories_id: Int,
    @SerializedName("repair_sub_categories_id") val repair_sub_categories_id: Int,
    @SerializedName("grip_name") val grip_name: String,
    @SerializedName("grip_brand") val grip_brand: String,
    @SerializedName("grip_image") val grip_image: String,
    @SerializedName("slider_images") val slider_images: String,
    @SerializedName("grip_model") val grip_model: String,
    @SerializedName("description") val description: String,
    @SerializedName("grip_type") val grip_type: String,
    @SerializedName("material") val material: String,
    @SerializedName("properties") val properties: String,
    @SerializedName("color") val color: String,
    @SerializedName("dimensions") val dimensions: String,
    @SerializedName("regular_price") val regular_price: Int,
    @SerializedName("special_price") val special_price: Int,
    @SerializedName("estimated_time") val estimated_time: String,
    @SerializedName("status") val status: String,
    @SerializedName("created_date") val created_date: String,
    @SerializedName("updated_date") val updated_date: String
)
