package com.techarion.techarion.login_ui.login_view_model

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techarion.techarion.login_ui.model.User_Credentials
import com.techarion.techarion.login_ui.model.userDetails
import com.techarion.techarion.login_ui.repository.UserCredentialsRepository
import com.techarion.techarion.utils.Helper
import com.techarion.techarion.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val userCredentialsRepository: UserCredentialsRepository):ViewModel() {
    val userResponseLiveData:LiveData<NetworkResult<User_Credentials>>
    get() = userCredentialsRepository.userResponseLiveData


    fun loginCredentials(userDetails: userDetails){
        viewModelScope.launch {
            userCredentialsRepository.userCredentials(userDetails)
        }
    }

    fun validateCredentials(emailAddress: String, password: String, isLogin: Boolean) : Pair<Boolean, String> {

        var result = Pair(true, "")
        if(TextUtils.isEmpty(emailAddress)  || TextUtils.isEmpty(password)){
            result = Pair(false, "Please provide the credentials")
        }
//        else if(!Helper.isValidEmail(emailAddress)){
//            result = Pair(false, "Email is invalid")
//        }
        else if(!TextUtils.isEmpty(password) && password.length <= 4){
            result = Pair(false, "Password length should be greater than 4")
        }
        return result
    }


}