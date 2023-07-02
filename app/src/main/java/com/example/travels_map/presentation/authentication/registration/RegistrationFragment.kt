package com.example.travels_map.presentation.authentication.registration

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.travels_map.R
import com.example.travels_map.TravelsMapApplication
import com.example.travels_map.databinding.FragmentRegistrationBinding
import com.example.travels_map.presentation.activity.MainActivity
import com.example.travels_map.utils.hideSoftKeyboard
import javax.inject.Inject

class RegistrationFragment : Fragment(R.layout.fragment_registration) {

    @Inject
    lateinit var registrationViewModelFactory: RegistrationViewModel.RegistrationViewModelFactory
    private val viewModel by viewModels<RegistrationViewModel> { registrationViewModelFactory }

    private val binding by viewBinding(FragmentRegistrationBinding::bind)

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
            signUpButton.setOnClickListener {
                requireView().hideSoftKeyboard()
                viewModel.signUp(
                    editTextUsername.text.toString(),
                    editTextFullName.text.toString(),
                    editTextPassword.text.toString(),
                ) {
                    (requireActivity() as MainActivity).setupNavGraph()
                }
            }

            textViewSignIn.setOnClickListener {
                findNavController().navigate(RegistrationFragmentDirections.actionRegistrationFragmentToLoginFragment())
            }
        }
    }
}