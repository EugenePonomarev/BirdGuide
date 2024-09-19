package com.greencodemoscow.redbook.redBook.presentation.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.greencodemoscow.redbook.BuildConfig.MAPKIT_API_KEY
import com.greencodemoscow.redbook.R
import com.greencodemoscow.redbook.core.ui.delegate.viewBinding
import com.greencodemoscow.redbook.databinding.FragmentRedBookItemBinding
import com.greencodemoscow.redbook.redBook.presentation.viewmodel.RedBookItemViewModel
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RedBookItemFragment : Fragment(R.layout.fragment_red_book_item) {

    private val binding by viewBinding(FragmentRedBookItemBinding::bind)
    private val viewModel: RedBookItemViewModel by viewModels()

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageBack.setOnClickListener {
            findNavController().popBackStack()
        }
        // Получаем аргументы
        val args: RedBookItemFragmentArgs by navArgs()
        val redBookItem = args.redBookItem

        // Используем полученные данные
        binding.header.text = redBookItem.name
        binding.description.text = redBookItem.description
        // Загрузите изображение, описание или любые другие данные

        checkPermission()
    }

    private fun checkPermission() {
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            viewModel.locationTracker()
        }

        permissionLauncher.launch(
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
            )
        )
    }
}