package com.techarion.techarion.attadence.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.techarion.techarion.R
import com.techarion.techarion.attadence.clicklistner.ClickListener
import com.techarion.techarion.create_new_task.model.GetAllEmp
import com.techarion.techarion.utils.Cons
import com.techarion.techarion.utils.TokenManager
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer


class AttendanceAdapter(private val onClickListener: ClickListener, private val mList: List<GetAllEmp.Result>) : RecyclerView.Adapter<AttendanceAdapter.ViewHolder>() {

    lateinit var tokenManger:TokenManager
    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_attadance, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        tokenManger =  TokenManager()

        val items = mList[position]
        holder.textView.text = items.fullName

        if (items.isPresent==true){
            holder.btnPresent.setBackgroundResource(R.drawable.green_bg)
            holder.btnAbsence.setBackgroundResource(R.drawable.absance_circle)
        } else if (items.isPresent==false){
            holder.btnAbsence.setBackgroundResource(R.drawable.red_bg)
            holder.btnPresent.setBackgroundResource(R.drawable.absance_circle)
        }




        if (tokenManger.getTeamLead()==false && tokenManger.getAdmin(Cons.is_admin)==false && tokenManger.getEmp(Cons.is_Emp)==true){
            holder.btnAbsence.isEnabled = false
            holder.btnPresent.isEnabled = false
        }

        holder.btnPresent.setOnClickListener {
            onClickListener.onPresentClickListener(holder.btnPresent,items)
        }

        holder.btnAbsence.setOnClickListener {
            onClickListener.onAbsenceClickListener(holder.btnAbsence,items)
        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textView: TextView = itemView.findViewById(R.id.tv_emp_name)
        val btnPresent:TextView = itemView.findViewById(R.id.bt_present)
        val btnAbsence:TextView = itemView.findViewById(R.id.bt_absence)
    }
}