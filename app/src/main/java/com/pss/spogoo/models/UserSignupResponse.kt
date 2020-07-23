package com.pss.spogoo.models

import com.google.gson.annotations.SerializedName

data class UserSignupResponse(
    @SerializedName("login_type") val login_type: String,
    @SerializedName("full_name") val full_name: String,
    @SerializedName("email_id") val email_id: String,
    @SerializedName("mobile_number") val mobile_number: Long,
    @SerializedName("created_date") val created_date: String,
    @SerializedName("user_id") val user_id: Int,
    @SerializedName("user_type") val user_type: String
)

