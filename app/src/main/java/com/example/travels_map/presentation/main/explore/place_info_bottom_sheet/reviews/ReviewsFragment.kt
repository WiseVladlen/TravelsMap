package com.example.travels_map.presentation.main.explore.place_info_bottom_sheet.reviews

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.travels_map.R
import com.example.travels_map.TravelsMapApplication
import com.example.travels_map.databinding.FragmentReviewsBinding
import com.example.travels_map.domain.entities.Place
import com.example.travels_map.presentation.main.explore.ExploreFragmentInternalDirections.actionOpenCreateReviewFragment
import com.example.travels_map.presentation.main.explore.place_info_bottom_sheet.reviews.adaper.ReviewsTabItem
import com.example.travels_map.presentation.main.explore.place_info_bottom_sheet.reviews.adaper.ReviewsTabItemDelegationAdapter
import com.example.travels_map.utils.launchWhenCreated
import kotlinx.coroutines.flow.zip
import javax.inject.Inject

class ReviewsFragment : Fragment(R.layout.fragment_reviews) {

    @Inject
    lateinit var reviewsViewModelFactory: ReviewsViewModel.ReviewsViewModelFactory
    private val viewModel by viewModels<ReviewsViewModel> { reviewsViewModelFactory }

    private val binding by viewBinding(FragmentReviewsBinding::bind)

    private val reviewsTabItemAdapter = ReviewsTabItemDelegationAdapter(::onRatingChanged)

    private fun getScreenItemList(place: Place) = mutableListOf(
        ReviewsTabItem.RatingItem(place),
        ReviewsTabItem.RateAndReviewItem,
    )

    override fun onAttach(context: Context) {
        super.onAttach(context)

        TravelsMapApplication.INSTANCE.appComponent?.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeView()
    }

    private fun initializeView() {
        setupRecyclerView()
        observeDataChanges()
    }

    private fun setupRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(context)

        binding.recyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = reviewsTabItemAdapter
            addItemDecoration(DividerItemDecoration(context, linearLayoutManager.orientation))
        }
    }

    private fun observeDataChanges() {
        viewModel.placeFlow.zip(viewModel.reviewListFlow) { place, reviewList ->
            reviewsTabItemAdapter.items = getScreenItemList(place).apply {
                addAll(reviewList.map { ReviewsTabItem.ReviewItem(it) })
            }
        }.launchWhenCreated(this)
    }

    private fun onRatingChanged(rating: Float) = actionOpenCreateReviewFragment(rating)

    companion object {
        fun newInstance() = ReviewsFragment()
    }
}