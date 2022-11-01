package com.techarion.techarion.login_ui.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.techarion.techarion.api.UserApi
import com.techarion.techarion.login_ui.model.User_Credentials
import com.techarion.techarion.login_ui.model.userDetails
import com.techarion.techarion.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject


class UserCredentialsRepository @Inject constructor(private val userApi: UserApi) {

    private val _userResponseLiveData = MutableLiveData<NetworkResult<User_Credentials>>()
    val userResponseLiveData:LiveData<NetworkResult<User_Credentials>>
    get() = _userResponseLiveData


    suspend fun userCredentials(userDetails: userDetails){
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = userApi.loginCredentials(userDetails)
        handleUserCredentialsResponse(response)
    }



    private fun handleUserCredentialsResponse(response: Response<User_Credentials>){
        try {
            if (response.isSuccessful && response.body() !=null){
                _userResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
            }
            else if (response.errorBody()!=null){
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
//                _userResponseLiveData.postValue(NetworkResult.Error(errorObj.getString("username")))
//                _userResponseLiveData.postValue(NetworkResult.Error(errorObj.getString("password")))
                _userResponseLiveData.postValue(NetworkResult.Error("Invalid credentials"))


            }
            else {
                _userResponseLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }

        }
        catch (e:Exception){


        }
    }

}