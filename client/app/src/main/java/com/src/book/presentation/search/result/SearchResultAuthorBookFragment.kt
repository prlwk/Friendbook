package com.src.book.presentation.search.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.src.book.databinding.FragmentSearchResultAuthorBookBinding
import com.src.book.domain.model.author.AuthorList
import com.src.book.domain.model.book.BookList
import com.src.book.presentation.MainActivity
import com.src.book.presentation.main.main_page.viewModel.MainPageViewModel
import com.src.book.presentation.search.result.adapter.BookPagingAdapter
import com.src.book.presentation.search.result.adapter.DefaultLoadStateAdapter
import com.src.book.presentation.search.result.adapter.SimpleAuthorPagingAdapter
import com.src.book.presentation.utils.simpleScan
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SearchResultAuthorBookFragment : Fragment() {
    private lateinit var binding: FragmentSearchResultAuthorBookBinding
    private lateinit var viewModel: MainPageViewModel
    private lateinit var viewHolderAuthor: DefaultLoadStateAdapter.Holder
    private lateinit var viewHolderBook: DefaultLoadStateAdapter.Holder
    private lateinit var title: String
    private var bookIsNotEmpty = false
    private var authorIsNotEmpty = false
    private var bookIsLoaded: Boolean = false
    private var authorIsLoaded: Boolean = false
    private var booksIsError = false
    private var authorsIsError = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchResultAuthorBookBinding.inflate(inflater)
        viewModel = (activity as MainActivity).getMainPageViewModel()
        val args = this.arguments
        title = args?.getString(TITLE).toString()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.slSearch.visibility = View.VISIBLE
        binding.slSearch.startShimmer()
        binding.tvTitle.text = title
        setAuthorAdapterForRecyclerView()
        setBookAdapterForRecyclerView()
        setOnClickListenerForBackButton()
        setVisibilityForBooks(View.GONE)
        setVisibilityForAuthors(View.GONE)
        setVisibilityForBooksError(false)
        setVisibilityForAuthorsError(false)
    }

    //AUTHOR
    private fun setAuthorAdapterForRecyclerView() {
        val adapter = SimpleAuthorPagingAdapter { onClickAuthor(it) }
        val loadStateAdapter = DefaultLoadStateAdapter { clickTryAgainAuthor() }
        val adapterWithLoadState = adapter.withLoadStateFooter(loadStateAdapter)
        adapter.addLoadStateListener { loadState ->
            if (loadState.source.refresh is LoadState.NotLoading) {
                authorIsNotEmpty = if (loadState.append.endOfPaginationReached) {
                    adapter.itemCount >= 1
                } else {
                    true
                }
                authorIsLoaded = true
            }
            if (loadState.source.refresh is LoadState.Error) {
                authorsIsError = true
                authorIsNotEmpty = false
                authorIsLoaded = true
            }
            checkAuthorBookState()
        }
        val layoutManager = GridLayoutManager(requireContext(), 1, RecyclerView.HORIZONTAL, false)
        binding.rvAuthors.layoutManager = layoutManager
        binding.rvAuthors.adapter = adapterWithLoadState
        viewHolderAuthor = DefaultLoadStateAdapter.Holder(
            binding.layoutLoadView
        ) { clickTryAgainAuthor() }
        observeAuthors(adapter)
        observeLoadStateAuthorList(adapter)
        handleAuthorListScrollingToTopWhenSearching(adapter)
        handleAuthorListVisibility(adapter)
    }

    //TODO поторить запрос
    private fun clickTryAgainAuthor() {

    }

    //TODO переход на страницу автора
    private fun onClickAuthor(author: AuthorList) {

    }

    private fun observeAuthors(
        adapter: SimpleAuthorPagingAdapter
    ) {
        lifecycleScope.launch {
            viewModel.getAuthorResult(
                word = title,
                sort = null,
                startRating = null,
                finishRating = null
            )
                .collectLatest { pagingData ->
                    adapter.submitData(pagingData)
                }
        }

    }

    private fun observeLoadStateAuthorList(adapter: SimpleAuthorPagingAdapter) {
        lifecycleScope.launch {
            adapter.loadStateFlow.debounce(500).collectLatest { state ->
                viewHolderAuthor.bind(state.refresh)

            }
        }
    }

    private fun handleAuthorListScrollingToTopWhenSearching(adapter: SimpleAuthorPagingAdapter) =
        lifecycleScope.launch {
            getRefreshLoadStateFlowAuthorList(adapter)
                .simpleScan(count = 2)
                .collectLatest { (previousState, currentState) ->
                    if (previousState is LoadState.Loading && currentState is LoadState.NotLoading) {
                        binding.rvItems.scrollToPosition(0)
                    }
                }
        }

    private fun handleAuthorListVisibility(adapter: SimpleAuthorPagingAdapter) =
        lifecycleScope.launch {
            getRefreshLoadStateFlowAuthorList(adapter)
                .simpleScan(count = 3)
                .collectLatest { (beforePrevious, previous, current) ->
                    binding.rvItems.isInvisible = current is LoadState.Error
                            || previous is LoadState.Error
                            || (beforePrevious is LoadState.Error && previous is LoadState.NotLoading
                            && current is LoadState.Loading)
                }
        }

    private fun getRefreshLoadStateFlowAuthorList(adapter: SimpleAuthorPagingAdapter): Flow<LoadState> {
        return adapter.loadStateFlow
            .map { it.refresh }
    }

    private fun setVisibilityForAuthors(visibility: Int) {
        binding.rvAuthors.visibility = visibility
        binding.tvAuthorsTitle.visibility = visibility
    }

    private fun setVisibilityForAuthorsError(visibility: Boolean) {
        binding.layoutAuthorError.tvError.isVisible = visibility
        binding.layoutAuthorError.tvUpdate.isVisible = visibility
    }

    private fun showAuthorError() {
        setVisibilityForAuthorsError(true)
        setVisibilityForAuthors(View.GONE)
    }

    //BOOK
    private fun setBookAdapterForRecyclerView() {
        val adapter = BookPagingAdapter(onClickBook = { onClickBook(it) },
            onClickMore = { onCLickMore(it) },
            onCLickBookmark = { onClickBookmark(it) })
        val loadStateAdapter = DefaultLoadStateAdapter { clickTryAgainAuthor() }
        val adapterWithLoadState = adapter.withLoadStateFooter(loadStateAdapter)
        adapter.addLoadStateListener { loadState ->
            if (loadState.source.refresh is LoadState.NotLoading) {
                bookIsNotEmpty = if (loadState.append.endOfPaginationReached) {
                    adapter.itemCount >= 1
                } else {
                    true
                }
                bookIsLoaded = true
            }
            if (loadState.source.refresh is LoadState.Error) {
                booksIsError = true
                bookIsNotEmpty = false
                bookIsLoaded = true
            }
            checkAuthorBookState()
        }
        val layoutManager = GridLayoutManager(requireContext(), 1, RecyclerView.VERTICAL, false)
        binding.rvItems.layoutManager = layoutManager
        binding.rvItems.adapter = adapterWithLoadState
        viewHolderBook = DefaultLoadStateAdapter.Holder(
            binding.layoutLoadView
        ) { clickTryAgainBook() }
        //TODO передать
        observeBooks(adapter)
        observeLoadStateBookList(adapter)
        handleBookListScrollingToTopWhenSearching(adapter)
        handleBookListVisibility(adapter)
    }

    //TODO
    private fun clickTryAgainBook() {

    }

    //TODO прописать логику
    private fun onCLickMore(book: BookList) {

    }

    private fun onClickBookmark(book: BookList) {

    }

    private fun onClickBook(book: BookList) {

    }

    private fun observeBooks(
        adapter: BookPagingAdapter

    ) {
        lifecycleScope.launch {
            viewModel.getBookResult(
                word = title, sort = null,
                startRating = null,
                finishRating = null,
                genres = null,
                tags = null
            )
                .collectLatest { pagingData ->
                    adapter.submitData(pagingData)
                }
        }

    }

    private fun observeLoadStateBookList(adapter: BookPagingAdapter) {
        lifecycleScope.launch {
            adapter.loadStateFlow.debounce(500).collectLatest { state ->
                viewHolderBook.bind(state.refresh)

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

    private fun setVisibilityForBooks(visibility: Int) {
        binding.rvItems.visibility = visibility
        binding.tvBooksTitle.visibility = visibility
    }

    private fun setOnClickListenerForBackButton() {
        binding.ivBack.setOnClickListener {
            (activity as MainActivity).popBackStack()
        }
    }

    private fun setVisibilityForBooksError(visibility: Boolean) {
        binding.layoutBookError.tvError.isVisible = visibility
        binding.layoutBookError.tvUpdate.isVisible = visibility
    }

    private fun showBookError() {
        setVisibilityForBooksError(true)
        setVisibilityForBooks(View.GONE)
    }

    private fun checkAuthorBookState() {
        if (authorIsLoaded && bookIsLoaded) {
            binding.slSearch.stopShimmer()
            binding.slSearch.visibility = View.GONE
            if (!authorIsNotEmpty && !bookIsNotEmpty) {
                binding.layoutEmpty.clMain.visibility = View.VISIBLE
                setVisibilityForAuthors(View.GONE)
                setVisibilityForBooks(View.GONE)
            } else {
                binding.layoutEmpty.clMain.visibility = View.GONE
                if (authorIsNotEmpty) {
                    setVisibilityForAuthors(View.VISIBLE)
                }
                if (bookIsNotEmpty) {
                    setVisibilityForBooks(View.VISIBLE)
                }
            }
            if (authorsIsError) {
                setVisibilityForAuthorsError(true)
            }
            if (booksIsError) {
                setVisibilityForBooksError(true)
            }
        }
    }

    companion object {
        const val TITLE = "title"
    }
}