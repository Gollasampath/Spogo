package com.pss.spogoo.models

import com.google.gson.annotations.SerializedName

data class HealthProductsResponse(
    @SerializedName("health_products_id") val health_products_id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("health_pro_cat_id") val health_pro_cat_id: String,
    @SerializedName("description") val description: String,
    @SerializedName("product_image") val product_image: String,
    @SerializedName("slider_images") val slider_images: String,
    @SerializedName("quantity") val quantity: String,
    @SerializedName("color") val color: String,
    @SerializedName("size") val size: String,
    @SerializedName("regular_price") val regular_price: String,
    @SerializedName("sales_price") val sales_price: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("sku_code") val sku_code: String,
    @SerializedName("brand") val brand: String,
    @SerializedName("label") val label: String,
    @SerializedName("status") val status: String,
    @SerializedName("additional_info") val additional_info: String,
    @SerializedName("deleted") val deleted: String,
    @SerializedName("created_date") val created_date: String,
    @SerializedName("update_date") val update_date: String
)
