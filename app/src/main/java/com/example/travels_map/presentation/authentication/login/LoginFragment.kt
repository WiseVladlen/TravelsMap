package com.example.travels_map.presentation.authentication.login

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.travels_map.R
import com.example.travels_map.TravelsMapApplication
import com.example.travels_map.databinding.FragmentLoginBinding
import com.example.travels_map.presentation.activity.MainActivity
import javax.inject.Inject

class LoginFragment : Fragment(R.layout.fragment_login) {

    @Inject
    lateinit var loginViewModelFactory: LoginViewModel.LoginViewModelFactory
    private val viewModel by viewModels<LoginViewModel> { loginViewModelFactory }

    private val binding by viewBinding(FragmentLoginBinding::bind)

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
            logInButton.setOnClickListener {
                viewModel.logIn(
                    editTextUsername.text.toString(),
                    editTextPassword.text.toString(),
                ) {
                    (requireActivity() as MainActivity).setupNavGraph()
                }
            }

            textViewSignUp.setOnClickListener {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegistrationFragment())
            }
        }
    }
}