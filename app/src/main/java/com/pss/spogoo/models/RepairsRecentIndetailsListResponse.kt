package com.pss.spogoo.models

import com.google.gson.annotations.SerializedName

data class RepairsRecentIndetailsListResponse(
    @SerializedName("Status") val status: Boolean,
    @SerializedName("Message") val message: String,
    @SerializedName("Response") val response: RepairsRecentInDetailsResponse,
    @SerializedName("code") val code: Int
)
