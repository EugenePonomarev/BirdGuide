package com.greencodemoscow.redbook.map.presentation.screen

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.greencodemoscow.redbook.BuildConfig.MAPKIT_API_KEY
import com.greencodemoscow.redbook.R
import com.greencodemoscow.redbook.core.ui.delegate.viewBinding
import com.greencodemoscow.redbook.databinding.FragmentMapBinding
import com.greencodemoscow.redbook.map.presentation.viewmodel.MapFragmentViewModel
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Circle
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.runtime.image.ImageProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MapFragment : Fragment(R.layout.fragment_map) {

    private val binding by viewBinding(FragmentMapBinding::bind)
    private val viewModel: MapFragmentViewModel by viewModels()

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey(MAPKIT_API_KEY)
        MapKitFactory.initialize(this.context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkPermission()

        initMapKit()

        stateSubscribe()
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

    private fun initMapKit() {
        // Перемещаем камеру на среднюю точку всех координат с увеличенным масштабом
        binding.mapView.mapWindow.map.move(
            CameraPosition(
                Point(55.711851, 37.507693), // Средняя точка для центра карты
                14f, // Увеличьте масштаб для лучшей видимости точек
                0f,
                0f
            )
        )

        // Включаем слой пользовательского местоположения
        val mapKit = MapKitFactory.getInstance()
        val locationMapKit = mapKit.createUserLocationLayer(binding.mapView.mapWindow)
        locationMapKit.isVisible = true

        // Принудительно обновляем карту
        binding.mapView.invalidate()
    }

    private fun stateSubscribe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { state ->
                    // Extract the points from the animal list
                    state.animalList.map { it.points }.let { addAnimalCirclesAndIcons(it, binding.mapView.mapWindow.map.mapObjects) }

                    // Extract the points from the plant list
                    state.plantList.map { it.points }.let { addPlantIcons(it, binding.mapView.mapWindow.map.mapObjects) }
                }
            }
        }
    }

    private fun addAnimalCirclesAndIcons(
        points: List<Point>,
        mapObjects: MapObjectCollection
    ) {
        val imageProvider = ImageProvider.fromResource(requireContext(), R.drawable.animal)
        val pinsCollection = binding.mapView.mapWindow.map.mapObjects.addCollection()
        points.forEach { point ->
            // Add circle
            val circle = Circle(point, 50.0f) // Радиус 50 метров
            mapObjects.addCircle(circle).apply {
                strokeColor = Color.argb(150, 255, 0, 0) // Красная граница
                strokeWidth = 2f
                fillColor = Color.argb(100, 255, 100, 100) // Полупрозрачная заливка
            }

            // Add icon
            pinsCollection.addPlacemark().apply {
                geometry = point
                setIcon(imageProvider)
            }
        }

        binding.mapView.invalidate() // Обновляем карту
    }

    private fun addPlantIcons(
        points: List<Point>,
        mapObjects: MapObjectCollection
    ) {
        val imageProvider = ImageProvider.fromResource(requireContext(), R.drawable.plant)
        val pinsCollection = binding.mapView.mapWindow.map.mapObjects.addCollection()
        points.forEach { point ->
            pinsCollection.addPlacemark().apply {
                geometry = point
                setIcon(imageProvider)
            }
        }

        binding.mapView.invalidate()
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        binding.mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }
}