package com.techarion.techarion.employee.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class GetAllComment(
    @SerializedName("count") val count: Int?,
    @SerializedName("next") val next: Any?,
    @SerializedName("previous") val previous: Any?,
    @SerializedName("results") val results: List<Result>
) {
    @Keep
    data class Result(
        @SerializedName("comment") val comment: String?,
        @SerializedName("date_created") val dateCreated: String?,
        @SerializedName("employee") val employee: Employee?,
        @SerializedName("id") val id: Int?,
        @SerializedName("slug") val slug: String?
    ) {
        @Keep
        data class Employee(
            @SerializedName("full_name") val fullName: String?,
            @SerializedName("id") val id: Int?,
            @SerializedName("slug") val slug: String?
        )
    }
}