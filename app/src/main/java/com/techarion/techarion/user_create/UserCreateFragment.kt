package com.techarion.techarion.user_create

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.textfield.TextInputLayout
import com.techarion.techarion.R
import com.techarion.techarion.databinding.FragmentUserCreateBinding
import com.techarion.techarion.user_create.model.UserCreate
import com.techarion.techarion.user_create.viewmodel.CreateUserViewModel
import com.techarion.techarion.utils.Cons
import com.techarion.techarion.utils.NetworkResult
import com.techarion.techarion.utils.TokenManager
import java.util.regex.Pattern


class UserCreateFragment : Fragment() {
    lateinit var binding:FragmentUserCreateBinding
    private val userCreateUserViewModel: CreateUserViewModel by activityViewModels()
    private var userTeamLead:Boolean=false
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.fragment_user_create, container, false)
        binding = FragmentUserCreateBinding.bind(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tokenManager = TokenManager()
        userTeamLead = tokenManager.getTeamLead()
        Log.d("userTeamLead",tokenManager.getTeamLead().toString())
        Log.d("userAdmin",tokenManager.getAdmin(Cons.is_admin).toString())

        binding.btSubmit.setOnClickListener {
            val userId = binding.etCreateUserId.text.toString().trim()
            val email = binding.etEmailAddress.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val fullName = binding.etFullName.text.toString().trim()
            val allField = UserCreate(email,userId,password,fullName)
            if (valid()) {
              if (userTeamLead==true && tokenManager.getAdmin(Cons.is_admin)==false){
                  userCreateUserViewModel.createdTeamEmp(allField)
                  bindObserverForCreateUser()
              } else
                  userCreateUserViewModel.createEmpId(allField)
                  bindObserverForCreateUser()
            }

        }

    }

    private fun bindObserverForCreateUser(){
        userCreateUserViewModel.createEmpResponseLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            binding.btSubmit.isVisible=true
            when(it){
                is NetworkResult.Error->{
                  binding.txtError.text = it.message
                }
                is NetworkResult.Success->{
                    Toast.makeText(activity,"Successfully created employee",Toast.LENGTH_SHORT).show()
                 findNavController().navigate(R.id.action_userCreateFragment_to_dashboardFragment)
                }
                is NetworkResult.Loading->{
                    binding.progressBar.isVisible = true
                    binding.btSubmit.isVisible=false
                }
            }
        })

    }

    private fun valid():Boolean{
        val regexString = "[TA]+"
        when {

            binding.etCreateUserId.text?.isEmpty() == true -> {
                binding.etCreateUserId.error="Userid should be required"
                return false
            }
            binding.etFullName.text?.isEmpty()== true ->{
                binding.etFullName.error="Full name is required"
                return false
            }
            binding.etEmailAddress.text?.isEmpty()==true -> {
                binding.etEmailAddress.error = "Email is required"
                return false
            }
//            Patterns.EMAIL_ADDRESS.matcher(binding.etEmailAddress.text.toString()).matches() -> {
//                binding.etEmailAddress.error = "Invalid email"
//                return false
//
//            }
            binding.etPassword.text?.isEmpty()==true -> {
                binding.etPassword.error = "Password field should not be empty"
                binding.tvPassword.endIconMode = TextInputLayout.END_ICON_NONE
                return false
            }
            binding.etPassword.text?.isNotEmpty() == true ->{
                binding.tvPassword.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE

            }
            binding.etPassword.length()<=4 -> {
                binding.etPassword.error= "Password length should not be less than 5 digits"
                binding.tvPassword.endIconMode = TextInputLayout.END_ICON_NONE
                return false
            }
            binding.etConfirmPassword.text?.isEmpty()==true -> {
                binding.etConfirmPassword.error = "Confirm password field should not be empty"
                binding.tvConfirmPassword.endIconMode=TextInputLayout.END_ICON_NONE
                return false
            }
            binding.etConfirmPassword.text!=binding.etPassword.text -> {
                binding.tvConfirmPassword.error = "Confirm Password does not match"
                return false
            }


        }
        return true

    }


}