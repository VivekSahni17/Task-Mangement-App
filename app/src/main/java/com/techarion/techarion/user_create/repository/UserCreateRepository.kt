package com.techarion.techarion.user_create.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.techarion.techarion.api.AllApi
import com.techarion.techarion.api.UserApi
import com.techarion.techarion.login_ui.model.User_Credentials
import com.techarion.techarion.login_ui.model.userDetails
import com.techarion.techarion.user_create.model.UserCreate
import com.techarion.techarion.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class UserCreateRepository @Inject constructor(private val allApi: AllApi) {

    private val _userCreateResponseLiveData = MutableLiveData<NetworkResult<UserCreate>>()
    val userCreateResponseLiveData: LiveData<NetworkResult<UserCreate>>
        get() = _userCreateResponseLiveData


    suspend fun userCreate(userCreate: UserCreate){
        _userCreateResponseLiveData.postValue(NetworkResult.Loading())
        val response = allApi.createUser(userCreate)
        handleUserCreate(response)
    }

    suspend fun createdByTeamLead(userCreate: UserCreate){
        _userCreateResponseLiveData.postValue(NetworkResult.Loading())
        val response = allApi.createdByTeamLead(userCreate)
        handleUserCreate(response)

    }

    private fun handleUserCreate(response: Response<UserCreate>){
        try {
            if (response.isSuccessful && response.body() !=null){
                _userCreateResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
            }
            else if (response.errorBody()!=null){
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                _userCreateResponseLiveData.postValue(NetworkResult.Error(errorObj.getString("non_field_errors")))
            }
            else {
                _userCreateResponseLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        }
        catch (e:Exception){


        }
    }
}