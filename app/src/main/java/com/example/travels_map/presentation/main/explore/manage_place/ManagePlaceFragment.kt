package com.example.travels_map.presentation.main.explore.manage_place

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
import com.example.travels_map.presentation.main.explore.SelectedPlaceViewModel
import com.example.travels_map.utils.handleTouch
import com.example.travels_map.utils.hideSoftKeyboard
import com.example.travels_map.utils.onTouch
import com.example.travels_map.utils.showSoftKeyboard
import javax.inject.Inject

class ManagePlaceFragment : Fragment(R.layout.fragment_manage_element_title) {

    @Inject
    lateinit var managePlaceViewModelFactory: ManagePlaceViewModel.ManagePlaceViewModelFactory
    private val viewModel by viewModels<ManagePlaceViewModel> { managePlaceViewModelFactory }

    private val selectedPlaceViewModel by viewModels<SelectedPlaceViewModel>({ requireParentFragment() })

    private val binding by viewBinding(FragmentManageElementTitleBinding::bind)

    override fun onAttach(context: Context) {
        super.onAttach(context)

        TravelsMapApplication.INSTANCE.appComponent?.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeView()
    }

    private fun initializeView() {
        setupToolbar()
        setupEditText()
        setupTouchListener()
        setupOnClickListeners()
    }

    private fun setupToolbar() {
        binding.toolbar.title = if (selectedPlaceViewModel.value.name == null) {
            getString(R.string.manage_place_create)
        } else {
            getString(R.string.manage_place_edit)
        }
    }

    private fun setupEditText() {
        binding.editTextName.apply {
            showSoftKeyboard()
            setText(selectedPlaceViewModel.value.name)
        }
    }

    private fun setupTouchListener() {
        binding.root.onTouch(::handleTouch)
    }

    private fun setupOnClickListeners() {
        with(binding) {
            toolbar.setNavigationOnClickListener { actionCloseFragment() }

            fun savePlace() {
                requireView().hideSoftKeyboard()
                viewModel.savePlace(selectedPlaceViewModel.value.copy(name = editTextName.text.toString())) {
                    actionCloseFragment()
                }
            }

            editTextName.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    savePlace()
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }

            textViewDone.setOnClickListener {
                savePlace()
            }
        }
    }

    companion object {
        fun newInstance() = ManagePlaceFragment()
    }
}