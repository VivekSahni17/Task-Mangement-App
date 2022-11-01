package com.techarion.techarion.attadence

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.techarion.techarion.R
import com.techarion.techarion.databinding.FragmentAllAttendanceBinding


class AllAttendance : Fragment() {
   lateinit var binding:FragmentAllAttendanceBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_all_attendance, container, false)
        binding = FragmentAllAttendanceBinding.bind(view)
        return view
    }


}