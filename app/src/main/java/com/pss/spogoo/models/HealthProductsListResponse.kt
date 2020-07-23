package com.pss.spogoo.models

import com.google.gson.annotations.SerializedName

data class HealthProductsListResponse(
    @SerializedName("Status") val status: Boolean,
    @SerializedName("Message") val message: String,
    @SerializedName("Response") val response: List<HealthProductsResponse>,
    @SerializedName("code") val code: Int
)
