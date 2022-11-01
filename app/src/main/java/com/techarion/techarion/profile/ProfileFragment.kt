package com.techarion.techarion.profile

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.techarion.techarion.R
import com.techarion.techarion.databinding.FragmentProfileBinding
import com.techarion.techarion.profile.viewModel.ProfileViewModel
import com.techarion.techarion.utils.Cons
import com.techarion.techarion.utils.NetworkResult
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream


class ProfileFragment : Fragment() {
    lateinit var binding:FragmentProfileBinding
    private val profileViewModelProvider:ProfileViewModel by activityViewModels()
     var imageUri: Uri?=null
    lateinit var dialog: Dialog

    private val contract = registerForActivityResult(ActivityResultContracts.TakePicture()){

        binding.profileImage.setImageURI(null)
        binding.profileImage.setImageURI(imageUri)
    }

    @SuppressLint("Range")
    private val gContract = registerForActivityResult(ActivityResultContracts.GetContent()){
        if (it != null) {
            imageUri = it
        }
        val uri = imageUri
        val cursor =  uri?.let { requireActivity().contentResolver.query(it, null, null, null, null) }
        if (cursor != null && cursor.moveToNext()){
            val name = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            Log.d("nameee",name)
        }
        binding.profileImage.setImageURI(null)
        binding.profileImage.setImageURI(it)
        Log.d("uri",it.toString())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_profile, container, false)
        binding = FragmentProfileBinding.bind(view)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        imageUri = createImageUri()!!
        binding.imCamera.setOnClickListener {
//            contract.launch(imageUri)
            showDialogForGalleryAndCamera()

        }





//        binding.imCalender.setOnClickListener {
//            datePicker()
//        }

        profileViewModelProvider.getUserProfileDetails()
        bindObserverToGetProfileUserDetails()



        binding.btUpdate.setOnClickListener {
         //(activity as MainActivity).getPermission()
           uploadImage()
        }

        binding.imBackArrow.setOnClickListener {
            findNavController().popBackStack()
        }

    }

//    @SuppressLint("SetTextI18n")
//    private fun datePicker(){
//        val c = Calendar.getInstance()
//        val year = c.get(Calendar.YEAR)
//        val month = c.get(Calendar.MONTH)
//        val day = c.get(Calendar.DAY_OF_MONTH)
//        val datePickerDialog = DatePickerDialog(
//            // on below line we are passing context.
//            requireActivity(),
//            { view, year, monthOfYear, dayOfMonth ->
//                // on below line we are setting
//                // date to our text view.
//                binding.tvdateOfJoin.text = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year) }, year, month, day
//        )
//        datePickerDialog.show()
//        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FF0C3823"))
//        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#FF0C3823"))
//
//
//    }

    private fun bindObserverToGetProfileUserDetails(){
        profileViewModelProvider.getUserProfileDetails.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            binding.progressBar.isVisible = false


            when(it){
                is NetworkResult.Success->{
                    binding.tvName.text = it.data?.fullName
                    //binding.tvJobProfile.text = it.data?.employeeId
                    binding.etFirstName.setText(it.data?.fullName)
                    binding.etEmail.setText(it.data?.email)
                    binding.etPhone.setText(it.data?.profile?.phone.toString())
                    binding.etDesignation.setText(it.data?.profile?.occupation)
                    binding.etGender.setText(it.data?.profile?.gender)
                    Glide.with(requireContext()).load(Cons.image_url+it.data?.profile?.image).centerCrop().into(binding.profileImage)
                    val dateOfJoined = it.data?.profile?.dateOfJoin
                    //binding.tvdateOfJoin.text = dateOfJoined.toString()


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



    private fun  bindObserverToUpdateUserProfile(){
        profileViewModelProvider.updateUserProfileDetails.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            binding.progressBar.isVisible = false
            binding.btUpdate.isVisible = true


            when(it){
                is NetworkResult.Success->{
                    profileViewModelProvider.getUserProfileDetails()
                    bindObserverToGetProfileUserDetails()
                }
                is NetworkResult.Error->{
                    Toast.makeText(activity,it.message,Toast.LENGTH_SHORT).show()

                }
                is NetworkResult.Loading->{
                    binding.progressBar.isVisible = true
                    binding.btUpdate.isVisible = false

                }
            }
        })
    }


    // Function to check and request permission.
    private fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission), requestCode)
        } else {
            Toast.makeText(requireContext(), "Permission already granted", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createImageUri(): Uri? {
        val cImage = File(context?.filesDir,"camera_photos.png")
        return FileProvider.getUriForFile(requireContext(),"com.techarion.techarion.fileProvider",cImage)

    }


    @SuppressLint("Recycle", "Range")
    private fun uploadImage(){
        val fileDir = context?.filesDir
        val file = File(fileDir,"image.png")

        val inputStream = activity?.contentResolver?.openInputStream(imageUri!!)
        val outPutString = FileOutputStream(file)
        inputStream!!.copyTo(outPutString)

        Log.d("filename", inputStream.toString())

        // get name of file which select from gallery


//        val path = ":/storage/sdcard0/DCIM/Camera/1414240995236.jpg"
//        val filename = path.substring(path.lastIndexOf("/") + 1)
//        Log.d("file",filename)




        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val part = MultipartBody.Part.createFormData("image", file.name,requestBody)


        val phone = RequestBody.create("text/plain".toMediaTypeOrNull(), binding.etPhone.text.toString())
        val designation = RequestBody.create("text/plain".toMediaTypeOrNull(),binding.etDesignation.text.toString())
        val gender = RequestBody.create("text/plain".toMediaTypeOrNull(),binding.etGender.text.toString())
        val dateofJoin = RequestBody.create("text/plain".toMediaTypeOrNull(),binding.etGender.text.toString())


        profileViewModelProvider.updateUerProfileDetails(part,phone,designation,gender)
        bindObserverToUpdateUserProfile()




    }


    private fun showDialogForGalleryAndCamera() {
        dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.layout_to_choose_image_from_gallery_or_camera)
        dialog.setCancelable(true)
        dialog.show()

        val choose_gallery: LinearLayout? = dialog.findViewById(R.id.choose_gallery)
        choose_gallery?.setOnClickListener {
            dialog.dismiss()
            gContract.launch("image/*")


        }
        val choose_camera: LinearLayout? = dialog.findViewById(R.id.choose_camera)
        choose_camera?.setOnClickListener {
            dialog.dismiss()
            contract.launch(imageUri)
            //contractForCamera.launch(imageUri)

        }
    }



}


