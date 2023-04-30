package com.example.travels_map.presentation.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.travels_map.R
import com.example.travels_map.TravelsMapApplication
import javax.inject.Inject

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    @Inject
    lateinit var mainActivityViewModelFactory: MainActivityViewModel.MainActivityViewModelFactory
    private val viewModel by viewModels<MainActivityViewModel> { mainActivityViewModelFactory }

    private val navController by lazy { findNavController(R.id.nav_host_fragment) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        TravelsMapApplication.INSTANCE.appComponent?.inject(this)
    }

    override fun onStart() {
        super.onStart()

        setupNavGraph()
    }

    @MainThread
    fun setupNavGraph() {
        when (viewModel.user == null) {
            true -> navController.setGraph(R.navigation.authentication_nav_graph)
            false -> navController.setGraph(R.navigation.main_nav_graph)
        }
    }
}