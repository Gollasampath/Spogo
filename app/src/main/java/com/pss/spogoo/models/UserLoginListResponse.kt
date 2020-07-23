package com.pss.spogoo.models

import com.google.gson.annotations.SerializedName

data class UserLoginListResponse(
    @SerializedName("Status") val status: Boolean,
    @SerializedName("Message") val message: String,
    @SerializedName("Response") val response: UserLoginResponse,
    @SerializedName("code") val code: Int
)
