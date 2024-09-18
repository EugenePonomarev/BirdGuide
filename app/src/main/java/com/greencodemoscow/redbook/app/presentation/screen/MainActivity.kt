package com.greencodemoscow.redbook.app.presentation.screen

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.transition.Visibility
import com.greencodemoscow.redbook.R
import com.greencodemoscow.redbook.app.presentation.viewmodel.MainViewModel
import com.greencodemoscow.redbook.databinding.ActivityMainBinding
import com.greencodemoscow.redbook.databinding.SplashScreenBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        checkPermission()
        stateSubscribe()
    }

    private fun stateSubscribe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { state ->
                    if (state.isLoading) {
                        val pulseAnimation = AnimationUtils.loadAnimation(this@MainActivity, R.anim.pulse)
                        binding.splashLayout.splashImage.startAnimation(pulseAnimation)
                        binding.mainLayout.mainLayout.visibility = View.GONE
                        binding.splashLayout.splashLayout.visibility = View.VISIBLE
                    } else {
                        binding.mainLayout.mainLayout.visibility = View.VISIBLE
                        binding.splashLayout.splashLayout.visibility = View.GONE
                        initNavHost()
                    }
                }
            }
        }
    }


    private fun initNavHost() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        binding.mainLayout.bottomNavigationView.setupWithNavController(navController)
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