package com.techarion.techarion


import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.TypedValue
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.button.MaterialButton
import com.google.android.material.navigation.NavigationView
import com.karumi.dexter.Dexter
import com.karumi.dexter.DexterBuilder
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.shashank.sony.fancytoastlib.FancyToast
import com.techarion.techarion.cheack_internet_connection.NetworkViewModel
import com.techarion.techarion.databinding.ActivityMainBinding
import com.techarion.techarion.notification.RemindersManager
import com.techarion.techarion.profile.viewModel.ProfileViewModel
import com.techarion.techarion.user_action.UserActionActivity
import com.techarion.techarion.utils.Cons
import com.techarion.techarion.utils.NetworkResult
import com.techarion.techarion.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var dialog: Dialog
    lateinit var internetDialog: Dialog
    private var is_admin: Boolean = false
    private var is_emp: Boolean = false
    private var is_teamLead: Boolean = false
    lateinit var dexter: DexterBuilder
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @Inject
    lateinit var tokenManager: TokenManager
    private val networkViewModel: NetworkViewModel by viewModels()
    private val profileViewModelProvider: ProfileViewModel by viewModels()

    companion object{
        private const val PERMISSION_REQUEST_ACCESS_LOCATION=100
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            dexter.check()
        }


    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        internetDialog = Dialog(this)
        internetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        is_admin = tokenManager.getAdmin(Cons.is_admin)
        is_emp = tokenManager.getEmp(Cons.is_Emp)
        is_teamLead = tokenManager.getTeamLead()

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        setContentView(binding.root)

















        window.navigationBarColor = ContextCompat.getColor(this@MainActivity, R.color.button_txt_color)

        RemindersManager.startReminder(this)

        binding.navView.menu.findItem(R.id.all_task).isVisible = !(is_admin == false && is_teamLead == false && is_emp == true)
        binding.navView.menu.findItem(R.id.all_task).isVisible = !(is_admin == false && is_teamLead == true)





        profileViewModelProvider.getUserProfileDetails()
        bindObserverToGetProfileUserDetails()

        hideHamburgerIcon()





        

        lifecycleScope.launchWhenCreated {
            networkViewModel.isConnected.collectLatest {
                if (it) {
                    internetDialog.dismiss()
                } else {
                    internetDialog.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
                    internetDialog.setContentView(R.layout.layout_for_no_internet_connection)
                    internetDialog.setCancelable(false)
                  val ok  =  internetDialog.findViewById<TextView>(R.id.btRetry)
                    ok.setOnClickListener {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            startActivity(Intent(Settings.ACTION_DATA_USAGE_SETTINGS))
                        }
                    }
                    internetDialog.show()
                }
            }
        }

        binding.toolBar.setNavigationOnClickListener {
            binding.mydrawerlayout.open()

        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.navView.setNavigationItemSelectedListener { menuItem ->


            when (menuItem.itemId) {

                R.id.all_task -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.allTaskFragment)
                    binding.mydrawerlayout.close()
                }
                R.id.new_task -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.createNewTaskFragment)
                    binding.mydrawerlayout.close()
                }
                R.id.nav_assigned_task -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.taskAssignedToEmployee)
                    binding.mydrawerlayout.close()

                }
                R.id.nav_created_task -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.createdTaskByEmployeeFragment)
                    binding.mydrawerlayout.close()
                }
                R.id.profile -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.profileFragment)
                    binding.mydrawerlayout.close()
                }
                R.id.nav_current_location_frag->{
                    findNavController(R.id.nav_host_fragment).navigate(R.id.currentLocationFragment)
                    binding.mydrawerlayout.close()
                }
                R.id.logot -> {
                    logoutVerificationDialog()
                }


            }
            menuItem.isChecked = true
            binding.mydrawerlayout.close()
            true
        }


    }

    private fun logoutVerificationDialog() {
        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_alert)
        dialog.setCancelable(true)
        dialog.show()

        val stay_diag: MaterialButton? = dialog.findViewById(R.id.stay_diag)
        stay_diag?.setOnClickListener {
            dialog.dismiss()
        }
        val logOut: MaterialButton? = dialog.findViewById(R.id.logout_diag)
        logOut?.setOnClickListener {
            tokenManager.clearAll()
            dialog.dismiss()
            gotoActivityWithNoBackUp(UserActionActivity::class.java)

        }
    }





    private fun createNotificationsChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                getString(R.string.reminders_notification_channel_id),
                getString(R.string.reminders_notification_channel_name),
                NotificationManager.IMPORTANCE_HIGH)
            ContextCompat.getSystemService(this, NotificationManager::class.java)
                ?.createNotificationChannel(channel)
        }
    }

    private fun hideHamburgerIcon() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            binding.toolBar.isVisible = destination.id != R.id.profileFragment
            binding.toolBar.isVisible = destination.id != R.id.attandance
           if(destination.id ==R.id.currentLocationFragment || destination.id == R.id.taskDetailsFragment || destination.id == R.id.allTaskFragment ){
               binding.toolBar.visibility = View.GONE
           }
            binding.toolBar.visibility = View.VISIBLE

            if (destination.id == R.id.profileFragment || destination.id == R.id.currentLocationFragment || destination.id == R.id.allTaskFragment) {
                binding.navHostFragment.margin(top = 0F)
            } else {
                binding.navHostFragment.margin(top = 55F)
            }
        }
    }

    fun View.margin(left: Float? = null, top: Float? = null, right: Float? = null, bottom: Float? = null) {
        layoutParams<ViewGroup.MarginLayoutParams> {
            left?.run { leftMargin = dpToPx(this) }
            top?.run { topMargin = dpToPx(this) }
            right?.run { rightMargin = dpToPx(this) }
            bottom?.run { bottomMargin = dpToPx(this) }
        }
    }

    inline fun <reified T : ViewGroup.LayoutParams> View.layoutParams(block: T.() -> Unit) {
        if (layoutParams is T) block(layoutParams as T)
    }

    fun View.dpToPx(dp: Float): Int = context.dpToPx(dp)
    fun Context.dpToPx(dp: Float): Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()


    fun getPermission() {
        dexter = Dexter.withContext(this)
            .withPermissions(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    report.let {

                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(
                                this@MainActivity,
                                "Permissions Granted",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            AlertDialog.Builder(this@MainActivity, R.style.Theme_TechArion).apply {
                                setMessage("please allow the required permissions")
                                    .setCancelable(false)
                                    .setPositiveButton("Settings") { _, _ ->
                                        val reqIntent =
                                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                                .apply {
                                                    val uri =
                                                        Uri.fromParts("package", packageName, null)
                                                    data = uri
                                                }
                                        resultLauncher.launch(reqIntent)
                                    }
                                // setNegativeButton(R.string.cancel) { dialog, _ -> dialog.cancel() }
                                val alert = this.create()
                                alert.show()
                            }
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            }).withErrorListener {
                Toast.makeText(this, it.name, Toast.LENGTH_SHORT).show()
            }
        dexter.check()
    }


    //password validation like Vivek@123
//    internal fun isValidPassword(password: String): Boolean {
//        if (password.length < 8) return false
//        if (password.filter { it.isDigit() }.firstOrNull() == null) return false
//        if (password.filter { it.isLetter() }.filter { it.isUpperCase() }.firstOrNull() == null) return false
//        if (password.filter { it.isLetter() }.filter { it.isLowerCase() }.firstOrNull() == null) return false
//        if (password.filter { !it.isLetterOrDigit() }.firstOrNull() == null) return false
//
//        return true
//    }


    private fun bindObserverToGetProfileUserDetails() {
        profileViewModelProvider.getUserProfileDetails.observe(this, androidx.lifecycle.Observer {

            when (it) {
                is NetworkResult.Success -> {

                    val navigationView: NavigationView = findViewById(R.id.navView)
                    val header: View = navigationView.getHeaderView(0)
                    val tv: TextView = header.findViewById(R.id.txtName)
                    val image: ImageView = header.findViewById(R.id.profile_image1)
                    tv.text = it.data?.fullName
                    Glide.with(this).load(Cons.image_url + it.data?.profile?.image).centerCrop()
                        .into(image)
                }
                is NetworkResult.Error -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()

                }
                is NetworkResult.Loading -> {


                }
            }

        })
    }


    fun <T : AppCompatActivity> Activity.gotoActivityWithNoBackUp(targetActivityClass: Class<T>) {
        val intent = Intent(this@MainActivity, targetActivityClass)
            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
             finish()
           startActivity(intent) }






//        override fun handleOnBackPressed() {
//            // Checking if drawer is open
//            if (binding.mydrawerlayout.isDrawerOpen(binding.navView)) {
//                // Close the drawer on left
//                binding.mydrawerlayout.closeDrawer(Gravity.LEFT)
//            } else {
//                // Drawer not open close the application
//                finish()
//            }
//
//        }

    private fun getCurrentLocation(){
        if (checkPermission()){
            if (isLocationEnabled()){
                // final latti and long code here
                fusedLocationProviderClient.lastLocation.addOnCompleteListener {
                    val location: Location = it.result
                    if (location==null){

                    } else {
//                        FancyToast.makeText(this,"Get Success !",FancyToast.LENGTH_LONG, FancyToast.SUCCESS,false).show()
//                        FancyToast.makeText(this,""+location.latitude,FancyToast.LENGTH_LONG, FancyToast.SUCCESS,false).show()
//                        FancyToast.makeText(this,""+location.longitude,FancyToast.LENGTH_LONG, FancyToast.SUCCESS,false).show()
                    }

                }

            } else{
                FancyToast.makeText(this,"Turn on location !",FancyToast.LENGTH_LONG, FancyToast.WARNING,false).show()
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))

            }
        } else{
            //request permission here
            requestPermission()
        }
    }

    private fun checkPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false

    }
    private fun requestPermission(){
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_REQUEST_ACCESS_LOCATION)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode== PERMISSION_REQUEST_ACCESS_LOCATION){
            if (grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                FancyToast.makeText(this,"Granted !",FancyToast.LENGTH_LONG, FancyToast.SUCCESS,false).show()
                getCurrentLocation()
            } else{
                FancyToast.makeText(this,"Denied !",FancyToast.LENGTH_LONG, FancyToast.WARNING,false).show()
            }
        }
    }

    private fun isLocationEnabled():Boolean{
        val locationManger:LocationManager =  getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManger.isProviderEnabled(LocationManager.GPS_PROVIDER)||locationManger.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    fun Activity.enableFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.let {
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                it.hide(WindowInsets.Type.systemBars())
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_IMMERSIVE
                            // Set the content to appear under the system bars so that the
                            // content doesn't resize when the system bars hide and show.
                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            // Hide the nav bar and status bar
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                    )
        }
    }

    fun Activity.disableFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.show(WindowInsets.Type.systemBars())
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        }
    }

    override fun onBackPressed() {
        val drawer = binding.mydrawerlayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }




}





