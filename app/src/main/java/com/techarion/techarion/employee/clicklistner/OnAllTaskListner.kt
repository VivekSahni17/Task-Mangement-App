package com.techarion.techarion.employee.clicklistner

import com.techarion.techarion.employee.model.AssignedTask

interface OnAllTaskListener {
    fun onCLickListener(item:AssignedTask.Result)
}