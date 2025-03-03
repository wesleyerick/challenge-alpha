package com.wesleyerick.podracer.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.wesleyerick.podracer.R
import com.wesleyerick.podracer.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val navController by lazy { findNavController() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupButtons()
        return binding.root
    }

    private fun setupButtons() = with(binding) {
        vehiclesButton.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_vehiclesFragment)
        }
        starshipsButton.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_starshipsFragment)
        }
    }
}