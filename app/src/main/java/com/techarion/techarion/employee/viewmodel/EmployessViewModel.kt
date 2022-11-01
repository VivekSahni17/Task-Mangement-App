package com.techarion.techarion.employee.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techarion.techarion.attadence.model.Attedane
import com.techarion.techarion.create_new_task.model.GetAllEmp
import com.techarion.techarion.dashboard.Total
import com.techarion.techarion.employee.model.AssignedTask
import com.techarion.techarion.employee.model.GetAllComment
import com.techarion.techarion.employee.model.TaskDetails
import com.techarion.techarion.employee.model.UpdateStatus
import com.techarion.techarion.employee.repository.EmployeesRepository
import com.techarion.techarion.user_create.model.UserCreate
import com.techarion.techarion.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmployeesViewModel @Inject constructor(private val employeesRepository: EmployeesRepository): ViewModel() {

    val getAllAssignedTaskToEmp : LiveData<NetworkResult<AssignedTask>>
         get() = employeesRepository.getAllAssinedTask

    val getAllCommentLiveDat:LiveData<NetworkResult<GetAllComment>>
    get() = employeesRepository.getAllCommentResponseLiveData

    val updateTaskStatusResponseLiveData get() = employeesRepository.updateTaskStatusResponseLiveData

    val userAttendanceLiveData: LiveData<NetworkResult<Attedane>>
        get() = employeesRepository.userAttendanceResponseLiveData


    val getTaskDetails : LiveData<NetworkResult<TaskDetails>>
        get() = employeesRepository._getAllTaskDetails

    val getDashboardStats : LiveData<NetworkResult<Total>>
        get() = employeesRepository.getDashboardStats

    fun getDashboardStats(){
        viewModelScope.launch {
            employeesRepository.getDashboardStats()
        }
    }

    fun getAllTaskCreatedByEmp(){
        viewModelScope.launch {
            employeesRepository.getAllTaskCreatedByEmp()
        }
    }

    fun getAllTaskAssignedToEmp(){
        viewModelScope.launch {
            employeesRepository.getAllTaskAssignedToEmp()
        }
    }

    fun getAllTask(){
        viewModelScope.launch {
            employeesRepository.getAllTask()
        }
    }

    fun getTaskDetails(slug:String){
        viewModelScope.launch {
            employeesRepository.getTaskDetails(slug)
        }
    }

    fun updateTaskStatus(slug:String,updateStatus: UpdateStatus){
        viewModelScope.launch {
            employeesRepository.updateTaskStatus(slug,updateStatus)
        }
    }

    fun getAllComment(slug: String){
        viewModelScope.launch {
            employeesRepository.getAllComment(slug)
        }
    }

    fun userAttendance(attedane: Attedane){
        viewModelScope.launch {
            employeesRepository.userAttendance(attedane)
        }
    }



}