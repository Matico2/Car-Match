package com.example.carmatch.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.carmatch1.databinding.FragmentFavVehiclesBinding

class FavVehiclesFragment : Fragment() {
    private lateinit var binding: FragmentFavVehiclesBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavVehiclesBinding.inflate(inflater, container, false)
        return binding.root
    }
}