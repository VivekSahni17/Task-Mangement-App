package com.techarion.techarion.employee

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import com.techarion.techarion.MainActivity
import com.techarion.techarion.R
import com.techarion.techarion.databinding.FragmentAllTaskFraagmentBinding
import com.techarion.techarion.employee.adapter.GetAllAssignedTaskAdapter
import com.techarion.techarion.employee.clicklistner.OnAllTaskListener
import com.techarion.techarion.employee.model.AssignedTask
import com.techarion.techarion.employee.viewmodel.EmployeesViewModel
import com.techarion.techarion.utils.Cons
import com.techarion.techarion.utils.NetworkResult
import com.techarion.techarion.utils.TokenManager


@Suppress("UNCHECKED_CAST")
class AllTaskFragment : Fragment(),OnAllTaskListener {

    lateinit var binding:FragmentAllTaskFraagmentBinding
    private val employeesViewModel: EmployeesViewModel by activityViewModels()

    lateinit var assignedTaskAdapter: GetAllAssignedTaskAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    private lateinit var mList:List<AssignedTask.Result>

    lateinit var tokenManager: TokenManager




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_all_task_fraagment, container, false)
        binding = FragmentAllTaskFraagmentBinding.bind(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        linearLayoutManager = LinearLayoutManager(requireActivity())
        binding.allTaskList.layoutManager = linearLayoutManager
        binding.allTaskList.setHasFixedSize(true)
        mList = listOf()
        tokenManager = TokenManager()

//        if (tokenManager.getAdmin(Cons.is_admin)==true && tokenManager.getTeamLead()==true && tokenManager.getEmp(Cons.is_Emp)==true){
//            employeesViewModel.getAllTask()
//            bindObserverToGetAllTask()
//        } else if (tokenManager.getAdmin(Cons.is_admin)==false && tokenManager.getTeamLead()==true && tokenManager.getEmp(Cons.is_Emp)==false){
//
//        } else if (tokenManager.getAdmin(Cons.is_admin)==false && tokenManager.getTeamLead()==false && tokenManager.getEmp(Cons.is_Emp)==true){
//
//        }


        employeesViewModel.getAllTask()
        bindObserverToGetAllTask()

        binding.swipeContainer.setOnRefreshListener {
            employeesViewModel.getAllTask()
            bindObserverToGetAllTask()
        }

   }

    private fun bindObserverToGetAllTask(){
        employeesViewModel.getAllAssignedTaskToEmp.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            binding.swipeContainer.isRefreshing = false
            when(it){
                is NetworkResult.Success->{
                    binding.tvNothingFound.isVisible = it.data?.results?.isEmpty() == true
                    assignedTaskAdapter  = GetAllAssignedTaskAdapter(this,it.data?.results as List<AssignedTask.Result>)
                    binding.allTaskList.adapter = assignedTaskAdapter
                }is NetworkResult.Error->{ Toast.makeText(activity,it.message, Toast.LENGTH_SHORT).show()
                }is NetworkResult.Loading->{ binding.progressBar.isVisible = true } }
        })

    }

    override fun onCLickListener(item: AssignedTask.Result) {
        val bundle = Bundle()
        bundle.putString(Cons.SLUG,item.slug)
        bundle.putInt(Cons.id,item.id)
        findNavController().navigate(R.id.action_allTaskFragment_to_taskDetailsFragment,bundle)
    }





}