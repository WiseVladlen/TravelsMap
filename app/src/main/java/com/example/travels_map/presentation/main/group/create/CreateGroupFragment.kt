package com.example.travels_map.presentation.main.group.create

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
import com.example.travels_map.databinding.FragmentManageElementTitleBinding
import com.example.travels_map.utils.hideSoftKeyboard
import javax.inject.Inject

class CreateGroupFragment : Fragment(R.layout.fragment_manage_element_title) {

    @Inject
    lateinit var createGroupViewModelFactory: CreateGroupViewModel.CreateGroupViewModelFactory
    private val viewModel by viewModels<CreateGroupViewModel> { createGroupViewModelFactory }

    private val binding by viewBinding(FragmentManageElementTitleBinding::bind)

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
        setupToolbar()
        setupTextInputLayout()
        setupOnClickListeners()
    }

    private fun setupToolbar() {
        binding.toolbar.title = getString(R.string.create_group_title)
    }

    private fun setupTextInputLayout() {
        binding.textInputLayoutName.hint = getString(R.string.create_group_group_name_hint)
    }

    private fun setupOnClickListeners() {
        with(binding) {
            toolbar.setNavigationOnClickListener { navController.navigateUp() }

            editTextName.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    requireView().hideSoftKeyboard()
                    viewModel.createGroup(editTextName.text.toString()) {
                        navController.popBackStack(R.id.groupFragment, inclusive = false)
                    }
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }

            textViewDone.setOnClickListener {
                requireView().hideSoftKeyboard()
                viewModel.createGroup(editTextName.text.toString()) {
                    navController.popBackStack(R.id.groupFragment, inclusive = false)
                }
            }
        }
    }
}