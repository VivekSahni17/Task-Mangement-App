package com.techarion.techarion.profile.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techarion.techarion.dashboard.Total
import com.techarion.techarion.profile.model.UpdateUserProfile
import com.techarion.techarion.profile.model.UserProfileDetails
import com.techarion.techarion.profile.repository.ProfileRepository
import com.techarion.techarion.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val profileRepository: ProfileRepository): ViewModel()  {

    val getUserProfileDetails : LiveData<NetworkResult<UserProfileDetails>>
        get() = profileRepository.getUserProfileDetails

    fun getUserProfileDetails(){
        viewModelScope.launch {
            profileRepository.getUserProfileDetails()
        }
    }

//    image: MultipartBody.Part,
    val updateUserProfileDetails get() = profileRepository.updateUserProfile
    fun updateUerProfileDetails(image: MultipartBody.Part, phone:RequestBody,designation:RequestBody,gender:RequestBody){
        viewModelScope.launch {
            profileRepository.upDateProfileDetails(image,phone,designation,gender)
        }
    }


}