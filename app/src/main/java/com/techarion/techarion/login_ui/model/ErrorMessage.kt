package com.techarion.techarion.login_ui.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class ErrorMessage(
    @SerializedName("password") val password: List<String?>?,
    @SerializedName("status") val status: List<String?>?
)