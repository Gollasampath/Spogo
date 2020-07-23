package com.pss.spogoo.models

import com.google.gson.annotations.SerializedName

data class FitnessExcerseResponse(
    @SerializedName("fit_exce_id") val fit_exce_id: Int,
    @SerializedName("product_name") val product_name: String,
    @SerializedName("short_description") val short_description: String,
    @SerializedName("description") val description: String,
    @SerializedName("categories_id") val categories_id: Int,
    @SerializedName("sub_categories_id") val sub_categories_id: String,
    @SerializedName("product_image") val product_image: String,
    @SerializedName("slider_images") val slider_images: String,
    @SerializedName("price") val price: Int,
    @SerializedName("sales_price") val sales_price: String,
    @SerializedName("offer_from") val offer_from: String,
    @SerializedName("offer_to") val offer_to: String,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("age") val age: String,
    @SerializedName("size") val size: String,
    @SerializedName("color") val color: String,
    @SerializedName("sku_code") val sku_code: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("latitude") val latitude: String,
    @SerializedName("longitude") val longitude: String,
    @SerializedName("size_dimensions") val size_dimensions: String,
    @SerializedName("weight_product") val weight_product: String,
    @SerializedName("playing_level") val playing_level: String,
    @SerializedName("grip_size") val grip_size: String,
    @SerializedName("manufacturer_name") val manufacturer_name: String,
    @SerializedName("manufacturer_brand") val manufacturer_brand: String,
    @SerializedName("status") val status: String,
    @SerializedName("created_date") val created_date: String,
    @SerializedName("updated_date") val updated_date: String
)
