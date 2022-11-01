package com.techarion.techarion.create_new_task.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.techarion.techarion.api.AllApi
import com.techarion.techarion.api.UserApi
import com.techarion.techarion.create_new_task.model.CreateTask
import com.techarion.techarion.create_new_task.model.GetAllEmp
import com.techarion.techarion.employee.model.Comment
import com.techarion.techarion.login_ui.model.User_Credentials
import com.techarion.techarion.user_create.model.UserCreate
import com.techarion.techarion.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

class CreateNewTaskRespository @Inject constructor(private val allApi: AllApi) {


    val _getAllAllEmp = MutableLiveData<NetworkResult<GetAllEmp>>()
    val getAllEmployee get() = _getAllAllEmp

    private val _createTaskResponseLiveData = MutableLiveData<NetworkResult<CreateTask>>()
    val createTaskResponseLiveData: LiveData<NetworkResult<CreateTask>>
        get() = _createTaskResponseLiveData

    private val _createCommentResponseLiveData = MutableLiveData<NetworkResult<Comment>>()
    val createCommentResponseLiveData:LiveData<NetworkResult<Comment>>
    get() = _createCommentResponseLiveData


    suspend fun getAllAttendanceUser(date:String){
        try {
            _getAllAllEmp.postValue(NetworkResult.Loading())
            val response = allApi.getAllAttendanceUser(date)
            if (response.isSuccessful && response.body() != null) {
                _getAllAllEmp.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {
                _getAllAllEmp.postValue(NetworkResult.Error("Something Went Wrong"))

            }
        } catch (e: Exception){

        }

    }

    suspend fun getAllEmp(){
        try {
            _getAllAllEmp.postValue(NetworkResult.Loading())
            val response = allApi.getAllUsers()
            if (response.isSuccessful && response.body() != null) {
                _getAllAllEmp.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {
                _getAllAllEmp.postValue(NetworkResult.Error("Something Went Wrong"))

            }
        } catch (e: Exception){

        }
    }

    suspend fun createNewTask(createTask: CreateTask){
        _createTaskResponseLiveData.postValue(NetworkResult.Loading())
        val response = allApi.createTAsk(createTask)
        handleCreateNewTask(response)
    }

    suspend fun createComment(comment: Comment){
        _createTaskResponseLiveData.postValue(NetworkResult.Loading())
        val response = allApi.addComment(comment)
        handleCreateComment(response)
    }





    private fun handleCreateNewTask(response: Response<CreateTask>){
        try {
            if (response.isSuccessful && response.body() !=null){
                _createTaskResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
            }
            else if (response.errorBody()!=null){
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                _createTaskResponseLiveData.postValue(NetworkResult.Error(errorObj.getString("non_field_errors")))
            }
            else {
                _createTaskResponseLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        }
        catch (e:Exception){


        }
    }

    private fun handleCreateComment(response: Response<Comment>){
        try {
            if (response.isSuccessful && response.body() !=null){
                _createCommentResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
            }
            else if (response.errorBody()!=null){
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                _createCommentResponseLiveData.postValue(NetworkResult.Error(errorObj.getString("non_field_errors")))
                _createCommentResponseLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }
            else {
                _createCommentResponseLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        }
        catch (e:Exception){


        }
    }



}