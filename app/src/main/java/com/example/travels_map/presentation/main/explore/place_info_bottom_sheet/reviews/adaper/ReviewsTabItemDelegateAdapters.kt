package com.example.travels_map.presentation.main.explore.place_info_bottom_sheet.reviews.adaper

import com.example.travels_map.R
import com.example.travels_map.databinding.RateAndReviewLayoutBinding
import com.example.travels_map.databinding.RatingLayoutBinding
import com.example.travels_map.databinding.ReviewLayoutBinding
import com.example.travels_map.utils.date.getTimeAgo
import com.example.travels_map.utils.toString
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

fun ratingAdapterDelegate() = adapterDelegateViewBinding<ReviewsTabItem.RatingItem, ReviewsTabItem, RatingLayoutBinding>(
    { layoutInflater, parent -> RatingLayoutBinding.inflate(layoutInflater, parent, false) }
) {
    bind {
        binding.apply {
            item.place.feedback?.let { feedback ->
                textViewRating.text = feedback.rating.toString(1)

                ratingBar.rating = feedback.rating

                textViewNumberOfReviews.text = getString(R.string.rating_number_of_reviews, feedback.number)
            }
        }
    }
}

fun rateAndReviewAdapterDelegate(onRatingChanged: (Float) -> Unit) = adapterDelegateViewBinding<ReviewsTabItem.RateAndReviewItem, ReviewsTabItem, RateAndReviewLayoutBinding>(
    { layoutInflater, parent -> RateAndReviewLayoutBinding.inflate(layoutInflater, parent, false) }
) {
    bind {
        binding.ratingBar.apply {
            setOnRatingBarChangeListener { _, rating, fromUser ->
                if (fromUser) {
                    this.rating = 0f
                    onRatingChanged(rating)
                }
            }
        }
    }
}

fun reviewAdapterDelegate() = adapterDelegateViewBinding<ReviewsTabItem.ReviewItem, ReviewsTabItem, ReviewLayoutBinding>(
    { layoutInflater, parent -> ReviewLayoutBinding.inflate(layoutInflater, parent, false) }
) {
    bind {
        binding.apply {
            imageViewAvatar.setImageResource(R.drawable.ic_baseline_account_circle_48)

            textViewTitle.text = item.review.user.username
            textViewSubtitle.text = item.review.user.fullName

            ratingBar.rating = item.review.rating.toFloat()

            textViewDate.text = getTimeAgo(item.review.date)

            textViewReviewText.text = item.review.text
        }
    }
}