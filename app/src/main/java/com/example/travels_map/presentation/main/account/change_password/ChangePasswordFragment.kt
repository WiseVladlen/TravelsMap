package com.example.travels_map.presentation.main.account.change_password

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
import com.example.travels_map.databinding.FragmentChangePasswordBinding
import com.example.travels_map.utils.hideSoftKeyboard
import javax.inject.Inject

class ChangePasswordFragment : Fragment(R.layout.fragment_change_password) {

    @Inject
    lateinit var changePasswordViewModelFactory: ChangePasswordViewModel.ChangePasswordViewModelFactory
    private val viewModel by viewModels<ChangePasswordViewModel> { changePasswordViewModelFactory }

    private val binding by viewBinding(FragmentChangePasswordBinding::bind)

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
        setupOnClickListeners()
    }

    private fun setupOnClickListeners() {
        with(binding) {
            toolbar.setNavigationOnClickListener { navController.navigateUp() }

            fun changePassword() {
                viewModel.changePassword(
                    editTextNewPassword.text.toString(),
                    editTextNewPasswordAgain.text.toString(),
                ) {
                    navController.popBackStack(R.id.accountFragment, inclusive = false)
                }
            }

            textViewDone.setOnClickListener { changePassword() }

            editTextNewPasswordAgain.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    requireView().hideSoftKeyboard()
                    changePassword()
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
        }
    }
}