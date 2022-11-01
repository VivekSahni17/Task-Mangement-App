package com.techarion.techarion.attadence.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Attedane(
    @SerializedName("is_present") val isPresent: Boolean?,
    @SerializedName("owner") val owner: Int?,
    @SerializedName("date")val date:String
)