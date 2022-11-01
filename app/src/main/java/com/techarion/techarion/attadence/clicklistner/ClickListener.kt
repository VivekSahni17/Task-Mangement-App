package com.techarion.techarion.attadence.clicklistner

import android.widget.TextView
import com.techarion.techarion.create_new_task.model.GetAllEmp

interface ClickListener {
    fun onPresentClickListener(btPresent:TextView,items:GetAllEmp.Result)
    fun onAbsenceClickListener(btPresent:TextView,items: GetAllEmp.Result)
}