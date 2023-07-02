package com.example.travels_map.presentation.main.explore.place_info_bottom_sheet.overview

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
import com.example.travels_map.databinding.FragmentOverviewBinding
import com.example.travels_map.domain.entities.Place
import com.example.travels_map.presentation.main.explore.ExploreFragmentInternalDirections.actionOpenCreateReviewFragment
import com.example.travels_map.presentation.main.explore.place_info_bottom_sheet.overview.adapter.OverviewTabDelegationAdapter
import com.example.travels_map.presentation.main.explore.place_info_bottom_sheet.overview.adapter.OverviewTabItem
import com.example.travels_map.utils.launchWhenCreated
import kotlinx.coroutines.flow.zip
import javax.inject.Inject

class OverviewFragment : Fragment(R.layout.fragment_overview) {

    @Inject
    lateinit var overviewViewModelFactory: OverviewViewModel.OverviewViewModelFactory
    private val viewModel by viewModels<OverviewViewModel> { overviewViewModelFactory }

    private val binding by viewBinding(FragmentOverviewBinding::bind)

    private val overviewTabItemAdapter = OverviewTabDelegationAdapter(::onRatingChanged)

    private fun getScreenItemList(place: Place) = mutableListOf(
        OverviewTabItem.PlaceCoordinatesItem(place),
        OverviewTabItem.RatingItem(place),
        OverviewTabItem.RateAndReviewItem,
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
            layoutManager = LinearLayoutManager(context)
            adapter = overviewTabItemAdapter
            addItemDecoration(DividerItemDecoration(context, linearLayoutManager.orientation))
        }
    }

    private fun observeDataChanges() {
        viewModel.placeFlow.zip(viewModel.reviewListFlow) { place, reviewList ->
            overviewTabItemAdapter.items = when (val lastReview = reviewList.lastOrNull()) {
                null -> getScreenItemList(place)
                else -> getScreenItemList(place).apply { add(OverviewTabItem.ReviewItem(lastReview)) }
            }
        }.launchWhenCreated(this)
    }

    private fun onRatingChanged(rating: Float) = actionOpenCreateReviewFragment(rating)

    companion object {
        fun newInstance() = OverviewFragment()
    }
}