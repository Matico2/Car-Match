package com.example.carmatch.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.carmatch1.R

class AdVehiclesFragment : Fragment() {
   
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       
        return inflater.inflate(R.layout.fragment_ad_vehicles, container, false)
    }
    
    
}