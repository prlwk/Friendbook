package com.src.book.presentation.book.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.src.book.R
import com.src.book.databinding.FragmentReviewListBinding
import com.src.book.domain.model.ExtendedReview
import com.src.book.presentation.book.review.adapter.ReviewListAdapter
import kotlin.random.Random

class ReviewListFragment : Fragment() {

    private lateinit var binding: FragmentReviewListBinding
    private val reviewListAdapter = ReviewListAdapter()
    private lateinit var reviews: List<ExtendedReview>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_review_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentReviewListBinding.bind(view);

        reviews = (1..10).map {
            ExtendedReview(
                id = it.toLong(),
                username = "Андрей",
                reviewText = getString(R.string.temp_string),
                rating = Random.nextInt(1, 11),
                reviewDate = "15.05.2022",
                photoSrc = null
            )
        }.toMutableList()

        setAdapterForExtendedReviewRecyclerView(reviews)

        calculateAllReviewsButtonText()
        binding.allReviewsButton.setOnClickListener { onAllReviewsButtonClick() }
        binding.positiveReviewsButton.setOnClickListener { onPositiveReviewsButtonClick() }
        binding.neutralReviewsButton.setOnClickListener { onNeutralReviewsButtonClick() }
        binding.negativeReviewsButton.setOnClickListener { onNegativeReviewsButtonClick() }
    }

    private fun setAdapterForExtendedReviewRecyclerView(reviews: List<ExtendedReview>) {
        reviewListAdapter.submitList(reviews)
        binding.rvReviews.adapter = reviewListAdapter
        binding.rvReviews.isNestedScrollingEnabled = false
    }

    private fun updateExtendedReviewRecyclerView(newReviews: List<ExtendedReview>) {
        reviewListAdapter.submitList(newReviews)
    }

    private fun onAllReviewsButtonClick() {
        updateExtendedReviewRecyclerView(reviews)
    }

    private fun onPositiveReviewsButtonClick() {
        val newReviews = reviews.filter { it.rating in 7..10 }.toMutableList()
        updateExtendedReviewRecyclerView(newReviews)
    }

    private fun onNeutralReviewsButtonClick() {
        val newReviews = reviews.filter { it.rating in 4..6 }.toMutableList()
        updateExtendedReviewRecyclerView(newReviews)
    }

    private fun onNegativeReviewsButtonClick() {
        val newReviews = reviews.filter { it.rating in 1..3 }.toMutableList()
        updateExtendedReviewRecyclerView(newReviews)
    }

    private fun calculateAllReviewsButtonText() {
        if (reviews.size > 9999) {
            binding.tvAllReviews.text =
                getString(R.string.all_reviews, (reviews.size / 1000).toString() + "k")
        } else {
            binding.tvAllReviews.text = getString(R.string.all_reviews, reviews.size.toString())
        }
    }
}