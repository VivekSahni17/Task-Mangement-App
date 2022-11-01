package com.techarion.techarion.employee

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.techarion.techarion.R
import com.techarion.techarion.create_new_task.viewmodel.CreateNewTaskViewModel
import com.techarion.techarion.databinding.FragmentTaskDetailsBinding
import com.techarion.techarion.employee.adapter.GetAllCommentAdapter
import com.techarion.techarion.employee.model.Comment
import com.techarion.techarion.employee.model.GetAllComment
import com.techarion.techarion.employee.model.UpdateStatus
import com.techarion.techarion.employee.viewmodel.EmployeesViewModel
import com.techarion.techarion.utils.Cons
import com.techarion.techarion.utils.NetworkResult
import okhttp3.internal.assertThreadHoldsLock


class TaskDetailsFragment : Fragment() {
    lateinit var binding:FragmentTaskDetailsBinding
    private var selectedFirstAidType: Int = 0
    private var selectType: String = "Android"
    private val employeesViewModel: EmployeesViewModel by activityViewModels()
    private val createNewTaskViewModel: CreateNewTaskViewModel by activityViewModels()
    lateinit var allCommentAdapter:GetAllCommentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_task_details, container, false)
        binding = FragmentTaskDetailsBinding.bind(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setStatus()

        employeesViewModel.getAllComment(arguments?.getString(Cons.SLUG).toString())
        bindObserverGetAllComment()

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        binding.listComment.layoutManager = layoutManager

        employeesViewModel.getTaskDetails(arguments?.getString(Cons.SLUG).toString())
        bindObserverToGetTaskDetails()

        if (binding.tvStatus.text=="Done"){
            binding.spType.isEnabled = true
        }

        binding.imSend.setOnClickListener {
        val comment = binding.etComment.text.toString()

        val owner=  arguments?.getInt(Cons.id)
            val cmt  = Comment(comment,owner)
            createNewTaskViewModel.createComment(cmt)
            bindObserverToCreateComment()
        }
        binding.swipeContainer.setOnRefreshListener {
            employeesViewModel.getTaskDetails(arguments?.getString(Cons.SLUG).toString())
            bindObserverToGetTaskDetails()
            employeesViewModel.getAllComment(arguments?.getString(Cons.SLUG).toString())
            bindObserverGetAllComment()

        }




    }

    private fun setStatus() {
        val typeList =
            arrayOf("Click here to select", "INPROGRESS", "DONE")
        val adapter: ArrayAdapter<String> = object :
            ArrayAdapter<String>(
                requireContext(),
                androidx.appcompat.R.layout.select_dialog_item_material, typeList
            ) {
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view: TextView =
                    super.getDropDownView(position, convertView, parent) as TextView

                if (position == binding.spType.selectedItemPosition && position != 0) {
                    view.setTextColor(Color.parseColor("#000000"))
                }
                if (position == 0) {
                    view.setTextColor(Color.parseColor("#999999"))
                }
                return view
            }

            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }
        }
        binding.spType.adapter = adapter
        binding.spType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position > 0) {
                    selectedFirstAidType = if (position == 1) 1 else 0
                }
                selectType = parent?.getItemAtPosition(position).toString()
                if (selectType=="Click here to select"){

                } else if (selectType=="INPROGRESS" || selectType =="DONE"){
                    val status = UpdateStatus(selectType)
                    employeesViewModel.updateTaskStatus(arguments?.getString(Cons.SLUG).toString(),status)
                    updateTaskStatus()
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    private fun bindObserverToGetTaskDetails(){
        employeesViewModel.getTaskDetails.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            binding.swipeContainer.isRefreshing = false
            when(it){
                is NetworkResult.Success->{
                    binding.tvCreaterName.text = it.data?.createdBy?.fullName
                    binding.tvEmpName.text = it.data?.assignedTo?.fullName
                    binding.tvProgress.text = it.data?.status
                    binding.tvDescription.text=it.data?.descriptions
                    binding.tvTitle.text = it.data?.title
                    binding.taskCreateDate.text = it.data?.dateCreated

                }
                is NetworkResult.Error->{
                    Toast.makeText(activity,it.message,Toast.LENGTH_SHORT).show()

                }
                is NetworkResult.Loading->{
                    binding.progressBar.isVisible = true

                }
            }
        })
    }

    private fun updateTaskStatus(){
        employeesViewModel.updateTaskStatusResponseLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false

            when(it){
                is NetworkResult.Success->{
                    Toast.makeText(activity,selectType,Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_taskDetailsFragment_to_allTaskFragment)

                }
                is NetworkResult.Error->{
                    Toast.makeText(activity,it.message,Toast.LENGTH_SHORT).show()


                }
                is NetworkResult.Loading->{
                    binding.progressBar.isVisible=true
                }
            }
        })
    }

    private fun bindObserverToCreateComment(){
        createNewTaskViewModel.createCommentResponseLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false

            when(it){
                is NetworkResult.Success->{

                    //Toast.makeText(activity,"Successfully added comment",Toast.LENGTH_SHORT).show()
                    employeesViewModel.getAllComment(arguments?.getString(Cons.SLUG).toString())
                    bindObserverGetAllComment()


                }
                is NetworkResult.Error->{
                    Toast.makeText(activity,it.message,Toast.LENGTH_SHORT).show()


                }
                is NetworkResult.Loading->{
                    binding.progressBar.isVisible=true
                }
            }
        })

    }

    private fun bindObserverGetAllComment(){
        employeesViewModel.getAllCommentLiveDat.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            binding.swipeContainer.isRefreshing = false

            when(it){
                is NetworkResult.Success->{
                    allCommentAdapter = GetAllCommentAdapter(it.data?.results as List<GetAllComment.Result>)
                    binding.listComment.adapter = allCommentAdapter



                }
                is NetworkResult.Error->{
                    Toast.makeText(activity,it.message,Toast.LENGTH_SHORT).show()


                }
                is NetworkResult.Loading->{
                    binding.progressBar.isVisible=true
                }
            }
        })

    }





}