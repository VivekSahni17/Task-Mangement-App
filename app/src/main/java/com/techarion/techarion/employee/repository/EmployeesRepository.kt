package com.techarion.techarion.employee.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.techarion.techarion.api.AllApi
import com.techarion.techarion.attadence.model.Attedane
import com.techarion.techarion.create_new_task.model.GetAllEmp
import com.techarion.techarion.dashboard.Total
import com.techarion.techarion.employee.model.AssignedTask
import com.techarion.techarion.employee.model.GetAllComment
import com.techarion.techarion.employee.model.TaskDetails
import com.techarion.techarion.employee.model.UpdateStatus
import com.techarion.techarion.user_create.model.UserCreate
import com.techarion.techarion.utils.NetworkResult
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

class EmployeesRepository @Inject constructor(private val  allApi: AllApi) {

    val _getAllAssinedTask = MutableLiveData<NetworkResult<AssignedTask>>()
    val getAllAssinedTask get() = _getAllAssinedTask

    private val _getAllTask = MutableLiveData<NetworkResult<AssignedTask>>()
    val getAllTask get() = _getAllTask


    val _getAllTaskDetails = MutableLiveData<NetworkResult<TaskDetails>>()
    val getAllTaskDetails get() = _getAllTaskDetails


    val _getAllCommentRespinseLiveData = MutableLiveData<NetworkResult<GetAllComment>>()
    val getAllCommentResponseLiveData get() = _getAllCommentRespinseLiveData



    private val _updateTaskStatusResponseLiveData = MutableLiveData<NetworkResult <String>>()
    val updateTaskStatusResponseLiveData get() = _updateTaskStatusResponseLiveData


    private val _userAttendanceResponseLiveData = MutableLiveData<NetworkResult<Attedane>>()
    val  userAttendanceResponseLiveData: LiveData<NetworkResult<Attedane>>
        get() = _userAttendanceResponseLiveData


    val _getDashboardStats = MutableLiveData<NetworkResult<Total>>()
    val getDashboardStats get() = _getDashboardStats

    suspend fun getDashboardStats(){
        try {
            _getDashboardStats.postValue(NetworkResult.Loading())
            val response = allApi.getDashboardItem()
            if (response.isSuccessful && response.body() != null) {
                _getDashboardStats.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {
                _getDashboardStats.postValue(NetworkResult.Error("Something Went Wrong"))

            }
        } catch (e: Exception){

        }
    }





    suspend fun getAllTaskCreatedByEmp(){
        try {
            _getAllAssinedTask.postValue(NetworkResult.Loading())
            val response = allApi.createTaskByEMp()
            if (response.isSuccessful && response.body() != null) {
                _getAllAssinedTask.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {
                _getAllAssinedTask.postValue(NetworkResult.Error("Something Went Wrong"))

            }
        } catch (e: Exception){

        }
    }

    suspend fun getAllComment(slug: String){

        try {
            _getAllCommentRespinseLiveData.postValue(NetworkResult.Loading())
            val response = allApi.getAllComment(slug)
            if (response.isSuccessful && response.body() != null) {
                _getAllCommentRespinseLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {
                _getAllCommentRespinseLiveData.postValue(NetworkResult.Error("Something Went Wrong"))

            }
        } catch (e: Exception){

        }

    }




    suspend fun getAllTaskAssignedToEmp(){
        try {
            _getAllAssinedTask.postValue(NetworkResult.Loading())
            val response = allApi.getAssignedTaskToEmp()
            if (response.isSuccessful && response.body() != null) {
                _getAllAssinedTask.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {
                _getAllAssinedTask.postValue(NetworkResult.Error("Something Went Wrong"))

            }
        } catch (e: Exception){

        }
    }

    suspend fun getAllTask(){
        try {
            _getAllAssinedTask.postValue(NetworkResult.Loading())
            val response = allApi.allTask()
            if (response.isSuccessful && response.body() != null) {
                _getAllAssinedTask.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {
                _getAllAssinedTask.postValue(NetworkResult.Error("Something Went Wrong"))

            }
        } catch (e: Exception){

        }
    }


    suspend fun getTaskDetails(slug:String){
        try {
            _getAllTaskDetails.postValue(NetworkResult.Loading())
            val response = allApi.getTaskDetails(slug)
            if (response.isSuccessful && response.body() != null) {
                _getAllTaskDetails.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {
                _getAllTaskDetails.postValue(NetworkResult.Error("Something Went Wrong"))

            }
        } catch (e: Exception){

        }
    }

    suspend fun updateTaskStatus(slug:String,updateStatus: UpdateStatus){
        _updateTaskStatusResponseLiveData.postValue(NetworkResult.Loading())
        val response = allApi.updateStatus(slug,updateStatus)
        handleTaskStatusResponse(response,"Updated Billing Address")
    }

    private fun handleTaskStatusResponse(response: Response<UpdateStatus>, message:String){
        if (response.isSuccessful && response.body() !=null){
            _updateTaskStatusResponseLiveData.postValue(NetworkResult.Success(message))
        } else {
            _updateTaskStatusResponseLiveData.postValue(NetworkResult.Success("Something went wrong"))
        }
    }

    suspend fun userAttendance(attedane: Attedane){
        _userAttendanceResponseLiveData.postValue(NetworkResult.Loading())
        val response = allApi.postAttedance(attedane)
        handleUserAttendance(response)
    }



    private fun handleUserAttendance(response: Response<Attedane>){
        try {
            if (response.isSuccessful && response.body() !=null){
                _userAttendanceResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
            }
            else if (response.errorBody()!=null){
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                _userAttendanceResponseLiveData.postValue(NetworkResult.Error(errorObj.getString("non_field_errors")))
            }
            else {
                _userAttendanceResponseLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        }
        catch (e:Exception){


        }
    }
}

