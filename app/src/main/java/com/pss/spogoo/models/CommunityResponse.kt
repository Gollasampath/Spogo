package com.pss.spogoo.models

import com.google.gson.annotations.SerializedName

data class CommunityResponse(
    @SerializedName("community_id") val community_id: Int,
    @SerializedName("community_name") val community_name: String,
    @SerializedName("status") val status: String,
    @SerializedName("created_date") val created_date: String
)

