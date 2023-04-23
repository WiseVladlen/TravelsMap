package com.example.travels_map.presentation.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.travels_map.R
import com.example.travels_map.TravelsMapApplication
import com.example.travels_map.utils.launch
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    @Inject
    lateinit var mainActivityViewModelFactory: MainActivityViewModel.MainActivityViewModelFactory
    private val viewModel by viewModels<MainActivityViewModel> { mainActivityViewModelFactory }

    private val navController: NavController by lazy {
        findNavController(R.id.nav_host_fragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        TravelsMapApplication.INSTANCE.appComponent?.inject(this)

        observeDataChanges()
    }

    private fun observeDataChanges() {
        viewModel.currentUserStateFlow.onEach { isNotNull ->
            when (isNotNull) {
                true -> navController.setGraph(R.navigation.main_nav_graph)
                false -> navController.setGraph(R.navigation.authentication_nav_graph)
            }
        }.launch(this, Lifecycle.State.STARTED)
    }
}