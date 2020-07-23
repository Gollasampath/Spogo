package com.pss.spogoo.models

import com.google.gson.annotations.SerializedName

data class SortApparelsListResponse(
    @SerializedName("Status") val status: Boolean,
    @SerializedName("Message") val message: String,
    @SerializedName("Response") val response: List<SortApparelsResponse>,
    @SerializedName("code") val code: Int
)
