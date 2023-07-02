package com.example.travels_map.presentation.main.explore.create_route

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.travels_map.R
import com.example.travels_map.TravelsMapApplication
import com.example.travels_map.databinding.FragmentManageElementTitleBinding
import com.example.travels_map.presentation.main.explore.ExploreFragmentInternalDirections.actionCloseFragment
import com.example.travels_map.presentation.main.explore.ExploreFragmentInternalDirections.actionNavigateToPlaceInfoBottomSheetFragment
import com.example.travels_map.utils.handleTouch
import com.example.travels_map.utils.hideSoftKeyboard
import com.example.travels_map.utils.onTouch
import com.example.travels_map.utils.showSoftKeyboard
import javax.inject.Inject

class CreateRouteFragment : Fragment(R.layout.fragment_manage_element_title) {

    @Inject
    lateinit var createRouteViewModelFactory: CreateRouteViewModel.CreateRouteViewModelFactory
    private val viewModel by viewModels<CreateRouteViewModel> { createRouteViewModelFactory }

    private val binding by viewBinding(FragmentManageElementTitleBinding::bind)

    override fun onAttach(context: Context) {
        super.onAttach(context)

        TravelsMapApplication.INSTANCE.appComponent?.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeView()
    }

    override fun onDestroy() {
        super.onDestroy()

        actionNavigateToPlaceInfoBottomSheetFragment()
    }

    private fun initializeView() {
        setupToolbar()
        setupEditText()
        setupTouchListener()
        setupOnClickListeners()
    }

    private fun setupToolbar() {
        binding.toolbar.title = getString(R.string.manage_route_name_title)
    }

    private fun setupEditText() {
        binding.editTextName.showSoftKeyboard()
    }

    private fun setupTouchListener() {
        binding.root.onTouch(::handleTouch)
    }

    private fun setupOnClickListeners() {
        with(binding) {
            toolbar.setNavigationOnClickListener {
                actionCloseFragment()
            }

            fun saveRouteName() {
                requireView().hideSoftKeyboard()
                viewModel.createRoute(editTextName.text.toString()) {
                    actionCloseFragment()
                }
            }

            editTextName.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    saveRouteName()
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }

            textViewDone.setOnClickListener {
                saveRouteName()
            }
        }
    }

    companion object {
        fun newInstance() = CreateRouteFragment()
    }
}