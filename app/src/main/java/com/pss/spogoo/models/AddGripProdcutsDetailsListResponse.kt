package com.pss.spogoo.models

import com.google.gson.annotations.SerializedName

data class AddGripProdcutsDetailsListResponse(
    @SerializedName("Status") val status: Boolean,
    @SerializedName("Message") val message: String,
    @SerializedName("Response") val response: AddGripProductsDetailsResponse,
    @SerializedName("code") val code: Int
)
