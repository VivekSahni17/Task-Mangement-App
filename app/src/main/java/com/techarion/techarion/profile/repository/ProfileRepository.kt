package com.techarion.techarion.profile.repository

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.techarion.techarion.api.AllApi
import com.techarion.techarion.dashboard.Total
import com.techarion.techarion.profile.model.UpdateUserProfile
import com.techarion.techarion.profile.model.UserProfileDetails
import com.techarion.techarion.utils.NetworkResult
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

class ProfileRepository @Inject constructor(private val allApi: AllApi) {


    val _getUserProfileDetails = MutableLiveData<NetworkResult<UserProfileDetails>>()
    val getUserProfileDetails get() = _getUserProfileDetails

    suspend fun getUserProfileDetails() {
        try {
            _getUserProfileDetails.postValue(NetworkResult.Loading())
            val response = allApi.getUserProfileDetails()
            if (response.isSuccessful && response.body() != null) {
                _getUserProfileDetails.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {
                _getUserProfileDetails.postValue(NetworkResult.Error("Something Went Wrong"))

            }
        } catch (e: Exception) {

        }
    }

    /// used for update the user information
    private val _updateUserProfile = MutableLiveData<NetworkResult<String>>()
    val updateUserProfile get() = _updateUserProfile

//    image: MultipartBody.Part,
    suspend fun upDateProfileDetails(image: MultipartBody.Part,phone:RequestBody,designation:RequestBody,gender:RequestBody) {
        _updateUserProfile.postValue(NetworkResult.Loading())
        val response = allApi.updateUserProfile(image,phone,designation,gender)
        handleResponseForUpdateUserProfile(response, "Updated Successfully")

    }

    private  fun handleResponseForUpdateUserProfile(response: Response<JsonObject>, message: String){
        try {
            if (response.isSuccessful && response.body() != null) {
                _updateUserProfile.postValue(NetworkResult.Success(message))
            } else {
                _updateUserProfile.postValue(NetworkResult.Success( "Something went wrong"))
            }
        } catch (e:Exception){

        }
    }

}