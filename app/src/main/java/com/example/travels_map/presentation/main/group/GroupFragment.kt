package com.example.travels_map.presentation.main.group

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.travels_map.R
import com.example.travels_map.TravelsMapApplication
import com.example.travels_map.databinding.FragmentGroupBinding
import com.example.travels_map.domain.entities.Group
import com.example.travels_map.presentation.main.group.adapter.GroupStructureItem
import com.example.travels_map.presentation.main.group.adapter.GroupStructureItemDelegationAdapter
import com.example.travels_map.presentation.main.group.adapter.Attributes
import com.example.travels_map.utils.launchWhenStarted
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class GroupFragment : Fragment(R.layout.fragment_group) {

    @Inject
    lateinit var groupViewModelFactory: GroupViewModel.GroupViewModelFactory
    private val viewModel by navGraphViewModels<GroupViewModel>(R.id.nav_graph) { groupViewModelFactory }

    private val binding by viewBinding(FragmentGroupBinding::bind)

    private val navController by lazy { requireActivity().findNavController(R.id.main_nav_host_fragment) }

    private val groupStructureAdapter = GroupStructureItemDelegationAdapter(::onActionItemClick)

    override fun onAttach(context: Context) {
        super.onAttach(context)

        TravelsMapApplication.INSTANCE.appComponent?.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeView()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        groupStructureAdapter.items = listOf()
    }

    private fun initializeView() {
        setupRecyclerView()
        observeDataChanges()
    }

    private fun setupRecyclerView() {
        binding.groupItemViewRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = groupStructureAdapter
        }
    }

    private fun observeDataChanges() {
        viewModel.loadingStateFlow.onEach { isLoading ->
            when (isLoading) {
                true -> binding.contentLoadingProgressBar.show()
                false -> binding.contentLoadingProgressBar.hide()
            }
        }.launchWhenStarted(viewLifecycleOwner)

        viewModel.errorStateFlow.onEach { isError ->
            if (isError) {
                navController.navigate(GroupFragmentDirections.actionGroupFragmentToSelectedGroupErrorFragment())
            }
        }.launchWhenStarted(viewLifecycleOwner)

        viewModel.groupFlow.onEach { group ->
            submitGroupData(group)
        }.launchWhenStarted(viewLifecycleOwner)
    }

    private fun onActionItemClick(item: GroupStructureItem.ActionItem) {
        when (item) {
            is GroupStructureItem.ActionItem.AddParticipant -> {
                navController.navigate(GroupFragmentDirections.actionGroupFragmentToAddParticipantFragment())
            }
            is GroupStructureItem.ActionItem.Switch -> {
                navController.navigate(GroupFragmentDirections.actionGroupFragmentToGroupSwitchingBottomSheetFragment())
            }
            is GroupStructureItem.ActionItem.Leave -> viewModel.leaveGroup()
        }
    }

    private fun submitGroupData(group: Group) {
        val list = mutableListOf(
            GroupStructureItem.HeaderItem(group),
            GroupStructureItem.ActionItem.AddParticipant(
                Attributes(
                    R.drawable.ic_baseline_person_add_24,
                    R.string.add_participant_title,
                )
            ),
            GroupStructureItem.ActionItem.Switch(
                Attributes(
                    R.drawable.ic_baseline_switch_24,
                    R.string.group_switch,
                )
            ),
            GroupStructureItem.ActionItem.Leave(
                Attributes(
                    R.drawable.ic_baseline_logout_24,
                    R.string.group_leave,
                )
            ),
            GroupStructureItem.ParticipantCountItem(group.participants.size),
        )
        list.addAll(group.participants.map { GroupStructureItem.ParticipantItem(it) })

        groupStructureAdapter.items = list
    }
}