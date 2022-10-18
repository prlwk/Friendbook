package com.src.book.data.remote.model

import com.src.book.TestModelsGenerator
import com.src.book.TestModelsResponseGenerator
import com.src.book.data.remote.model.review.reviewBook.ReviewBookMapper
import io.mockk.junit4.MockKRule
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.*

@ExperimentalCoroutinesApi
class ReviewBookMapperTest {
    @get:Rule
    val rule = MockKRule(this)
    private lateinit var testModelsResponseGenerator: TestModelsResponseGenerator
    private lateinit var testModelsGenerator: TestModelsGenerator
    private lateinit var reviewBookMapper: ReviewBookMapper

    @Before
    fun setUp() {
        reviewBookMapper = ReviewBookMapper()
        testModelsResponseGenerator = TestModelsResponseGenerator()
        testModelsGenerator = TestModelsGenerator()
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    @Test
    fun testMapTagSuccessful() = runTest {
        val reviewResponseModel = testModelsResponseGenerator.generateReviewBookResponseModel()
        val reviewModel = testModelsGenerator.generateReviewModel()
        Assert.assertEquals(
            reviewModel,
            reviewBookMapper.mapFromResponseToModel(reviewResponseModel)
        )
    }
}