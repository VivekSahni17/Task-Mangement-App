package com.techarion.techarion.user_create.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techarion.techarion.login_ui.model.User_Credentials
import com.techarion.techarion.login_ui.model.userDetails
import com.techarion.techarion.user_create.model.UserCreate
import com.techarion.techarion.user_create.repository.UserCreateRepository
import com.techarion.techarion.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateUserViewModel @Inject constructor(private val userCreateRepository: UserCreateRepository):ViewModel() {


    val createEmpResponseLiveData: LiveData<NetworkResult<UserCreate>>
        get() = userCreateRepository.userCreateResponseLiveData

    fun createEmpId(userCreate: UserCreate){
        viewModelScope.launch {
            userCreateRepository.userCreate(userCreate)
        }
    }

    fun createdTeamEmp(userCreate: UserCreate){
        viewModelScope.launch {
            userCreateRepository.createdByTeamLead(userCreate)
        }
    }
}