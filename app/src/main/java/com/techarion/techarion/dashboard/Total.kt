package com.techarion.techarion.dashboard


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Total(
    @SerializedName("total_absent") val totalAbsent: Int?,
    @SerializedName("total_employee") val totalEmployee: Int?,
    @SerializedName("total_present") val totalPresent: Int?
)