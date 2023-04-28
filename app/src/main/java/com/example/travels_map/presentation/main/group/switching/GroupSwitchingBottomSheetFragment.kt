package com.example.travels_map.presentation.main.group.switching

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.travels_map.R
import com.example.travels_map.databinding.FragmentGroupSwitchingBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class GroupSwitchingBottomSheetFragment : BottomSheetDialogFragment() {

    private val binding: FragmentGroupSwitchingBottomSheetBinding by viewBinding(CreateMethod.INFLATE)

    private val navController by lazy { requireActivity().findNavController(R.id.main_nav_host_fragment) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
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
            textViewSelectGroup.setOnClickListener {
                navController.navigate(GroupSwitchingBottomSheetFragmentDirections.actionGroupSwitchingBottomSheetFragmentToSelectGroupFragment())
            }

            textViewJoinGroup.setOnClickListener {
                navController.navigate(GroupSwitchingBottomSheetFragmentDirections.actionGroupSwitchingBottomSheetFragmentToJoinGroupFragment())
            }

            textViewCreateGroup.setOnClickListener {
                navController.navigate(GroupSwitchingBottomSheetFragmentDirections.actionGroupSwitchingBottomSheetFragmentToCreateGroupFragment())
            }
        }
    }
}