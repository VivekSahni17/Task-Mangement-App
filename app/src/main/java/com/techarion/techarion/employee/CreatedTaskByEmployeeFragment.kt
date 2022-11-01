package com.techarion.techarion.employee

import android.os.Bundle
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
import androidx.recyclerview.widget.RecyclerView
import com.techarion.techarion.R
import com.techarion.techarion.databinding.FragmentCreateNewTaskBinding
import com.techarion.techarion.databinding.FragmentCreatedTaskByEmployessBinding
import com.techarion.techarion.employee.adapter.GetAllAssignedTaskAdapter
import com.techarion.techarion.employee.clicklistner.OnAllTaskListener
import com.techarion.techarion.employee.model.AssignedTask
import com.techarion.techarion.employee.viewmodel.EmployeesViewModel
import com.techarion.techarion.utils.Cons
import com.techarion.techarion.utils.NetworkResult
import com.techarion.techarion.utils.TokenManager


@Suppress("UNCHECKED_CAST")
class CreatedTaskByEmployeeFragment : Fragment(),OnAllTaskListener {
  lateinit var binding:FragmentCreatedTaskByEmployessBinding
  lateinit var assignedTaskAdapter: GetAllAssignedTaskAdapter
  private val employeesViewModel:EmployeesViewModel by activityViewModels()
    //lateinit var tokenManager:TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_created_task_by_employess, container, false)
        binding = FragmentCreatedTaskByEmployessBinding.bind(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        binding.allTaskList.layoutManager = layoutManager

        employeesViewModel.getAllTaskCreatedByEmp()
        bindObserverToGetAllTask()

        binding.swipeContainer.setOnRefreshListener {
            employeesViewModel.getAllTaskCreatedByEmp()
            bindObserverToGetAllTask()

        }

//        binding.imBackArrow.setOnClickListener {
//            findNavController().popBackStack()
//        }


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
                }
                is NetworkResult.Error->{
                    Toast.makeText(activity,it.message, Toast.LENGTH_SHORT).show()

                }
                is NetworkResult.Loading->{
                    binding.progressBar.isVisible = true

                }
            }
        })

    }

    override fun onCLickListener(item: AssignedTask.Result) {
        val bundle = Bundle()
        bundle.putString(Cons.SLUG,item.slug)
    }


}