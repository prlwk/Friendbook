package com.src.book.presentation.main.main_page

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.src.book.R
import com.src.book.databinding.FragmentMainPageBinding
import com.src.book.domain.model.author.AuthorList
import com.src.book.domain.model.Genre
import com.src.book.domain.model.book.BookAuthor
import com.src.book.domain.model.book.BookList
import com.src.book.domain.model.book.BookMapper
import com.src.book.domain.model.search.SearchItem
import com.src.book.domain.utils.BasicState
import com.src.book.presentation.MainActivity
import com.src.book.presentation.book.main_page.BookFragment
import com.src.book.presentation.main.main_page.adapter.AuthorListAdapter
import com.src.book.presentation.main.main_page.adapter.BookListAdapter
import com.src.book.presentation.main.main_page.adapter.GenreListAdapter
import com.src.book.presentation.main.main_page.adapter.SearchItemAdapter
import com.src.book.presentation.main.main_page.adapter.itemDecoration.CategoryItemDecoration
import com.src.book.presentation.main.main_page.filter.FilterFragment
import com.src.book.presentation.main.main_page.viewModel.MainPageViewModel
import com.src.book.presentation.search.result.SearchResultAuthorBookFragment
import com.src.book.presentation.search.result.SearchResultWithTitleFragment
import com.src.book.utils.REGEX_SPACE
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class MainPageFragment : Fragment() {
    private lateinit var binding: FragmentMainPageBinding
    private lateinit var viewModel: MainPageViewModel
    private val searchItemsList: ArrayList<SearchItem> = ArrayList(emptyList())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainPageBinding.inflate(inflater)
        viewModel = (activity as MainActivity).getMainPageViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.liveDataBookPopularity.observe(
            this.viewLifecycleOwner,
            this::checkPopularBooksState
        )
        viewModel.liveDataTheBestAuthors.observe(
            this.viewLifecycleOwner,
            this::checkBestAuthorsState
        )
        viewModel.liveDataPopularGenres.observe(
            this.viewLifecycleOwner,
            this::checkGenresState
        )
        setAdapters()
        setOnClickListeners()
        setListeners()
        viewModel.getPopularBooks()
        viewModel.getBestAuthors()
        viewModel.getPopularGenres()
        binding.llPopularBookError.visibility = View.GONE
        binding.llBestAuthorError.visibility = View.GONE
        setVisibilityForSearchItem(View.GONE)
    }

    private fun setAdapters() {
        setAdapterForPopularBooksRecyclerView()
        setAdapterForBestAuthorsRecyclerView()
        setAdapterForGenresRecyclerView()
        setAdapterForSearchItem()
    }

    private fun setOnClickListeners() {
        setOnClickListenerForUpdatePopularBooksButton()
        setOnClickListenerForUpdateBestAuthorsButton()

        setOnClickListenerForMoreAuthors()
        setOnClickListenerForMoreBooks()

        setOnClickListenerForFilter()
    }

    private fun setListeners() {
        binding.etSearch.setOnClickListener {
            if (binding.mlSearchBar.currentState != R.id.with_cancel_button) {
                binding.mlSearchBar.transitionToState(R.id.with_cancel_button)
                binding.etSearch.isCursorVisible = true
                setVisibilityForSearchItem(View.VISIBLE)
            }
        }

        binding.etSearch.setOnFocusChangeListener { _, _ ->
            if (binding.mlSearchBar.currentState != R.id.with_cancel_button) {
                binding.mlSearchBar.transitionToState(R.id.with_cancel_button)
                binding.etSearch.isCursorVisible = true
                setVisibilityForSearchItem(View.VISIBLE)
            }
        }
        binding.cancelButton.setOnClickListener {
            hideKeyboard()
            if (binding.mlSearchBar.currentState != R.id.no_cancel_button) {
                setTransactionToStateNoCancelButton()
                setVisibilityForSearchItem(View.GONE)
            }
        }
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(binding.etSearch.hasFocus()) {
                    val text = binding.etSearch.text.toString()
                        .replace(REGEX_SPACE, " ")
                        .lowercase(Locale.getDefault())
                        .trim()
                    if (text.isEmpty()) {
                        binding.rvSearchItem.visibility = View.VISIBLE
                    } else {
                        binding.rvSearchItem.visibility = View.GONE
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        //TODO перейти на результат поиска
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                setVisibilityForSearchItem(View.GONE)
                val name = binding.etSearch.text.toString()
                    .replace(REGEX_SPACE, " ")
                    .lowercase(Locale.getDefault())
                    .trim()
                binding.etSearch.clearFocus()
                binding.etSearch.setText("")
                if (binding.mlSearchBar.currentState != R.id.no_cancel_button) {
                    setTransactionToStateNoCancelButton()
                }
                if (name.isNotEmpty()) {
                    viewModel.addSearchItem(name)
                    navigateToSearchResultAuthorBookFragment(name)
                }
                return@setOnEditorActionListener false
            } else {
                return@setOnEditorActionListener true
            }
        }
    }

    private fun navigateToSearchResultAuthorBookFragment(name: String) {
        binding.rvSearchItem.visibility = View.GONE
        val bundle = Bundle()
        bundle.putString(SearchResultAuthorBookFragment.TITLE, name)
        val fragment = SearchResultAuthorBookFragment()
        fragment.arguments = bundle
        (activity as MainActivity).replaceFragment(fragment)
    }

    private fun setTransactionToStateNoCancelButton() {
        binding.mlSearchBar.transitionToState(R.id.no_cancel_button)
        binding.etSearch.isCursorVisible = false
        binding.etSearch.setText("")
    }

    private fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    private fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    //Popular books
    private fun checkPopularBooksState(state: BasicState<List<BookList>>) {
        when (state) {
            is BasicState.SuccessState -> {
                setDataForPopularBooks(state.data)
            }
            is BasicState.LoadingState -> {
                setVisibilityForShimmerLayout(View.VISIBLE, binding.shimmerPopularBook)
                binding.llPopularBookError.visibility = View.GONE
                binding.rvPopularBooks.visibility = View.GONE
            }
            is BasicState.ErrorState -> {
                showErrorPopularBooks()
            }
            else -> {}
        }
    }

    private fun setDataForPopularBooks(books: List<BookList>) {
        val adapter = binding.rvPopularBooks.adapter as BookListAdapter
        adapter.submitList(books.map { BookMapper().mapBookListToBookAuthor(it) })
        binding.llPopularBookError.visibility = View.GONE
        binding.rvPopularBooks.visibility = View.VISIBLE
        setVisibilityForShimmerLayout(View.GONE, binding.shimmerPopularBook)
    }

    private fun showErrorPopularBooks() {
        binding.shimmerPopularBook.stopShimmer()
        binding.rvPopularBooks.visibility = View.GONE
        binding.llPopularBookError.visibility = View.VISIBLE
        setVisibilityForShimmerLayout(View.GONE, binding.shimmerPopularBook)
    }

    private fun setAdapterForPopularBooksRecyclerView() {
        val adapter = BookListAdapter { item -> onClickBook(item) }
        val layoutManager = GridLayoutManager(requireContext(), 1, RecyclerView.HORIZONTAL, false)
        binding.rvPopularBooks.layoutManager = layoutManager
        binding.rvPopularBooks.adapter = adapter
    }

    private fun setOnClickListenerForUpdatePopularBooksButton() {
        binding.layoutPopularBookError.tvUpdate.setOnClickListener {
            viewModel.getPopularBooks()
        }
    }

    private fun onClickBook(book: BookAuthor) {
        val bundle = Bundle()
        bundle.putLong(BookFragment.BOOK_ID, book.id)
        val fragment = BookFragment()
        fragment.arguments = bundle
        (activity as MainActivity).replaceFragment(fragment)
    }

    //The best Authors
    private fun checkBestAuthorsState(state: BasicState<List<AuthorList>>) {
        when (state) {
            is BasicState.SuccessState -> {
                setDataForBestAuthors(authors = state.data)
            }
            is BasicState.LoadingState -> {
                setVisibilityForShimmerLayout(View.VISIBLE, binding.shimmerBestAuthors)
                binding.llBestAuthorError.visibility = View.GONE
                binding.rvBestAuthors.visibility = View.GONE
            }
            is BasicState.ErrorState -> {
                showErrorBestAuthors()
            }
            else -> {}
        }
    }

    private fun setAdapterForBestAuthorsRecyclerView() {
        val adapter = AuthorListAdapter { item -> onClickAuthor(item) }
        val layoutManager = GridLayoutManager(requireContext(), 1, RecyclerView.HORIZONTAL, false)
        binding.rvBestAuthors.layoutManager = layoutManager
        binding.rvBestAuthors.adapter = adapter
    }

    private fun setDataForBestAuthors(authors: List<AuthorList>) {
        val adapter = binding.rvBestAuthors.adapter as AuthorListAdapter
        adapter.submitList(authors)
        binding.llBestAuthorError.visibility = View.GONE
        binding.rvBestAuthors.visibility = View.VISIBLE
        setVisibilityForShimmerLayout(View.GONE, binding.shimmerBestAuthors)
    }

    private fun showErrorBestAuthors() {
        binding.shimmerBestAuthors.stopShimmer()
        binding.rvBestAuthors.visibility = View.GONE
        binding.llBestAuthorError.visibility = View.VISIBLE
        setVisibilityForShimmerLayout(View.GONE, binding.shimmerBestAuthors)
    }

    private fun setOnClickListenerForUpdateBestAuthorsButton() {
        binding.layoutBestAuthorError.tvUpdate.setOnClickListener {
            viewModel.getBestAuthors()
        }
    }

    //TODO переход на страницу автора
    private fun onClickAuthor(author: AuthorList) {

    }

    private fun setVisibilityForShimmerLayout(visibility: Int, shimmerLayout: ShimmerFrameLayout) {
        shimmerLayout.visibility = visibility
        if (visibility == View.VISIBLE) {
            shimmerLayout.startShimmer()
        } else {
            shimmerLayout.stopShimmer()
        }
    }

    private fun setOnClickListenerForMoreAuthors() {
        binding.tvMoreAuthors.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(
                SearchResultWithTitleFragment.SHOW_PARAMETER,
                SearchResultWithTitleFragment.SHOW_ONLY_AUTHOR
            )
            bundle.putString(
                SearchResultWithTitleFragment.TITLE,
                getString(R.string.best_authors)
            )
            val fragment = SearchResultWithTitleFragment()
            fragment.arguments = bundle
            (activity as MainActivity).replaceFragment(fragment)
        }
    }

    private fun setOnClickListenerForMoreBooks() {
        binding.tvMoreBooks.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(
                SearchResultWithTitleFragment.SHOW_PARAMETER,
                SearchResultWithTitleFragment.SHOW_ONLY_BOOKS
            )
            bundle.putString(
                SearchResultWithTitleFragment.TITLE,
                getString(R.string.popular)
            )
            val fragment = SearchResultWithTitleFragment()
            fragment.arguments = bundle
            (activity as MainActivity).replaceFragment(fragment)
        }
    }

    private fun checkGenresState(state: BasicState<List<Genre>>) {
        when (state) {
            is BasicState.SuccessState -> {
                setDataForGenres(state.data)
            }
            is BasicState.LoadingState -> {
                setVisibilityForShimmerLayout(View.VISIBLE, binding.shimmerCategory)
                binding.llCategoryError.visibility = View.GONE
                binding.rvCategory.visibility = View.GONE
            }
            is BasicState.ErrorState -> {
                showErrorGenres()
            }
            else -> {}
        }
    }

    //Genres
    private fun setAdapterForGenresRecyclerView() {
        val adapter = GenreListAdapter { onClickGenre(it) }
        val layoutManager = GridLayoutManager(requireContext(), 2, RecyclerView.HORIZONTAL, false)
        binding.rvCategory.layoutManager = layoutManager
        binding.rvCategory.adapter = adapter
    }

    private fun showErrorGenres() {
        binding.shimmerCategory.stopShimmer()
        binding.rvCategory.visibility = View.GONE
        binding.llCategoryError.visibility = View.VISIBLE
        setVisibilityForShimmerLayout(View.GONE, binding.shimmerCategory)
    }

    private fun setDataForGenres(genres: List<Genre>) {
        val adapter = binding.rvCategory.adapter as GenreListAdapter
        adapter.submitList(genres)
        binding.rvCategory.addItemDecoration(CategoryItemDecoration())
        binding.llCategoryError.visibility = View.GONE
        binding.rvCategory.visibility = View.VISIBLE
        setVisibilityForShimmerLayout(View.GONE, binding.shimmerCategory)
    }

    //TODO перейти на страницу с жанрами
    private fun onClickGenre(genre: Genre) {

    }

    private fun setAdapterForSearchItem() {
        val adapter = SearchItemAdapter(onClickDelete = { id, poistion ->
            onCLickSearchItemDelete(
                id = id,
                adapterPosition = poistion
            )
        },
            onClickSearchItem = { onCLickSearchItem(it) })
        val layoutManager = GridLayoutManager(requireContext(), 1, RecyclerView.VERTICAL, false)
        binding.rvSearchItem.layoutManager = layoutManager
        binding.rvSearchItem.adapter = adapter
        observeSearchItems(adapter)
    }

    private fun onCLickSearchItemDelete(id: Long, adapterPosition: Int) {
        viewModel.deleteSearchItem(id)
        val adapter = binding.rvSearchItem.adapter as SearchItemAdapter
        searchItemsList.removeAt(adapterPosition)
        adapter.notifyItemRemoved(adapterPosition)
        adapter.notifyItemRangeChanged(adapterPosition, searchItemsList.size)
    }

    private fun onCLickSearchItem(name: String) {
        navigateToSearchResultAuthorBookFragment(name)
    }

    //Search items
    private fun observeSearchItems(adapter: SearchItemAdapter) {
        lifecycleScope.launch {

            viewModel.getAllSearchItems().collectLatest {
                searchItemsList.addAll(it)
                adapter.submitList(searchItemsList)
            }
        }
    }

    private fun setVisibilityForSearchItem(visibility: Int) {
        binding.rvSearchItem.visibility = visibility
        if (visibility == View.VISIBLE) {
            binding.svMainContent.visibility = View.GONE
        } else {
            binding.svMainContent.visibility = View.VISIBLE
        }

    }

    private fun setOnClickListenerForFilter() {
        binding.ivFilterButton.setOnClickListener {
            (activity as MainActivity).replaceFragment(FilterFragment())
        }
    }
}