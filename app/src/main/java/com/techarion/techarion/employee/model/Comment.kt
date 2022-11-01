package com.techarion.techarion.employee.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Comment(
    @SerializedName("comment") val comment: String?,
    @SerializedName("owner") val owner: Int?
)