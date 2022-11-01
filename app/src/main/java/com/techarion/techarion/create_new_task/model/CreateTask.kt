package com.techarion.techarion.create_new_task.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class CreateTask(
    @SerializedName("assigned_to") val assignedTo: Int?,
    @SerializedName("descriptions") val descriptions: String?,
    @SerializedName("title") val title: String?
)