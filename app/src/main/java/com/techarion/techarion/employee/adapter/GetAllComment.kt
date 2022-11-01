package com.techarion.techarion.employee.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.techarion.techarion.databinding.PreviousDiscussionThisTaskBinding

import com.techarion.techarion.employee.model.GetAllComment
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class GetAllCommentAdapter( private val mList: List<GetAllComment.Result>) : RecyclerView.Adapter<GetAllCommentAdapter.ViewHOlder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHOlder {
        val itemBinding = PreviousDiscussionThisTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHOlder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHOlder, position: Int) {
        val items = mList[position]
        holder.bind(items)



    }

    override fun getItemCount(): Int = mList.size

    class ViewHOlder(private val itemBinding: PreviousDiscussionThisTaskBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(items: GetAllComment.Result) {
            itemBinding.tvname.text = items.employee?.fullName
            //itemBinding.tvDate.text = items.dateCreated
            itemBinding.tvComment.text = items.comment

            // val sdf3 = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
            val sdf3 = SimpleDateFormat("MMM dd HH:mm:ss zzz yyyy", Locale.getDefault())
            var d1: Date? = null

            try {
                d1 = sdf3.parse(items.dateCreated.toString())
                itemBinding.tvDate.text = d1.toString()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
}