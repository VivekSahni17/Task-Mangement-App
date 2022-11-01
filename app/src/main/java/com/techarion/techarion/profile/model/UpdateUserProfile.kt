package com.techarion.techarion.profile.model

import com.google.gson.annotations.SerializedName

data class UpdateUserProfile(
    @SerializedName("phone") val phoneNumber: String?,
    //@SerializedName("gender") val gender: String?,
    //@SerializedName("image") val image: String?,
    //@SerializedName("full_name") val fullName: String?,
    //@SerializedName("occupation") val occupation: String?,
    //@SerializedName("date_of_join") val dateOfJoin: String,

)