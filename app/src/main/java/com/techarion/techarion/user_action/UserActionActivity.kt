package com.techarion.techarion.user_action

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.techarion.techarion.MainActivity
import com.techarion.techarion.R
import com.techarion.techarion.databinding.ActivityUserActionBinding
import com.techarion.techarion.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class UserActionActivity : AppCompatActivity() {
    lateinit var binding:ActivityUserActionBinding
    @Inject
    lateinit var tokenManager: TokenManager
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityUserActionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.tech_arion)
        //setContentView(R.layout.activity_user_action)
        val areTokenAvailable = tokenManager.getToken() !=null
        setupLoginFlow(areTokenAvailable)




    }
    private fun setupLoginFlow(condition : Boolean){ //if True navigates to home activity else login fragment
        val navHostFragment = (supportFragmentManager.findFragmentById(R.id.login_fragment_container) as NavHostFragment)
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.user_action_navigation)
        graph.setStartDestination(if (condition) R.id.mainActivity else R.id.loginFragment)
        navHostFragment.navController.graph = graph
        if (condition) finish()

    }






}