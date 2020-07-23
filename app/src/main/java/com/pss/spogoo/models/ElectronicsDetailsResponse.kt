package com.pss.spogoo.models

import com.google.gson.annotations.SerializedName

data class ElectronicsDetailsResponse(
    @SerializedName("electronics_id") val electronics_id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("elec_categories_id") val elec_categories_id: Int,
    @SerializedName("description") val description: String,
    @SerializedName("product_image") val product_image: String,
    @SerializedName("slider_images") val slider_images: List<String>,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("color") val color: String,
    @SerializedName("size") val size: String,
    @SerializedName("regular_price") val regular_price: Int,
    @SerializedName("sales_price") val sales_price: Int,
    @SerializedName("gender") val gender: String,
    @SerializedName("sku_code") val sku_code: String,
    @SerializedName("brand") val brand: String,
    @SerializedName("label") val label: String,
    @SerializedName("status") val status: String,
    @SerializedName("additional_info") val additional_info: String,
    @SerializedName("deleted") val deleted: Int,
    @SerializedName("created_date") val created_date: String,
    @SerializedName("update_date") val update_date: String
)
