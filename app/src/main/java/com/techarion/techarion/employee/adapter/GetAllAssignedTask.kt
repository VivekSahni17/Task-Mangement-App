package com.techarion.techarion.employee.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.techarion.techarion.databinding.TaskLayoutBinding
import com.techarion.techarion.employee.clicklistner.OnAllTaskListener
import com.techarion.techarion.employee.model.AssignedTask
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class GetAllAssignedTaskAdapter(private val onClickListener:OnAllTaskListener,private val mList: List<AssignedTask.Result>) : RecyclerView.Adapter<GetAllAssignedTaskAdapter.ViewHOlder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHOlder {
        val itemBinding = TaskLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHOlder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHOlder, position: Int) {
        val items = mList[position]
        holder.bind(items)



        holder.itemView.setOnClickListener {
            onClickListener.onCLickListener(items)
        }


    }

    override fun getItemCount(): Int = mList.size

    class ViewHOlder(private val itemBinding: TaskLayoutBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        @SuppressLint("NewApi")
        fun bind(items: AssignedTask.Result) {
            itemBinding.tvClintName.text = items.createdBy?.fullName
            itemBinding.tvEmpName.text = items.assignedTo?.fullName
            itemBinding.tvProgress.text = items.status
            itemBinding.description.text = items.descriptions
            itemBinding.tvTitle.text = items.title
            itemBinding.tvCreatedDate.text = items.dateCreated
        }
    }
}