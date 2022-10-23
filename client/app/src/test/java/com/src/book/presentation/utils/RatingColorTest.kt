package com.src.book.presentation.utils

import com.src.book.R
import org.junit.*


class RatingColorTest {
    @Test
    fun testNegativeRatingColor() {
        Assert.assertEquals(R.color.negative_rating, RatingColor.getColor(1.0))
        Assert.assertEquals(R.color.negative_rating, RatingColor.getColor(2.0))
        Assert.assertEquals(R.color.negative_rating, RatingColor.getColor(3.0))
        Assert.assertEquals(R.color.negative_rating, RatingColor.getColor(4.0))
    }

    @Test
    fun testNeuralRatingColor() {
        Assert.assertEquals(R.color.neural_rating, RatingColor.getColor(5.0))
        Assert.assertEquals(R.color.neural_rating, RatingColor.getColor(6.0))
    }

    @Test
    fun testPositiveRating() {
        Assert.assertEquals(R.color.positive_rating, RatingColor.getColor(7.0))
        Assert.assertEquals(R.color.positive_rating, RatingColor.getColor(8.0))
        Assert.assertEquals(R.color.positive_rating, RatingColor.getColor(9.0))
        Assert.assertEquals(R.color.positive_rating, RatingColor.getColor(10.0))
    }

    @Test
    fun testNegativeRatingBackground() {
        Assert.assertEquals(R.drawable.negative_rating_background, RatingColor.getBackground(1.0))
        Assert.assertEquals(R.drawable.negative_rating_background, RatingColor.getBackground(2.0))
        Assert.assertEquals(R.drawable.negative_rating_background, RatingColor.getBackground(3.0))
        Assert.assertEquals(R.drawable.negative_rating_background, RatingColor.getBackground(4.0))
    }

    @Test
    fun testNeuralRatingBackground() {
        Assert.assertEquals(R.drawable.neural_rating_background, RatingColor.getBackground(5.0))
        Assert.assertEquals(R.drawable.neural_rating_background, RatingColor.getBackground(6.0))
    }

    @Test
    fun testPositiveRatingBackground() {
        Assert.assertEquals(R.drawable.positive_rating_background, RatingColor.getBackground(7.0))
        Assert.assertEquals(R.drawable.positive_rating_background, RatingColor.getBackground(8.0))
        Assert.assertEquals(R.drawable.positive_rating_background, RatingColor.getBackground(9.0))
        Assert.assertEquals(R.drawable.positive_rating_background, RatingColor.getBackground(10.0))
    }
}