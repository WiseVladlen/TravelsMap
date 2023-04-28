package com.example.travels_map.presentation.main.group.error

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.travels_map.R
import com.example.travels_map.databinding.FragmentSelectedGroupErrorBinding

class SelectedGroupErrorFragment : Fragment(R.layout.fragment_selected_group_error) {

    private val binding by viewBinding(FragmentSelectedGroupErrorBinding::bind)

    private val navController by lazy { requireActivity().findNavController(R.id.main_nav_host_fragment) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeView()
    }

    private fun initializeView() {
        setupOnClickListeners()
    }

    private fun setupOnClickListeners() {
        with(binding) {
            selectGroupButton.setOnClickListener {
                navController.navigate(SelectedGroupErrorFragmentDirections.actionSelectedGroupErrorFragmentToSelectGroupFragment())
            }

            joinGroupButton.setOnClickListener {
                navController.navigate(SelectedGroupErrorFragmentDirections.actionSelectedGroupErrorFragmentToJoinGroupFragment())
            }

            createGroupButton.setOnClickListener {
                navController.navigate(SelectedGroupErrorFragmentDirections.actionSelectedGroupErrorFragmentToCreateGroupFragment())
            }
        }
    }
}