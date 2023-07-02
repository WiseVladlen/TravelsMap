package com.example.travels_map.presentation.main.group.add_participant

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.travels_map.R
import com.example.travels_map.TravelsMapApplication
import com.example.travels_map.databinding.FragmentAddParticipantBinding
import com.example.travels_map.utils.launchWhenCreated
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class AddParticipantFragment : Fragment(R.layout.fragment_add_participant) {

    @Inject
    lateinit var addParticipantViewModelFactory: AddParticipantViewModel.AddParticipantViewModelFactory
    private val viewModel by viewModels<AddParticipantViewModel> { addParticipantViewModelFactory }

    private val binding by viewBinding(FragmentAddParticipantBinding::bind)

    private val navController by lazy { requireActivity().findNavController(R.id.main_nav_host_fragment) }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        TravelsMapApplication.INSTANCE.appComponent?.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeView()
    }

    private fun initializeView() {
        observeDataChanges()
        setupOnClickListeners()
        setupToolbar()
    }

    private fun observeDataChanges() {
        viewModel.loadingStateFlow.onEach { state ->
            when (state) {
                true -> binding.contentLoadingProgressBar.show()
                false -> binding.contentLoadingProgressBar.hide()
            }
        }.launchWhenCreated(viewLifecycleOwner)

        viewModel.groupKeyFlow.onEach { key ->
            binding.textViewGroupKey.text = key
        }.launchWhenCreated(viewLifecycleOwner)
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { navController.navigateUp() }
    }

    private fun setupOnClickListeners() {
        with(binding) {
            textViewGroupKey.setOnClickListener {
                navController.navigate(
                    AddParticipantFragmentDirections.actionAddParticipantFragmentToGroupKeyManageBottomSheetFragment(
                        textViewGroupKey.text.toString(),
                    )
                )
            }
        }
    }
}