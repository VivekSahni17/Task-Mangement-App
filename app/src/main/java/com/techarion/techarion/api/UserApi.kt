package com.techarion.techarion.api

import com.techarion.techarion.login_ui.model.User_Credentials
import com.techarion.techarion.login_ui.model.userDetails
import com.techarion.techarion.user_create.model.UserCreate
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {
    @POST("accounts/api/client/v1/user-login/")
    suspend fun loginCredentials (@Body userDetails:userDetails):Response<User_Credentials>

//    @POST("accounts/api/admin/v1/employee-creation/")
//    suspend fun createUser(@Body userCreate:UserCreate):Response<UserCreate>

}