package com.example.carmatch.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.carmatch1.R
import com.example.carmatch1.databinding.FragmentAdVehiclesBinding

class AdVehiclesFragment : Fragment() {
    
    private lateinit var binding: FragmentAdVehiclesBinding
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdVehiclesBinding.inflate(inflater, container, false)
        
        // Configuração do botão "Ver mais"
        binding.btnViewMore.setOnClickListener {
            val isVisible = binding.additionalInfoLayout.visibility == View.VISIBLE
            (if (isVisible) View.GONE else View.VISIBLE).also { it.also { it.also { binding.additionalInfoLayout.visibility = it } } }
        }
        
        return binding.root
    }
}
