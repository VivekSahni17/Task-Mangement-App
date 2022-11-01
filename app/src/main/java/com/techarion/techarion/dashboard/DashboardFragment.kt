package com.techarion.techarion.dashboard

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.hardware.display.DisplayManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.auth0.jwt.JWT
import com.auth0.jwt.interfaces.DecodedJWT
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.labters.styler.core.StyleR
import com.techarion.techarion.HiltApplication.HiltApplication
import com.techarion.techarion.MainActivity
import com.techarion.techarion.R
import com.techarion.techarion.databinding.FragmentDashboardBinding
import com.techarion.techarion.employee.viewmodel.EmployeesViewModel
import com.techarion.techarion.profile.viewModel.ProfileViewModel
import com.techarion.techarion.user_action.UserActionActivity
import com.techarion.techarion.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class DashboardFragment : Fragment() {
    private var is_emp:Boolean= false
    private var is_admin:Boolean=false
    private var is_teamLead:Boolean=false

    @Inject
    lateinit var tokenManager:TokenManager
    private val employeeViewModel: EmployeesViewModel by activityViewModels()
    lateinit var binding:FragmentDashboardBinding

    private val profileViewModelProvider:ProfileViewModel by activityViewModels()
    private lateinit var mContext: MainActivity





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_dashboard, container, false)
        binding = FragmentDashboardBinding.bind(view)
        return view
    }
    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // userRole = tokenManager.getAdmin(Cons.is_admin)
        is_emp = tokenManager.getAdmin(Cons.is_Emp)
        is_admin = tokenManager.getEmp(Cons.is_admin)
        is_teamLead = tokenManager.getTeamLead()
        Log.d("vivek", tokenManager.getToken().toString())

        if (is_admin == false && is_emp == true && is_teamLead == false) {
            binding.tvCreateNewUser.text = "Create Task"
            binding.totalUsers.isVisible = false
            binding.tvInProgressTask.text = "allTask"
            binding.tvTotalInProgressTask.isVisible = false
            binding.noOfInProgressTask.isVisible = false
            binding.tvTotalNewTask.isVisible = false


        }

        val jwt: DecodedJWT = JWT.decode(tokenManager.getToken())
        Log.d("vivek", jwt.expiresAt.toString())
        Log.d("vivek", jwt.expiresAt.before(Date()).toString())


        val token = jwt.expiresAt.before(Date())
        when (token) {
            true -> {
                tokenManager.clearAll()
                gotoActivityWithNoBackUp(UserActionActivity::class.java)
            }
            else -> {
            }
        }








        employeeViewModel.getDashboardStats()
        getDashboardStats()

        binding.swipeContainer.setOnRefreshListener {
            employeeViewModel.getDashboardStats()
            getDashboardStats()

        }

        profileViewModelProvider.getUserProfileDetails()
        bindObserverToGetProfileUserDetails()





        Log.d("date", Instant.now().toString())



        binding.cardView2.setOnClickListener {
            if( binding.tvCreateNewUser.text == "allTask") {
                findNavController().navigate(R.id.action_dashboardFragment_to_taskAssignedToEmployee)
            } else {
                findNavController().navigate(R.id.action_dashboardFragment_to_allTaskFragment)
            }
        }
        binding.cardView3.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_attandance)
        }
        binding.cardView4.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_allTaskFragment)
        }
        binding.cardView1.setOnClickListener {
            if (binding.tvCreateNewUser.text == "Create Task")
            findNavController().navigate(R.id.action_dashboardFragment_to_createNewTaskFragment)
            else
                findNavController().navigate(R.id.action_dashboardFragment_to_userCreateFragment)
        }
        }



    private fun getDashboardStats(){
        employeeViewModel.getDashboardStats.observe(viewLifecycleOwner, Observer {
            binding.light.on()
            //binding.progressBar?.isVisible = false
            binding.progressCard.isVisible = false
            binding.swipeContainer.isRefreshing = false
            when(it){
                is NetworkResult.Success->{
                  binding.totalUsers.text = it.data?.totalEmployee.toString()
                    binding.totalPresent.text = it.data?.totalPresent.toString()
                    //binding.totalUsers.text = it.data?.totalEmployee.toString()

                }

                is NetworkResult.Error->{
                    it.message?.let { it1 -> Toaster.shortToast(it1) }

                }
                is NetworkResult.Loading->{
                    binding.light.off()
                    //binding.progressBar?.isVisible = true
                    binding.progressCard.isVisible =true


                }
            }
        })
    }

    private fun bindObserverToGetProfileUserDetails(){
        profileViewModelProvider.getUserProfileDetails.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            //binding.progressBar?.isVisible = false
            binding.progressCard.isVisible =false
            binding.light.isVisible = false
            binding.light.on()
            when(it){
                is NetworkResult.Success->{
                    Glide.with(requireContext()).load(Cons.image_url+it.data?.profile?.image).centerCrop().into(binding.profileImage)
                }
                is NetworkResult.Error->{
                    Toaster.shortToast(it.message.toString())

                }
                is NetworkResult.Loading->{
                    binding.light.off()
                    binding.light.isVisible = true
                    //binding.progressBar?.isVisible = true
                    binding.progressCard.isVisible =true


                }
            }

        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as MainActivity
        var doubleBackToExitPressedOnce = false
        val callback:OnBackPressedCallback
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (doubleBackToExitPressedOnce) {
                    activity?.finish()
                }
                doubleBackToExitPressedOnce = true
                Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
                //Toast.makeText(activity,"Press again to exit",Toast.LENGTH_SHORT).show()
               Snackbar.make(requireView(),"Press again to exit",Snackbar.LENGTH_SHORT).show()
                (activity as MainActivity).findViewById<DrawerLayout>(R.id.mydrawerlayout).closeDrawers()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
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