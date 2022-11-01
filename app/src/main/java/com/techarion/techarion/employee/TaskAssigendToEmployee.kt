package com.techarion.techarion.employee

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.techarion.techarion.MainActivity
import com.techarion.techarion.R
import com.techarion.techarion.databinding.FragmentTaskAssigendToEmployeeBinding
import com.techarion.techarion.employee.adapter.GetAllAssignedTaskAdapter
import com.techarion.techarion.employee.clicklistner.OnAllTaskListener
import com.techarion.techarion.employee.model.AssignedTask
import com.techarion.techarion.employee.viewmodel.EmployeesViewModel
import com.techarion.techarion.utils.Cons
import com.techarion.techarion.utils.NetworkResult
import java.nio.BufferUnderflowException


@Suppress("UNCHECKED_CAST")
class TaskAssignedToEmployee : Fragment(),OnAllTaskListener {
    lateinit var binding:FragmentTaskAssigendToEmployeeBinding
    private val employeesViewModel: EmployeesViewModel by activityViewModels()
    lateinit var assignedTaskAdapter: GetAllAssignedTaskAdapter
    private lateinit var mContext: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_task_assigend_to_employee, container, false)
        binding = FragmentTaskAssigendToEmployeeBinding.bind(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        binding.allTaskList.layoutManager = layoutManager
        employeesViewModel.getAllTaskAssignedToEmp()
        bindObserverToGetAllTask()

        binding.swipeContainer.setOnRefreshListener {
            employeesViewModel.getAllTaskAssignedToEmp()
            bindObserverToGetAllTask()




        }

    }

    private fun bindObserverToGetAllTask(){
        employeesViewModel.getAllAssignedTaskToEmp.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            binding.swipeContainer.isRefreshing = false
            when(it){
                is NetworkResult.Success->{
                    binding.allTaskList.layoutManager =  LinearLayoutManager(activity)
                    binding.tvNothingFound.isVisible = it.data?.results?.isEmpty() == true
                    assignedTaskAdapter = GetAllAssignedTaskAdapter(this,it.data?.results as List<AssignedTask.Result>)
                    binding.allTaskList.adapter = assignedTaskAdapter
                    Log.d("Error","Success")
                }
                is NetworkResult.Error->{
                    Toast.makeText(activity,it.message, Toast.LENGTH_SHORT).show()
                    Log.d("Error","Error")

                }
                is NetworkResult.Loading->{
                    binding.progressBar.isVisible = true
                    Log.d("Error","Loading")

                }
            }
        })

    }



    override fun onCLickListener(item: AssignedTask.Result) {
        val bundle = Bundle()
        bundle.putString(Cons.SLUG,item.slug)
        findNavController().navigate(R.id.action_taskAssignedToEmployee_to_taskDetailsFragment)
    }






}