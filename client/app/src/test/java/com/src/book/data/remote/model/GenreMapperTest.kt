package com.src.book.data.remote.model

import com.src.book.TestModelsGenerator
import com.src.book.TestModelsResponseGenerator
import com.src.book.data.remote.model.genre.GenreMapper
import io.mockk.junit4.MockKRule
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.*

@ExperimentalCoroutinesApi
class GenreMapperTest {
    @get:Rule
    val rule = MockKRule(this)
    private lateinit var testModelsResponseGenerator: TestModelsResponseGenerator
    private lateinit var testModelsGenerator: TestModelsGenerator
    private lateinit var genreMapper: GenreMapper

    @Before
    fun setUp() {
        genreMapper = GenreMapper()
        testModelsResponseGenerator = TestModelsResponseGenerator()
        testModelsGenerator = TestModelsGenerator()
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    @Test
    fun testMapGenreSuccessful() = runTest {
        val genreResponseModel = testModelsResponseGenerator.generateGenreResponseModel()
        val genreModel = testModelsGenerator.generateGenreModel()
        Assert.assertEquals(genreModel, genreMapper.mapFromResponseToModel(genreResponseModel))
    }
}