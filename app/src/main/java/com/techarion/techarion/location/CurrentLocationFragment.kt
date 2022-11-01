package com.techarion.techarion.location

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.icu.lang.UCharacter.SentenceBreak.CLOSE
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.WebSettings
import android.widget.Toast
import android.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.marginTop
import androidx.fragment.app.FragmentContainerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.appbar.MaterialToolbar
import com.labters.styler.extension.px
import com.shashank.sony.fancytoastlib.FancyToast
import com.techarion.techarion.MainActivity
import com.techarion.techarion.R
import com.techarion.techarion.databinding.FragmentCurrentLocationBinding
import com.techarion.techarion.databinding.FragmentMapsBinding
import com.techarion.techarion.utils.TrackingUtils


class CurrentLocationFragment : Fragment(), OnMapReadyCallback {
    lateinit var binding:FragmentCurrentLocationBinding
    private lateinit var mMap: GoogleMap
    lateinit var locationManager: LocationManager
    private lateinit var lastLocation: Location
    private var mPolylineList = mutableListOf<LatLng>()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var currentMarker: Marker?=null
    var line: Polyline?=null

    companion object{
        private const val LOCATION_REQUEST_CODE = 1
        private const val PERMISSION_REQUEST_ACCESS_LOCATION=100

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_current_location, container, false)
        binding = FragmentCurrentLocationBinding.bind(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())



        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)




    }

    override fun onMapReady(p0: GoogleMap) {

        mMap = p0
        mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
        mMap.uiSettings.isZoomControlsEnabled=true


        getCurrentLocation()
    }





    private fun placeMarkerOnMap(currentLatLong: LatLng) {
        mMap.setOnMapClickListener {
            FancyToast.makeText(requireContext(), "$currentLatLong", FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show()
            getCurrentLocation()
            mPolylineList += currentLatLong
            drawPolyline(mPolylineList = mPolylineList)
            if (currentMarker!=null){
                currentMarker!!.remove()
                currentMarker = null

            }
            if (currentMarker==null) {
                currentMarker = mMap.addMarker( MarkerOptions().position(currentLatLong)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))
            }


        }

    }

    private fun drawPolyline(mPolylineList: MutableList<LatLng>){
        val polyline = PolylineOptions().width(5f).color(Color.RED).geodesic(true)
        for (i in 0 until mPolylineList.size-1){
            val point: LatLng =mPolylineList[i]
            polyline.add(point)
        }
        line = mMap.addPolyline(polyline)
    }


    private fun getCurrentLocation(){
        if (TrackingUtils.checkPermission(requireContext())){
            if (TrackingUtils.isLocationEnabled(requireContext())){
                // final latti and long code here
                mMap.isMyLocationEnabled =true
                fusedLocationClient.lastLocation.addOnCompleteListener {
                    val location: Location = it.result
                    if (location != null){
                        lastLocation = location
                        val currentLatLong = LatLng(location.latitude,location.longitude)
                        placeMarkerOnMap(currentLatLong)
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong,20f))
                    }
                }

            } else{
                FancyToast.makeText(requireContext(),"Turn on location !",FancyToast.LENGTH_LONG, FancyToast.WARNING,false).show()
                Log.d("location","Turn on location")
                //startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                //getCurrentLocation()

            }
        } else{
            //request permission here
            requestPermission()
        }
    }


    private fun requestPermission(){
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION),
            CurrentLocationFragment.LOCATION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==  CurrentLocationFragment.LOCATION_REQUEST_CODE){
            if (grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                FancyToast.makeText(requireContext(),"Granted !",FancyToast.LENGTH_LONG, FancyToast.SUCCESS,false).show()
                getCurrentLocation()
            } else{
                FancyToast.makeText(requireContext(),"Denied !",FancyToast.LENGTH_LONG, FancyToast.WARNING,false).show()
            }
        }
    }



}