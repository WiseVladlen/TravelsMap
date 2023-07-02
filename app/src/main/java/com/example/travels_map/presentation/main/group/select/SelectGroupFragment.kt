package com.example.travels_map.presentation.main.group.select

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.travels_map.R
import com.example.travels_map.TravelsMapApplication
import com.example.travels_map.databinding.FragmentSelectGroupBinding
import com.example.travels_map.presentation.main.group.select.adapter.GroupItemDelegationAdapter
import com.example.travels_map.utils.launchWhenStarted
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class SelectGroupFragment : Fragment(R.layout.fragment_select_group) {

    @Inject
    lateinit var selectGroupViewModelFactory: SelectGroupViewModel.SelectGroupViewModelFactory
    private val viewModel by viewModels<SelectGroupViewModel> { selectGroupViewModelFactory }

    private val binding by viewBinding(FragmentSelectGroupBinding::bind)

    private val navController by lazy { requireActivity().findNavController(R.id.main_nav_host_fragment) }

    private val groupAdapter = GroupItemDelegationAdapter { item ->
        viewModel.selectGroup(item.group) {
            navController.popBackStack(R.id.groupFragment, inclusive = false)
        }
    }

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
        setupRecyclerView()
        observeDataChanges()
    }

    private fun setupOnClickListeners() {
        binding.toolbar.setNavigationOnClickListener { navController.navigateUp() }
    }

    private fun setupRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(context)
        val dividerItemDecoration = DividerItemDecoration(context, linearLayoutManager.orientation)

        binding.availableGroupsRecyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = groupAdapter
            addItemDecoration(dividerItemDecoration)
        }
    }

    private fun observeDataChanges() {
        viewModel.loadingStateFlow.onEach { state ->
            when (state) {
                true -> binding.contentLoadingProgressBar.show()
                false -> binding.contentLoadingProgressBar.hide()
            }
        }.launchWhenStarted(viewLifecycleOwner)

        viewModel.groupListFlow.onEach { groupList ->
            groupAdapter.items = groupList
        }.launchWhenStarted(viewLifecycleOwner)
    }
}
