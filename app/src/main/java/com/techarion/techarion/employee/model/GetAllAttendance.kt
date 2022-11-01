package com.techarion.techarion.employee.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class GetAllAttendance(
    @SerializedName("count") val count: Int?,
    @SerializedName("next") val next: Any?,
    @SerializedName("previous") val previous: Any?,
    @SerializedName("results") val results: List<Result?>?
) {
    @Keep
    data class Result(
        @SerializedName("email") val email: String?,
        @SerializedName("employee_id") val employeeId: String?,
        @SerializedName("full_name") val fullName: String?,
        @SerializedName("id") val id: Int?,
        @SerializedName("is_employee") val isEmployee: Boolean?,
        @SerializedName("is_present") val isPresent: Boolean?,
        @SerializedName("profile") val profile: Profile?,
        @SerializedName("slug") val slug: String?,
        @SerializedName("user_role") val userRole: String?
    ) {
        @Keep
        data class Profile(
            @SerializedName("image") val image: String?,
            @SerializedName("owner") val owner: Int?,
            @SerializedName("slug") val slug: String?
        )
    }
}