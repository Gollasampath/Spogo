package com.pss.spogoo.models

import com.google.gson.annotations.SerializedName

data class AccessoriesinDetailsListResponse(
    @SerializedName("Status") val status: Boolean,
    @SerializedName("Message") val message: String,
    @SerializedName("Response") val response: AccessoriesinDetailsResponse,
    @SerializedName("code") val code: Int
)
