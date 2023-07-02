package com.example.travels_map.presentation.main.account

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.travels_map.R
import com.example.travels_map.TravelsMapApplication
import com.example.travels_map.databinding.FragmentAccountBinding
import com.example.travels_map.presentation.activity.MainActivity
import com.example.travels_map.utils.launchWhenCreated
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class AccountFragment : Fragment(R.layout.fragment_account) {

    @Inject
    lateinit var accountViewModelFactory: AccountViewModel.AccountViewModelFactory
    private val viewModel by navGraphViewModels<AccountViewModel>(R.id.nav_graph) { accountViewModelFactory }

    private val binding by viewBinding(FragmentAccountBinding::bind)

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
                textViewUsername.text = user.username
                textViewName.text = user.fullName
            }
        }.launchWhenCreated(viewLifecycleOwner)
    }

    private fun setupOnClickListeners() {
        with(binding) {
            editProfileButton.setOnClickListener {
                navController.navigate(AccountFragmentDirections.actionAccountFragmentToEditProfileFragment())
            }

            changePasswordButton.setOnClickListener {
                navController.navigate(AccountFragmentDirections.actionAccountFragmentToChangePasswordFragment())
            }

            logOutButton.setOnClickListener {
                viewModel.logOut { (requireActivity() as MainActivity).setupNavGraph() }
            }
        }
    }
}