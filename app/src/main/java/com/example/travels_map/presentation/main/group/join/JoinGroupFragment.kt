package com.example.travels_map.presentation.main.group.join

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.travels_map.R
import com.example.travels_map.TravelsMapApplication
import com.example.travels_map.databinding.FragmentJoinGroupBinding
import com.example.travels_map.utils.hideSoftKeyboard
import javax.inject.Inject

class JoinGroupFragment : Fragment(R.layout.fragment_join_group) {

    @Inject
    lateinit var joinGroupViewModelFactory: JoinGroupViewModel.JoinGroupViewModelFactory
    private val viewModel by viewModels<JoinGroupViewModel> { joinGroupViewModelFactory }

    private val binding by viewBinding(FragmentJoinGroupBinding::bind)

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
        setupOnClickListeners()
    }

    private fun setupOnClickListeners() {
        with(binding) {
            toolbar.setNavigationOnClickListener { navController.navigateUp() }

            editTextQuery.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    requireView().hideSoftKeyboard()
                    viewModel.joinGroup(editTextQuery.text.toString()) {
                        navController.popBackStack(R.id.groupFragment, inclusive = false)
                    }
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }

            sendRequestButton.setOnClickListener {
                requireView().hideSoftKeyboard()
                viewModel.joinGroup(editTextQuery.text.toString()) {
                    navController.popBackStack(R.id.groupFragment, inclusive = false)
                }
            }
        }
    }
}