package com.techarion.techarion.create_new_task.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techarion.techarion.create_new_task.model.CreateTask
import com.techarion.techarion.create_new_task.model.GetAllEmp
import com.techarion.techarion.create_new_task.repository.CreateNewTaskRespository
import com.techarion.techarion.employee.model.Comment
import com.techarion.techarion.user_create.model.UserCreate
import com.techarion.techarion.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateNewTaskViewModel @Inject constructor(private val createNewTaskRespository: CreateNewTaskRespository):ViewModel() {
    val getAllEmployess : LiveData<NetworkResult<GetAllEmp>>
        get() = createNewTaskRespository.getAllEmployee

    val createNewTaskResponseLiveData: LiveData<NetworkResult<CreateTask>>
        get() = createNewTaskRespository.createTaskResponseLiveData

    val createCommentResponseLiveData: LiveData<NetworkResult<Comment>>
        get() = createNewTaskRespository.createCommentResponseLiveData


    fun getAllEmp(){
        viewModelScope.launch {
            createNewTaskRespository.getAllEmp()
        }
    }

    fun getAllAttendanceUsers(date:String){
        viewModelScope.launch {
            createNewTaskRespository.getAllAttendanceUser(date)
        }
    }

    fun  createNewTask(createTask:CreateTask){
        viewModelScope.launch {
            createNewTaskRespository.createNewTask(createTask)
        }
    }

    fun createComment(comment: Comment){
        viewModelScope.launch {
            createNewTaskRespository.createComment(comment)
        }
    }
}