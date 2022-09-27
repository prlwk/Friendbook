package com.src.book.presentation.utils

import com.src.book.R

class RatingColor {
    companion object {
        fun getColor(rating: Double): Int {
            return if (rating == 0.0 || (rating > 3 && rating <= 6)) {
                R.color.neural_rating
            } else if (rating <= 3) {
                R.color.negative_rating
            } else {
                R.color.positive_rating
            }
        }

        fun getBackground(rating: Double): Int {
            return if (rating == 0.0 || (rating > 3 && rating <= 6)) {
                R.drawable.neural_rating_background
            } else if (rating <= 3) {
                R.drawable.negative_rating_background
            } else {
                R.drawable.positive_rating_background
            }
        }
    }
}