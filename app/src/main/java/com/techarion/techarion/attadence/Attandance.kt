package com.techarion.techarion.attadence

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.techarion.techarion.R
import com.techarion.techarion.attadence.adapter.AttendanceAdapter
import com.techarion.techarion.attadence.clicklistner.ClickListener
import com.techarion.techarion.attadence.model.Attedane
import com.techarion.techarion.create_new_task.model.GetAllEmp
import com.techarion.techarion.create_new_task.viewmodel.CreateNewTaskViewModel
import com.techarion.techarion.databinding.FragmentAttandanceBinding
import com.techarion.techarion.employee.viewmodel.EmployeesViewModel
import com.techarion.techarion.utils.NetworkResult
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*


@Suppress("UNCHECKED_CAST")
class Attandance : Fragment(),ClickListener {
lateinit var binding:FragmentAttandanceBinding
    private val createNewTaskViewModel: CreateNewTaskViewModel by activityViewModels()
    private val employeeViewModel: EmployeesViewModel by activityViewModels()
    lateinit var getAllEmpAdapter:AttendanceAdapter
    var userId:Int = 0
    lateinit var selectedDate:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view  = inflater.inflate(R.layout.fragment_attandance, container, false)
        binding = FragmentAttandanceBinding.bind(view)
        return view
    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        binding.attendanceList.layoutManager = layoutManager

        selectedDate = toString()


        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        val formatted = current.format(formatter)
        Log.d("date",formatted)
        selectedDate = formatted

//        val fromDate = LocalDateTime.now().toInstant(ZoneOffset.UTC)
//      Log.d("date",fromDate.toString())
//        selectedDate = fromDate.toString()



        createNewTaskViewModel.getAllAttendanceUsers(selectedDate.toString())
        getAllEmpObserver()

//        binding.imCalender.setOnClickListener {
//            datePicker()
//        }


    }



    override fun onPresentClickListener(btPresent: TextView, items: GetAllEmp.Result) {
        val id  = Attedane(true,items.id,selectedDate.toString())
        employeeViewModel.userAttendance(id)
       bindObserverToUserAttendance()
    }

    override fun onAbsenceClickListener(btPresent: TextView, items: GetAllEmp.Result) {
        val id  = Attedane(false,items.id,selectedDate.toString())
        employeeViewModel.userAttendance(id)
        bindObserverToUserAttendance()
    }


    private fun getAllEmpObserver(){
        createNewTaskViewModel.getAllEmployess.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when(it){
                is NetworkResult.Success->{
                    val presents = it.data?.totalPresent
                    binding.totalPresent.text = presents.toString()
                    getAllEmpAdapter  = AttendanceAdapter(this,it.data?.results as List<GetAllEmp.Result>)
                    binding.attendanceList.adapter = getAllEmpAdapter
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

    private fun bindObserverToUserAttendance(){

        employeeViewModel.userAttendanceLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when(it){
                is NetworkResult.Success->{
                    createNewTaskViewModel.getAllAttendanceUsers(selectedDate.toString())
                    getAllEmpObserver()


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

    @SuppressLint("SetTextI18n", "NewApi")
    private fun datePicker(){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            // on below line we are passing context.
            requireActivity(),
            { view, year, monthOfYear, dayOfMonth ->
                // on below line we are setting
                // date to our text view.
                selectedDate = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year) }, year, month, day)
              val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
               val formatted = selectedDate.format(formatter)
              //binding.tvDate.text = formatted
        Toast.makeText(activity,formatted,Toast.LENGTH_SHORT).show()

        datePickerDialog.show()
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FF0C3823"))
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#FF0C3823"))

    }











}