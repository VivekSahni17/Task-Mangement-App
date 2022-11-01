package com.techarion.techarion.create_new_task

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.techarion.techarion.R
import com.techarion.techarion.create_new_task.adapter.GetAllEmployeeAdapter
import com.techarion.techarion.create_new_task.model.CreateTask
import com.techarion.techarion.create_new_task.model.GetAllEmp
import com.techarion.techarion.create_new_task.viewmodel.CreateNewTaskViewModel
import com.techarion.techarion.databinding.FragmentCreateNewTaskBinding
import com.techarion.techarion.utils.NetworkResult


class CreateNewTaskFragment : Fragment() {
    lateinit var binding:FragmentCreateNewTaskBinding
    private var selectedEmp: Int = 0
    private var selectType: String = "Android"
    lateinit var getAllEmpAdapter:GetAllEmployeeAdapter
   // private lateinit var mEmpList: ArrayList<GetAllEmp.Result>
    private val createNewTaskViewModel:CreateNewTaskViewModel by activityViewModels()

    @SuppressLint("SetTextI18n")
    var activityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(), ActivityResultCallback<ActivityResult> { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val d = result.data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                binding.etDescription.setText(binding.etDescription.text.toString() + "" + d!![0])
            }
        })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_create_new_task, container, false)
        binding = FragmentCreateNewTaskBinding.bind(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        createNewTaskViewModel.getAllEmp()
        getAllEmpObserver()

        binding.btSubmit.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val description = binding.etDescription.toString()
            val assign = binding.spAssign
            if (valid()) {
                val all = CreateTask(selectedEmp, description, title)
                createNewTaskViewModel.createNewTask(all)
                createNewTaskObserver()
            }
        }

        binding.imMic.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now!")
            activityResultLauncher.launch(intent)
        }








        binding.spAssign.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {


            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                try {
                    val selectedEmpId = parent?.getItemAtPosition(position) as GetAllEmp.Result
                    selectedEmp = selectedEmpId.id
                } catch (e: Exception) {

                }



            }

        }

    }







    private fun getAllEmpObserver(){
        createNewTaskViewModel.getAllEmployess.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when(it){
                is NetworkResult.Success->{
                    getAllEmpAdapter  = GetAllEmployeeAdapter(requireContext(),it.data?.results as List<GetAllEmp.Result>)
                    binding.spAssign.adapter = getAllEmpAdapter }

                is NetworkResult.Error->{
                    Toast.makeText(activity,it.message, Toast.LENGTH_SHORT).show()

                }
                is NetworkResult.Loading->{
                    binding.progressBar.isVisible=true

                }
            }
        })
    }


    private fun createNewTaskObserver(){
        createNewTaskViewModel.createNewTaskResponseLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when(it){
                is NetworkResult.Success->{
                    Toast.makeText(activity,"Successfully created task",Toast.LENGTH_SHORT).show()

                }
                is NetworkResult.Error->{
                    Toast.makeText(activity,it.message,Toast.LENGTH_SHORT).show()

                }
                is NetworkResult.Loading->{
                    binding.btSubmit.isVisible = false
                    binding.progressBar.isVisible = true

                }
            }
        })
    }

    private fun valid():Boolean{
        if (binding.etTitle.text?.isEmpty()==true){
            binding.etTitle.error = "Title is required"
            return false
        }else if (binding.etDescription.text?.isEmpty() == true){
            binding.etDescription.error = "Description is required"
            return false
        }
        return true
    }





    //    private fun setAssignToEmp() {
//        val typeList =
//            arrayOf("Click here to select", "Naveen", "Vivek", "Subhash", "bharath", "Akhil")
//        val adapter: ArrayAdapter<String> = object :
//            ArrayAdapter<String>(
//                requireContext(),
//                androidx.appcompat.R.layout.select_dialog_item_material, typeList
//            ) {
//            override fun getDropDownView(
//                position: Int,
//                convertView: View?,
//                parent: ViewGroup
//            ): View {
//                val view: TextView =
//                    super.getDropDownView(position, convertView, parent) as TextView
//
//                if (position == binding.spAssign.selectedItemPosition && position != 0) {
//                    view.setTextColor(Color.parseColor("#000000"))
//                }
//                if (position == 0) {
//                    view.setTextColor(Color.parseColor("#999999"))
//                }
//                return view
//            }
//
//            override fun isEnabled(position: Int): Boolean {
//                return position != 0
//            }
//        }
//        binding.spAssign.adapter = adapter
//        binding.spAssign.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                parent: AdapterView<*>?,
//                view: View?,
//                position: Int,
//                id: Long
//            ) {
//                if (position > 0) {
//                    selectedFirstAidType = if (position == 1) 1 else 0
//                }
//                selectType = parent?.getItemAtPosition(position).toString().toLowerCase()
//
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//
//            }
//        }
//    }


}