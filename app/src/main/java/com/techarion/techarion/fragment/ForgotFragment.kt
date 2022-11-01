package com.techarion.techarion.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.techarion.techarion.R
import com.techarion.techarion.databinding.FragmentForgotBinding


class ForgotFragment : Fragment() {
  lateinit var binding:FragmentForgotBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view  = inflater.inflate(R.layout.fragment_forgot, container, false)
        binding = FragmentForgotBinding.bind(view)
        return  view

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


    }


}