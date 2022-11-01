package com.techarion.techarion.login_ui.model

import com.google.gson.annotations.SerializedName

data class userDetails (
    @SerializedName("username") val userName: String?,
    @SerializedName("password") val password:String,
    @SerializedName("is_admin") val isAdmin: Boolean?,
    @SerializedName("is_employee") val isEmployee: Boolean?,
    //@SerializedName("is_teamLead") val isTeamLead: Boolean?,
)
