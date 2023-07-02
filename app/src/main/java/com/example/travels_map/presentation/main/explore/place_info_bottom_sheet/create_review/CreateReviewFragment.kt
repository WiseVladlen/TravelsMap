package com.example.travels_map.presentation.main.explore.place_info_bottom_sheet.create_review

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.travels_map.R
import com.example.travels_map.TravelsMapApplication
import com.example.travels_map.databinding.FragmentCreateReviewBinding
import com.example.travels_map.presentation.main.explore.ExploreFragmentInternalDirections.actionCloseFragment
import com.example.travels_map.presentation.main.explore.SelectedPlaceViewModel
import com.example.travels_map.utils.hideSoftKeyboard
import com.example.travels_map.utils.showSoftKeyboard
import javax.inject.Inject

class CreateReviewFragment : Fragment(R.layout.fragment_create_review) {

    @Inject
    lateinit var createReviewViewModelFactory: CreateReviewViewModel.CreateReviewViewModelFactory
    private val viewModel by viewModels<CreateReviewViewModel> { createReviewViewModelFactory }

    private val selectedPlaceViewModel by viewModels<SelectedPlaceViewModel>({ requireParentFragment() })

    private val binding by viewBinding(FragmentCreateReviewBinding::bind)

    override fun onAttach(context: Context) {
        super.onAttach(context)

        TravelsMapApplication.INSTANCE.appComponent?.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeView()
    }

    private fun initializeView() {
        setupEditText()
        setupToolbar()
        setupRatingBar()
        setupOnClickListeners()
    }

    private fun setupEditText() {
        binding.editTextReview.showSoftKeyboard()
    }

    private fun setupToolbar() {
        binding.toolbar.title = selectedPlaceViewModel.value.name
    }

    private fun setupRatingBar() {
        binding.ratingBar.rating = arguments?.getFloat(RATING) ?: 0f
    }

    private fun setupOnClickListeners() {
        binding.apply {
            toolbar.setNavigationOnClickListener { actionCloseFragment() }

            postButton.setOnClickListener {
                requireView().hideSoftKeyboard()
                viewModel.createReview(
                    editTextReview.text.toString(),
                    ratingBar.rating.toInt(),
                    selectedPlaceViewModel.value,
                ) {
                    actionCloseFragment()
                }
            }
        }
    }

    companion object {
        private const val RATING = "RATING"

        fun newInstance(rating: Float) = CreateReviewFragment().apply {
            arguments = bundleOf(RATING to rating)
        }
    }
}