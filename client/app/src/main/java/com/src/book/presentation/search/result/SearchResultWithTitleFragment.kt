package com.src.book.presentation.search.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.src.book.databinding.FragmentSearchResultWithTitleBinding
import com.src.book.domain.author.AuthorList
import com.src.book.domain.model.book.BookList
import com.src.book.presentation.MainActivity
import com.src.book.presentation.main.main_page.viewModel.MainPageViewModel
import com.src.book.presentation.search.result.adapter.AuthorPagingAdapter
import com.src.book.presentation.search.result.adapter.BookPagingAdapter
import com.src.book.presentation.search.result.adapter.DefaultLoadStateAdapter
import com.src.book.presentation.utils.simpleScan
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

//TODO продумать анимацию загрущки
//TODO если список пустой то менять фрагменты
class SearchResultWithTitleFragment : Fragment() {
    private lateinit var binding: FragmentSearchResultWithTitleBinding
    private lateinit var viewModel: MainPageViewModel
    private lateinit var viewHolder: DefaultLoadStateAdapter.Holder
    private lateinit var showParameter: String
    private lateinit var title: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchResultWithTitleBinding.inflate(inflater)
        viewModel = (activity as MainActivity).getMainPageViewModel()
        val args = this.arguments
        title = args?.getString(TITLE).toString()
        showParameter = args?.getString(SHOW_PARAMETER).toString()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListenerForBackButton()
        binding.tvTitle.text = title
        if (showParameter == SHOW_ONLY_AUTHOR) {
            setAuthorAdapterForRecyclerView(
                word = null,
                sort = MainPageViewModel.SORT_RATING,
                startRating = null,
                finishRating = null
            )
        } else if (showParameter == SHOW_ONLY_BOOKS) {
            setBookAdapterForRecyclerView(
                word = null,
                sort = MainPageViewModel.SORT_POPULARITY,
                startRating = null,
                finishRating = null,
                tags = null,
                genres = null
            )
        }
    }

    //AUTHOR
    private fun setAuthorAdapterForRecyclerView(
        word: String?,
        sort: String?,
        startRating: Int?,
        finishRating: Int?
    ) {
        val adapter = AuthorPagingAdapter { onClickAuthor(it) }
        val loadStateAdapter = DefaultLoadStateAdapter { clickTryAgain() }
        val adapterWithLoadState = adapter.withLoadStateFooter(loadStateAdapter)
        val layoutManager = GridLayoutManager(requireContext(), 1, RecyclerView.VERTICAL, false)
        binding.rvItems.layoutManager = layoutManager
        binding.rvItems.adapter = adapterWithLoadState
        viewHolder = DefaultLoadStateAdapter.Holder(
            binding.layoutLoadView
        ) { clickTryAgain() }
        //TODO передать
        observeAuthors(adapter, word, sort, startRating, finishRating)
        observeLoadStateAuthorList(adapter)
        handleAuthorListScrollingToTopWhenSearching(adapter)
        handleAuthorListVisibility(adapter)
    }

    //TODO поторить запрос
    private fun clickTryAgain() {

    }

    //TODO переход на страницу автора
    private fun onClickAuthor(author: AuthorList) {

    }

    //TODO show "ничего не найдено"
    private fun observeAuthors(
        adapter: AuthorPagingAdapter,
        word: String?,
        sort: String?,
        startRating: Int?,
        finishRating: Int?
    ) {
        lifecycleScope.launch {
            viewModel.getAuthorResult(word, sort, startRating, finishRating)
                .collectLatest { pagingData ->
                    adapter.submitData(pagingData)
                }
        }

    }

    private fun observeLoadStateAuthorList(adapter: AuthorPagingAdapter) {
        lifecycleScope.launch {
            adapter.loadStateFlow.debounce(500).collectLatest { state ->
                viewHolder.bind(state.refresh)

            }
        }
    }

    private fun handleAuthorListScrollingToTopWhenSearching(adapter: AuthorPagingAdapter) =
        lifecycleScope.launch {
            getRefreshLoadStateFlowAuthorList(adapter)
                .simpleScan(count = 2)
                .collectLatest { (previousState, currentState) ->
                    if (previousState is LoadState.Loading && currentState is LoadState.NotLoading) {
                        binding.rvItems.scrollToPosition(0)
                    }
                }
        }

    private fun handleAuthorListVisibility(adapter: AuthorPagingAdapter) = lifecycleScope.launch {
        getRefreshLoadStateFlowAuthorList(adapter)
            .simpleScan(count = 3)
            .collectLatest { (beforePrevious, previous, current) ->
                binding.rvItems.isInvisible = current is LoadState.Error
                        || previous is LoadState.Error
                        || (beforePrevious is LoadState.Error && previous is LoadState.NotLoading
                        && current is LoadState.Loading)
            }
    }

    private fun getRefreshLoadStateFlowAuthorList(adapter: AuthorPagingAdapter): Flow<LoadState> {
        return adapter.loadStateFlow
            .map { it.refresh }
    }


    //BOOK
    private fun setBookAdapterForRecyclerView(
        word: String?,
        sort: String?,
        startRating: Int?,
        finishRating: Int?,
        tags: String?,
        genres: String?
    ) {
        val adapter = BookPagingAdapter(onClickBook = { onClickBook(it) },
            onClickMore = { onCLickMore(it) },
            onCLickBookmark = { onClickBookmark(it) })
        val loadStateAdapter = DefaultLoadStateAdapter { clickTryAgain() }
        val adapterWithLoadState = adapter.withLoadStateFooter(loadStateAdapter)
        val layoutManager = GridLayoutManager(requireContext(), 1, RecyclerView.VERTICAL, false)
        binding.rvItems.layoutManager = layoutManager
        binding.rvItems.adapter = adapterWithLoadState
        viewHolder = DefaultLoadStateAdapter.Holder(
            binding.layoutLoadView
        ) { clickTryAgain() }
        //TODO передать
        observeBooks(adapter, word, sort, startRating, finishRating, tags, genres)
        observeLoadStateBookList(adapter)
        handleBookListScrollingToTopWhenSearching(adapter)
        handleBookListVisibility(adapter)
    }

    //TODO прописать логику
    private fun onCLickMore(book: BookList) {

    }

    private fun onClickBookmark(book: BookList) {

    }

    private fun onClickBook(book: BookList) {

    }

    private fun observeBooks(
        adapter: BookPagingAdapter,
        word: String?,
        sort: String?,
        startRating: Int?,
        finishRating: Int?,
        tags: String?,
        genres: String?

    ) {
        lifecycleScope.launch {
            viewModel.getBookResult(word, sort, startRating, finishRating, tags, genres)
                .collectLatest { pagingData ->
                    adapter.submitData(pagingData)
                }
        }

    }

    private fun observeLoadStateBookList(adapter: BookPagingAdapter) {
        lifecycleScope.launch {
            adapter.loadStateFlow.debounce(500).collectLatest { state ->
                viewHolder.bind(state.refresh)

            }
        }
    }

    private fun handleBookListScrollingToTopWhenSearching(adapter: BookPagingAdapter) =
        lifecycleScope.launch {
            getRefreshLoadStateFlowBookList(adapter)
                .simpleScan(count = 2)
                .collectLatest { (previousState, currentState) ->
                    if (previousState is LoadState.Loading && currentState is LoadState.NotLoading) {
                        binding.rvItems.scrollToPosition(0)
                    }
                }
        }

    private fun handleBookListVisibility(adapter: BookPagingAdapter) = lifecycleScope.launch {
        getRefreshLoadStateFlowBookList(adapter)
            .simpleScan(count = 3)
            .collectLatest { (beforePrevious, previous, current) ->
                binding.rvItems.isInvisible = current is LoadState.Error
                        || previous is LoadState.Error
                        || (beforePrevious is LoadState.Error && previous is LoadState.NotLoading
                        && current is LoadState.Loading)
            }
    }

    private fun getRefreshLoadStateFlowBookList(adapter: BookPagingAdapter): Flow<LoadState> {
        return adapter.loadStateFlow
            .map { it.refresh }
    }

    private fun setOnClickListenerForBackButton() {
        binding.ivBack.setOnClickListener {
            (activity as MainActivity).popBackStack()
        }
    }

    companion object {
        const val SHOW_PARAMETER = "show_parameter"
        const val SHOW_ONLY_AUTHOR = "author"
        const val SHOW_ONLY_BOOKS = "book"
        const val TITLE = "title"
    }
}