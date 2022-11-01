package com.techarion.techarion.employee.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class TaskDetails(
    @SerializedName("assigned_to") val assignedTo: AssignedTo?,
    @SerializedName("created_by") val createdBy: CreatedBy?,
    @SerializedName("date_created") val dateCreated: String?,
    @SerializedName("descriptions") val descriptions: String?,
    @SerializedName("id") val id: Int?,
    @SerializedName("slug") val slug: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("title") val title: String?
) {
    @Keep
    data class AssignedTo(
        @SerializedName("email") val email: String?,
        @SerializedName("employee_id") val employeeId: String?,
        @SerializedName("full_name") val fullName: String?,
        @SerializedName("id") val id: Int?,
        @SerializedName("slug") val slug: String?
    )

    @Keep
    data class CreatedBy(
        @SerializedName("email") val email: String?,
        @SerializedName("employee_id") val employeeId: String?,
        @SerializedName("full_name") val fullName: String?,
        @SerializedName("id") val id: Int?,
        @SerializedName("slug") val slug: String?
    )
}