package com.techarion.techarion.login_ui.model

import com.google.gson.annotations.SerializedName

data class User_Credentials(
    @SerializedName("access") val access: String?,
    @SerializedName("message") val message: String?,
    @SerializedName("user_details") val userDetails: userDetails?

)