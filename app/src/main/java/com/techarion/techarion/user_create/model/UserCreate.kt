package com.techarion.techarion.user_create.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class UserCreate(
    @SerializedName("email") val email: String?,
    @SerializedName("employee_id") val employeeId: String?,
    @SerializedName("password") val password: String?,
    @SerializedName("full_name") val fullName:String?
)