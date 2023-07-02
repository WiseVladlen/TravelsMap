package com.example.travels_map.presentation.main.account.edit_profile

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.travels_map.R
import com.example.travels_map.TravelsMapApplication
import com.example.travels_map.databinding.FragmentEditProfileBinding
import com.example.travels_map.utils.launchWhenCreated
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class EditProfileFragment : Fragment(R.layout.fragment_edit_profile) {

    @Inject
    lateinit var editProfileViewModelFactory: EditProfileViewModel.EditProfileViewModelFactory
    private val viewModel by viewModels<EditProfileViewModel> { editProfileViewModelFactory }

    private val binding by viewBinding(FragmentEditProfileBinding::bind)

    private val navController by lazy { requireActivity().findNavController(R.id.main_nav_host_fragment) }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        TravelsMapApplication.INSTANCE.appComponent?.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews()
    }

    private fun initializeViews() {
        observeDataChanges()
        setupOnClickListeners()
    }

    private fun observeDataChanges() {
        viewModel.userFlow.onEach { user ->
            with(binding) {
                editTextUsername.setText(user.username)
                editTextFullName.setText(user.fullName)
            }
        }.launchWhenCreated(viewLifecycleOwner)
    }

    private fun setupOnClickListeners() {
        with(binding) {
            toolbar.setNavigationOnClickListener { navController.navigateUp() }

            textViewDone.setOnClickListener {
                viewModel.editProfile(
                    editTextUsername.text.toString(),
                    editTextFullName.text.toString(),
                ) {
                    navController.popBackStack(R.id.accountFragment, inclusive = false)
                }
            }

            textViewEditPicture.setOnClickListener { navController.navigateUp() }
        }
    }
}