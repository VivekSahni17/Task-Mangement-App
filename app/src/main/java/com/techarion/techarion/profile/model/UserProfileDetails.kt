package com.techarion.techarion.profile.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class UserProfileDetails(
    @SerializedName("email") val email: String?,
    @SerializedName("employee_id") val employeeId: String?,
    @SerializedName("full_name") val fullName: String?,
    @SerializedName("id") val id: Int?,
    @SerializedName("profile") val profile: Profile?,
    @SerializedName("slug") val slug: String?,
    @SerializedName("user_role") val userRole: String?
) {
    @Keep
    data class Profile(
        @SerializedName("date_created") val dateCreated: String?,
        @SerializedName("date_of_birth") val dateOfBirth: String?,
        @SerializedName("date_of_join") val dateOfJoin: Any?,
        @SerializedName("experience") val experience: Any?,
        @SerializedName("gender") val gender: String?,
        @SerializedName("image") val image: String?,
        @SerializedName("occupation") val occupation: String?,
        @SerializedName("owner") val owner: Int?,
        @SerializedName("phone") val phone: Long?,
        @SerializedName("slug") val slug: String?
    )
}