package com.pss.spogoo.models

import com.google.gson.annotations.SerializedName

data class ApparealsDetailsListResponse(
    @SerializedName("Status") val status: Boolean,
    @SerializedName("Message") val message: String,
    @SerializedName("Response") val response: ApparealsDetsilsResponse,
    @SerializedName("code") val code: Int
)
