package com.example.travels_map.presentation.main.group.add_participant.group_key_manage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.travels_map.R
import com.example.travels_map.databinding.FragmentContentControlBottomSheetBinding
import com.example.travels_map.utils.copyTextToClipboard
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class GroupKeyManageBottomSheetFragment : BottomSheetDialogFragment() {

    private val binding: FragmentContentControlBottomSheetBinding by viewBinding(CreateMethod.INFLATE)

    private val args: GroupKeyManageBottomSheetFragmentArgs by navArgs()

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
        binding.apply {
            textViewCopy.apply {
                text = getString(R.string.group_key_manage_copy)
                setOnClickListener {
                    requireActivity().copyTextToClipboard(LABEL, args.key) {
                        dismiss()
                        Toast.makeText(context, CALLBACK, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            textViewShare.apply {
                text = getString(R.string.group_key_manage_share)
                setOnClickListener {
                    dismiss()
                }
            }
        }
    }

    private companion object GroupKeyCopying {
        const val LABEL = "GROUP_KEY"
        const val CALLBACK = "Group key copied to clipboard!"
    }
}