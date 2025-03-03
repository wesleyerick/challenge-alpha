package com.wesleyerick.podracer.view.vehicles.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.wesleyerick.podracer.R
import com.wesleyerick.podracer.data.model.vehicles.Vehicle
import com.wesleyerick.podracer.databinding.FragmentVehicleDetailsBinding
import com.wesleyerick.podracer.util.TypeEnum
import com.wesleyerick.podracer.util.getPhotoUrl
import com.wesleyerick.podracer.util.getSubtitleText
import com.wesleyerick.podracer.util.gone
import com.wesleyerick.podracer.util.listener
import com.wesleyerick.podracer.util.show
import com.wesleyerick.podracer.view.component.PodracerCircularProgress
import com.wesleyerick.podracer.view.vehicles.VehiclesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class VehicleDetailsFragment : Fragment() {

    private val arguments by navArgs<VehicleDetailsFragmentArgs>()
    private val vehicleId by lazy { arguments.vehicleId }
    private lateinit var binding: FragmentVehicleDetailsBinding

    private val viewModel by viewModel<VehiclesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentVehicleDetailsBinding.inflate(inflater, container, false)
        setupView()
        setupViewModel()
        return binding.root
    }

    private fun setupViewModel() = with(viewModel) {
        getDetails(vehicleId)

        vehicleDetails.listener(viewLifecycleOwner) {
            setImage(it)
            setVehicleText(it)
            isShowingProgress(it.name.isNotEmpty())
        }

        onError.listener(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                showErrorMessage(it)
                isShowingProgress(false)
            }
        }
    }

    private fun setupView() {
        setupProgress()
        isShowingProgress(true)

        binding.vehicleDetailsBackButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setVehicleText(it: Vehicle) = binding.apply {
        vehicleDetailsTitleText.text = it.name
        vehicleDetailsSubtitleOneText.text = getSubtitleText(
            context = requireContext(),
            beforeValue = getString(R.string.consumables_subtitle),
            value = it.consumables
        )
        vehicleDetailsSubtitleTwoText.text = getSubtitleText(
            context = requireContext(),
            beforeValue = getString(R.string.manufacturer_subtitle),
            value = it.manufacturer
        )
        vehicleDetailsSubtitleThreeText.text = getSubtitleText(
            context = requireContext(),
            beforeValue = getString(R.string.max_speed_subtitle),
            value = it.maxAtmospheringSpeed,
            afterValue = getString(R.string.kilometers_per_hour)
        )
        vehicleDetailsSubtitleFourText.text = getSubtitleText(
            context = requireContext(),
            beforeValue = getString(R.string.class_subtitle),
            value = it.vehicleClass
        )
    }


    private fun setImage(it: Vehicle) {
        Glide.with(requireContext())
            .load(getPhotoUrl(it.url, path = TypeEnum.VEHICLES.path))
            .centerCrop()
            .placeholder(R.drawable.placeholder)
            .into(binding.vehicleDetailsImage)
    }

    private fun showErrorMessage(it: String) = Toast
        .makeText(requireContext(), it, Toast.LENGTH_SHORT)
        .show()

    private fun isShowingProgress(isNotEmpty: Boolean) = with(binding) {
        if (isNotEmpty) {
            vehicleDetailsImage.show()
            vehicleDetailsTextListLinear.show()
            vehicleDetailsProgressBar.gone()
        } else {
            vehicleDetailsImage.gone()
            vehicleDetailsTextListLinear.gone()
            vehicleDetailsProgressBar.show()
        }
    }

    private fun setupProgress() = binding.vehicleDetailsProgressBar.setContent {
        PodracerCircularProgress()
    }
}