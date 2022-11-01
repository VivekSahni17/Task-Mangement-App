package com.techarion.techarion.login_ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations.map
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.techarion.techarion.MainActivity
import com.techarion.techarion.R
import com.techarion.techarion.databinding.FragmentLoginBinding
import com.techarion.techarion.login_ui.login_view_model.LoginViewModel
import com.techarion.techarion.login_ui.model.User_Credentials
import com.techarion.techarion.login_ui.model.userDetails
import com.techarion.techarion.utils.Cons
import com.techarion.techarion.utils.Helper
import com.techarion.techarion.utils.NetworkResult
import com.techarion.techarion.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import java.util.Collections.replaceAll
import javax.inject.Inject
import kotlin.math.log


@AndroidEntryPoint
class LoginFragment : Fragment() {
    lateinit var binding:FragmentLoginBinding
    private val loginViewModel:LoginViewModel by activityViewModels()
    @Inject
    lateinit var tokenManager: TokenManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_login, container, false)
        binding = FragmentLoginBinding.bind(view)
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.btLogin.setOnClickListener {
//            Helper.hideKeyboard(it)
//            val userDetails = userDetails(binding.etEmailAddress.text.toString().trim(),binding.etPassword.text.toString().trim())
//            loginViewModel.loginCredentials(userDetails)
//            bindObserverForLoginCredentials()
            val validationResult = validateUserInput()
            if (validationResult.first) {
                val userRequest = getUserRequest()
                loginViewModel.loginCredentials(userRequest)
                bindObserverForLoginCredentials()
            } else {
                showValidationErrors(validationResult.second)
            }
        }
        binding.tvForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotFragment)
        }
        binding.tvSignup.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }



    }
    private fun validateUserInput(): Pair<Boolean, String> {
        val emailAddress = binding.etEmailAddress.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        return loginViewModel.validateCredentials(emailAddress,  password, true)
    }
    private fun showValidationErrors(error: String) {
        binding.txtError.text = String.format(resources.getString(R.string.txt_error_message, error))
    }

    private fun getUserRequest(): userDetails {
        return binding.run {
            userDetails(
                etEmailAddress.text.toString().trim(),
                etPassword.text.toString().trim(),
                isAdmin = true,
                isEmployee = true

            )
        }
    }

    private fun bindObserverForLoginCredentials(){
        loginViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            binding.btLogin.isVisible = true
            when(it){
                is NetworkResult.Success ->{
                    it.data?.access?.let { it1 -> tokenManager.saveToken(Cons.USER_TOKEN, it1) }
                    it.data?.userDetails?.isAdmin?.let { it1 -> tokenManager.saveUserRole(Cons.is_admin, it1) }
                    it.data?.userDetails?.isEmployee?.let { it1 -> tokenManager.saveUserRole(Cons.is_Emp, it1) }
                    Log.d("vivek",tokenManager.getToken().toString())
                    Log.d("token", it.data?.access.toString())
                   Toast.makeText(activity,it.data?.message,Toast.LENGTH_SHORT).show()
//                    findNavController().navigate(R.id.action_loginFragment_to_mainActivity)
                    gotoActivityWithNoBackUp(MainActivity::class.java)
                }
                is NetworkResult.Error ->{
                    val msg = it.message?.replace("[", "")?.replace("]", "")
                    if (msg != null) {
                        binding.txtError.text = msg.removeSurrounding("\"")
                    }
                    Toast.makeText(activity,"${it.message}",Toast.LENGTH_SHORT).show()

                }
                is NetworkResult.Loading ->{
                    binding.progressBar.isVisible = true
                    binding.btLogin.isVisible = false

                }
            }
        })

    }

    fun <T : AppCompatActivity> Fragment.gotoActivityWithNoBackUp(targetActivityClass: Class<T>) {
        val intent = Intent(requireActivity(), targetActivityClass)
            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        requireActivity().finish()
        startActivity(intent)
    }




}