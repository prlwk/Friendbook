package com.src.book.presentation.main.main_page.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.src.book.domain.model.book.tag.TagWithTitle
import com.src.book.domain.author.AuthorList
import com.src.book.domain.model.Genre
import com.src.book.domain.model.Tag
import com.src.book.domain.model.book.BookList
import com.src.book.domain.model.book.genre.GenreWithCheck
import com.src.book.domain.model.book.genre.GenreWithTitle
import com.src.book.domain.model.book.tag.TagWithCheck
import com.src.book.domain.usecase.author.SearchAuthorsUseCase
import com.src.book.domain.usecase.author.SearchAuthorsWithPaginationUseCase
import com.src.book.domain.usecase.book.*
import com.src.book.domain.utils.BasicState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainPageViewModel(
    private val searchBooksUseCase: SearchBooksUseCase,
    private val searchAuthorsUseCase: SearchAuthorsUseCase,
    private val searchAuthorsWithPaginationUseCase: SearchAuthorsWithPaginationUseCase,
    private val searchBooksWithPaginationUseCase: SearchBooksWithPaginationUseCase,
    private val getPopularGenresUseCase: GetPopularGenresUseCase,
    private val getPopularTagsUseCase: GetPopularTagsUseCase,
    private val getAllTagsUseCase: GetAllTagsUseCase,
    private val getAllGenresUseCase: GetAllGenresUseCase,
) : ViewModel() {
    private val _mutableLiveDataBookPopularity =
        MutableLiveData<BasicState<List<BookList>>>(BasicState.DefaultState())
    private val _mutableLiveDataTheBestAuthors =
        MutableLiveData<BasicState<List<AuthorList>>>(BasicState.DefaultState())

    private val _mutableLivDataPopularGenres =
        MutableLiveData<BasicState<List<Genre>>>(BasicState.DefaultState())

    private val _mutableLiveDataPopularGenresFilter =
        MutableLiveData<BasicState<List<GenreWithCheck>>>(BasicState.DefaultState())
    private val _mutableLiveDataSelectedGenres =
        MutableLiveData<BasicState<List<GenreWithCheck>>>(BasicState.DefaultState())
    private val _mutableLiveDataAllGenresFilter =
        MutableLiveData<BasicState<List<GenreWithTitle>>>(BasicState.DefaultState())
    private val _mutableLiveDataGenresForMainFilter =
        MutableLiveData<BasicState<List<GenreWithCheck>>>(BasicState.DefaultState())

    private val _mutableLiveDataPopularTags =
        MutableLiveData<BasicState<List<TagWithCheck>>>(BasicState.DefaultState())
    private val _mutableLiveDataSelectedTags =
        MutableLiveData<BasicState<List<TagWithCheck>>>(BasicState.DefaultState())
    private val _mutableLiveDataAllTagsFilter =
        MutableLiveData<BasicState<List<TagWithTitle>>>(BasicState.DefaultState())
    private val _mutableLiveDataTagsForMainFilter =
        MutableLiveData<BasicState<List<TagWithCheck>>>(BasicState.DefaultState())

    val liveDataBookPopularity get() = _mutableLiveDataBookPopularity
    val liveDataTheBestAuthors get() = _mutableLiveDataTheBestAuthors

    val liveDataPopularGenres get() = _mutableLivDataPopularGenres
    val liveDataPopularGenresForFilter get() = _mutableLiveDataPopularGenresFilter
    val liveDataAllGenresFilter get() = _mutableLiveDataAllGenresFilter
    val liveDataGenresForMainFilter get() = _mutableLiveDataGenresForMainFilter

    val liveDataPopularTags get() = _mutableLiveDataPopularTags
    val liveDataAllTagsFilter get() = _mutableLiveDataAllTagsFilter
    val liveDataTagsForMainFilter get() = _mutableLiveDataTagsForMainFilter

    fun getPopularBooks() {
        viewModelScope.launch {
            _mutableLiveDataBookPopularity.value = BasicState.LoadingState()
            var state = searchBooksUseCase.execute(
                numberPage = 0,
                sizePage = SIZE_PAGE,
                word = null,
                sort = SORT_POPULARITY,
                startRating = null,
                finishRating = null,
                tags = null,
                genres = null
            )
            if (state is BasicState.EmptyState) {
                state = BasicState.ErrorState()
            }
            _mutableLiveDataBookPopularity.value = state
        }
    }

    fun getBestAuthors() {
        viewModelScope.launch {
            _mutableLiveDataTheBestAuthors.value = BasicState.LoadingState()
            var state = searchAuthorsUseCase.execute(
                numberPage = 0,
                sizePage = SIZE_PAGE,
                word = null,
                sort = SORT_RATING,
                startRating = null,
                finishRating = null,
            )
            if (state is BasicState.EmptyState) {
                state = BasicState.ErrorState()
            }
            _mutableLiveDataTheBestAuthors.value = state
        }
    }

    fun getAuthorResult(
        word: String?,
        sort: String?,
        startRating: Int?,
        finishRating: Int?
    ): Flow<PagingData<AuthorList>> {
        return searchAuthorsWithPaginationUseCase.execute(
            sizePage = SIZE_PAGE,
            word = word,
            sort = sort,
            startRating = startRating,
            finishRating = finishRating
        )
            .cachedIn(viewModelScope)
    }

    fun getBookResult(
        word: String?,
        sort: String?,
        startRating: Int?,
        finishRating: Int?,
        genres: String?,
        tags: String?
    ): Flow<PagingData<BookList>> {
        return searchBooksWithPaginationUseCase.execute(
            sizePage = SIZE_PAGE,
            word = word,
            sort = sort,
            startRating = startRating,
            finishRating = finishRating,
            genres = genres,
            tags = tags
        ).cachedIn(viewModelScope)
    }

    fun getPopularGenres() {
        viewModelScope.launch {
            _mutableLivDataPopularGenres.value = BasicState.LoadingState()
            _mutableLivDataPopularGenres.value = getPopularGenresUseCase.execute()
        }
    }

    //Tags
    fun getPopularTags() {
        viewModelScope.launch {
            _mutableLiveDataPopularTags.value = BasicState.LoadingState()
            val state = getPopularTagsUseCase.execute()
            if (state is BasicState.SuccessState) {
                _mutableLiveDataPopularTags.value =
                    BasicState.SuccessState(state.data.map { mapTagToTagWithCheck(it) })
            } else {
                _mutableLiveDataPopularTags.value = BasicState.ErrorState()
            }
        }
    }

    fun setNewSelectedTagsFromTagsList(tags: List<TagWithTitle>) {
        val result = ArrayList<TagWithTitle>(emptyList())
        for (item in tags) {
            if (item is TagWithTitle.Tag) {
                if (item.tagWithCheck.isSelected && !result.contains(item)) {
                    result.add(item)
                }
            }
        }
        _mutableLiveDataSelectedTags.value =
            BasicState.SuccessState(result.map { mapTagWithTitleToTagWithCheck(it as TagWithTitle.Tag) })
    }

    fun setNewSelectedTagsFromMainFilter(tags: List<TagWithCheck>) {
        val result = ArrayList<TagWithCheck>(emptyList())
        for (item in tags) {
            if (item.isSelected && !result.contains(item)) {
                result.add(item)
            }
        }
        _mutableLiveDataSelectedTags.value = BasicState.SuccessState(result)
    }

    fun getTagsForMainFilter() {
        viewModelScope.launch {
            _mutableLiveDataTagsForMainFilter.value = BasicState.LoadingState()
            val popularTagsState = getPopularTagsUseCase.execute()
            if (popularTagsState is BasicState.SuccessState) {
                val selectedTagsState = _mutableLiveDataSelectedTags.value
                if (selectedTagsState is BasicState.SuccessState) {
                    val popularTags = popularTagsState.data.map { mapTagToTagWithCheck(it) }
                    val selectedTags = selectedTagsState.data
                    val resultTagsList: ArrayList<TagWithCheck> = ArrayList(emptyList())
                    for (selectedTag in selectedTags) {
                        var isExists = false
                        for (popularTag in popularTags) {
                            if (selectedTag.tag.id == popularTag.tag.id) {
                                isExists = true
                                popularTag.isSelected = true
                            }
                        }
                        if (!isExists) {
                            resultTagsList.add(selectedTag)
                        }
                    }
                    resultTagsList.addAll(popularTags)
                    _mutableLiveDataTagsForMainFilter.value =
                        BasicState.SuccessState(resultTagsList)
                } else {
                    _mutableLiveDataTagsForMainFilter.value =
                        BasicState.SuccessState(popularTagsState.data.map { mapTagToTagWithCheck(it) })
                }
            } else {
                _mutableLiveDataTagsForMainFilter.value = BasicState.ErrorState()
            }
        }
    }

    fun getAllTagsForFilter() {
        viewModelScope.launch {
            _mutableLiveDataAllTagsFilter.value = BasicState.LoadingState()
            val popularTagsState = getPopularTagsUseCase.execute()
            val allTagsState = getAllTagsUseCase.execute()
            if (popularTagsState is BasicState.SuccessState && allTagsState is BasicState.SuccessState) {
                val tagsWithTitle =
                    ArrayList(listOf<TagWithTitle>(TagWithTitle.Title("Популярные")))
                val selectedTagsState = _mutableLiveDataSelectedTags.value
                if (selectedTagsState is BasicState.SuccessState) {
                    val popularTags = popularTagsState.data.map { mapTagToTagWithTitle(it) }
                    val selectedTags = selectedTagsState.data
                    for (selectedTag in selectedTags) {
                        for (popularTag in popularTags) {
                            if (selectedTag.tag.id == popularTag.tagWithCheck.tag.id) {
                                popularTag.tagWithCheck.isSelected = true
                            }

                        }
                    }
                    tagsWithTitle.addAll(popularTags)
                } else {
                    tagsWithTitle.addAll(popularTagsState.data.map { mapTagToTagWithTitle(it) })
                }
                tagsWithTitle.add(TagWithTitle.Title("Все теги"))
                if (selectedTagsState is BasicState.SuccessState) {
                    val allTags = allTagsState.data.map { mapTagToTagWithTitle(it) }
                    val selectedTags = selectedTagsState.data
                    for (selectedTag in selectedTags) {
                        for (tag in allTags) {
                            if (selectedTag.tag.id == tag.tagWithCheck.tag.id) {
                                tag.tagWithCheck.isSelected = true
                            }
                        }
                    }
                    tagsWithTitle.addAll(allTags)
                } else {
                    tagsWithTitle.addAll(allTagsState.data.map { mapTagToTagWithTitle(it) })
                }
                _mutableLiveDataAllTagsFilter.value = BasicState.SuccessState(tagsWithTitle)
            } else {
                _mutableLiveDataAllTagsFilter.value = BasicState.ErrorState()
            }
        }
    }

    private fun mapTagToTagWithCheck(tag: Tag) = TagWithCheck(tag = tag, isSelected = false)


    private fun mapTagToTagWithTitle(tag: Tag) = TagWithTitle.Tag(tagWithCheck = TagWithCheck(tag))


    private fun mapTagWithTitleToTagWithCheck(tagWithTitle: TagWithTitle.Tag) = TagWithCheck(
        tag = tagWithTitle.tagWithCheck.tag,
        isSelected = tagWithTitle.tagWithCheck.isSelected
    )

    //Genres
    fun getPopularGenresForFilter() {
        viewModelScope.launch {
            _mutableLiveDataPopularGenresFilter.value = BasicState.LoadingState()
            val state = getPopularGenresUseCase.execute()
            if (state is BasicState.SuccessState) {
                _mutableLiveDataPopularGenresFilter.value =
                    BasicState.SuccessState(state.data.map { mapGenreToGenreWithCheck(it) })
            } else {
                _mutableLiveDataPopularGenresFilter.value = BasicState.ErrorState()
            }
        }
    }

    fun setNewSelectedGenresFromGenresList(tags: List<GenreWithTitle>) {
        val result = ArrayList<GenreWithTitle>(emptyList())
        for (item in tags) {
            if (item is GenreWithTitle.Genre) {
                if (item.genreWithCheck.isSelected && !result.contains(item)) {
                    result.add(item)
                }
            }
        }
        _mutableLiveDataSelectedGenres.value =
            BasicState.SuccessState(result.map { mapGenreWithTitleToGenreWithCheck(it as GenreWithTitle.Genre) })
    }

    fun setNewSelectedGenresFromMainFilter(genres: List<GenreWithCheck>) {
        val result = ArrayList<GenreWithCheck>(emptyList())
        for (item in genres) {
            if (item.isSelected && !result.contains(item)) {
                result.add(item)
            }
        }
        _mutableLiveDataSelectedGenres.value = BasicState.SuccessState(result)
    }

    fun getGenresForMainFilter() {
        viewModelScope.launch {
            _mutableLiveDataGenresForMainFilter.value = BasicState.LoadingState()
            val popularGenresState = getPopularGenresUseCase.execute()
            if (popularGenresState is BasicState.SuccessState) {
                val selectedGenresState = _mutableLiveDataSelectedGenres.value
                if (selectedGenresState is BasicState.SuccessState) {
                    val popularGenres = popularGenresState.data.map { mapGenreToGenreWithCheck(it) }
                    val selectedGenres = selectedGenresState.data
                    val resultGenresList: ArrayList<GenreWithCheck> = ArrayList(emptyList())
                    for (selectedGenre in selectedGenres) {
                        var isExists = false
                        for (popularGenre in popularGenres) {
                            if (selectedGenre.genre.id == popularGenre.genre.id) {
                                isExists = true
                                popularGenre.isSelected = true
                            }
                        }
                        if (!isExists) {
                            resultGenresList.add(selectedGenre)
                        }
                    }
                    resultGenresList.addAll(popularGenres)
                    _mutableLiveDataGenresForMainFilter.value =
                        BasicState.SuccessState(resultGenresList)
                } else {
                    _mutableLiveDataGenresForMainFilter.value =
                        BasicState.SuccessState(popularGenresState.data.map {
                            mapGenreToGenreWithCheck(
                                it
                            )
                        })
                }
            } else {
                _mutableLiveDataGenresForMainFilter.value = BasicState.ErrorState()
            }
        }
    }

    fun getAllGenresForFilter() {
        viewModelScope.launch {
            _mutableLiveDataAllGenresFilter.value = BasicState.LoadingState()
            val popularGenresState = getPopularGenresUseCase.execute()
            val allGenresState = getAllGenresUseCase.execute()
            if (popularGenresState is BasicState.SuccessState && allGenresState is BasicState.SuccessState) {
                val genresWithTitle =
                    ArrayList(listOf<GenreWithTitle>(GenreWithTitle.Title("Популярные")))
                val selectedGenresState = _mutableLiveDataSelectedGenres.value
                if (selectedGenresState is BasicState.SuccessState) {
                    val popularGenres = popularGenresState.data.map { mapGenreToGenreWithTitle(it) }
                    val selectedGenres = selectedGenresState.data
                    for (selectedGenre in selectedGenres) {
                        for (popularGenre in popularGenres) {
                            if (selectedGenre.genre.id == popularGenre.genreWithCheck.genre.id) {
                                popularGenre.genreWithCheck.isSelected = true
                            }

                        }
                    }
                    genresWithTitle.addAll(popularGenres)
                } else {
                    genresWithTitle.addAll(popularGenresState.data.map { mapGenreToGenreWithTitle(it) })
                }
                genresWithTitle.add(GenreWithTitle.Title("Все теги"))
                if (selectedGenresState is BasicState.SuccessState) {
                    val allGenres = allGenresState.data.map { mapGenreToGenreWithTitle(it) }
                    val selectedGenres = selectedGenresState.data
                    for (selectedGenre in selectedGenres) {
                        for (genre in allGenres) {
                            if (selectedGenre.genre.id == genre.genreWithCheck.genre.id) {
                                genre.genreWithCheck.isSelected = true
                            }
                        }
                    }
                    genresWithTitle.addAll(allGenres)
                } else {
                    genresWithTitle.addAll(allGenresState.data.map { mapGenreToGenreWithTitle(it) })
                }
                _mutableLiveDataAllGenresFilter.value = BasicState.SuccessState(genresWithTitle)
            } else {
                _mutableLiveDataAllGenresFilter.value = BasicState.ErrorState()
            }
        }
    }

    private fun mapGenreToGenreWithCheck(genre: Genre): GenreWithCheck =
        GenreWithCheck(genre = genre, isSelected = false)

    private fun mapGenreToGenreWithTitle(genre: Genre) =
        GenreWithTitle.Genre(genreWithCheck = GenreWithCheck(genre))

    private fun mapGenreWithTitleToGenreWithCheck(genreWithTitle: GenreWithTitle.Genre) =
        GenreWithCheck(
            genre = genreWithTitle.genreWithCheck.genre,
            isSelected = genreWithTitle.genreWithCheck.isSelected
        )

    companion object {
        const val SIZE_PAGE = 5
        const val SORT_POPULARITY = "popularity"
        const val SORT_RATING = "rating"
    }
}