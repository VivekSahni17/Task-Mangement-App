package com.techarion.techarion.api

import com.google.gson.JsonObject
import com.techarion.techarion.attadence.model.Attedane
import com.techarion.techarion.create_new_task.model.CreateTask
import com.techarion.techarion.create_new_task.model.GetAllEmp
import com.techarion.techarion.dashboard.Total
import com.techarion.techarion.employee.model.*
import com.techarion.techarion.profile.model.UpdateUserProfile
import com.techarion.techarion.profile.model.UserProfileDetails
import com.techarion.techarion.user_create.model.UserCreate
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface AllApi {

    @POST("accounts/api/admin/v1/employee-creation/")
    suspend fun createUser(@Body userCreate: UserCreate): Response<UserCreate>

    @POST("accounts/api/admin/v1/team-lead-creation/")
    suspend fun createdByTeamLead(@Body userCreate: UserCreate):Response<UserCreate>

    @GET("accounts/api/admin/v1/get-all-users/")
    suspend fun getAllUsers():Response<GetAllEmp>

    @GET("attendance/api/admin/v1/attendance-user/")
    suspend fun getAllAttendanceUser(@Query("date")date:String):Response<GetAllEmp>

    @POST("task/api/client/v1/task-create/")
    suspend fun createTAsk(@Body createTask: CreateTask):Response<CreateTask>

    @POST("task/api/client/v1/tasks-comment/")
    suspend fun addComment(@Body comment: Comment):Response<Comment>

    @GET("task/api/client/v1/created-tasks/")
    suspend fun createTaskByEMp():Response<AssignedTask>

    @GET("task/api/client/v1/assigned-tasks/")
    suspend fun getAssignedTaskToEmp():Response<AssignedTask>

    @GET("task/api/admin/v1/all-task/")
    suspend fun allTask(): Response<AssignedTask>

    @GET("task/api/client/v1/task-create/{slug}/")
    suspend fun getTaskDetails(@Path("slug")slug:String):Response<TaskDetails>

    @PUT("task/api/client/v1/task-create/{slug}/")
    suspend fun updateStatus(@Path("slug")slug:String,@Body updateStatus: UpdateStatus):Response<UpdateStatus>

    @GET("task/api/client/v1/tasks-related-comments/{slug}/")
    suspend fun getAllComment(@Path("slug")slug: String):Response<GetAllComment>


    @POST("attendance/api/admin/v1/take-attendance/")
    suspend fun postAttedance(@Body attedane: Attedane):Response<Attedane>

    @GET("attendance/api/admin/v1/attendance-emp-stats/")
    suspend fun getDashboardItem(): Response<Total>

    @GET("accounts/api/client/v1/login-user-profile/")
    suspend fun getUserProfileDetails():Response<UserProfileDetails>

    @Multipart
    @PUT("accounts/api/client/v1/profile-update/")
    suspend fun updateUserProfile(@Part file: MultipartBody.Part,@Part("phone")phone:RequestBody,@Part("occupation")occupation:RequestBody,@Part("gender")gender:RequestBody):Response<JsonObject>








}