package com.pss.spogoo.models

import com.google.gson.annotations.SerializedName

data class ElectronicsDetailsListResponse(
    @SerializedName("Status") val status: Boolean,
    @SerializedName("Message") val message: String,
    @SerializedName("Response") val response: ElectronicsDetailsResponse,
    @SerializedName("code") val code: Int
)
