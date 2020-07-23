package com.pss.spogoo.models

import com.google.gson.annotations.SerializedName

data class BrandsDetailsListResponse(
    @SerializedName("Status") val status: Boolean,
    @SerializedName("Message") val message: String,
    @SerializedName("Response") val response: List<BrandsDetailsResponse>,
    @SerializedName("code") val code: Int
)
