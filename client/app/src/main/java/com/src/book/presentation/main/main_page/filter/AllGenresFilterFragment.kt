package com.src.book.presentation.main.main_page.filter

import android.annotation.SuppressLint
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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.src.book.R
import com.src.book.databinding.FragmentAllCategoryFilterBinding
import com.src.book.domain.model.book.genre.GenreWithTitle
import com.src.book.domain.utils.BasicState
import com.src.book.presentation.MainActivity
import com.src.book.presentation.main.main_page.adapter.AllGenreFilterAdapter
import com.src.book.presentation.main.main_page.adapter.TagGenreFilterItemDecoration
import com.src.book.presentation.main.main_page.viewModel.MainPageViewModel
import com.src.book.utils.REGEX_SPACE
import java.util.*
import kotlin.collections.ArrayList

class AllGenresFilterFragment : Fragment() {
    private lateinit var binding: FragmentAllCategoryFilterBinding
    private lateinit var viewModel: MainPageViewModel
    private var allItemInRecyclerView: ArrayList<GenreWithTitle> = ArrayList(listOf())
    private val itemDecoration = TagGenreFilterItemDecoration()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllCategoryFilterBinding.inflate(inflater)
        viewModel = (activity as MainActivity).getMainPageViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setAdapters()
        viewModel.liveDataAllGenresFilter.observe(
            this.viewLifecycleOwner,
            this::checkGenresState
        )
        viewModel.getAllGenresForFilter()
        binding.tvTitle.tag = getString(R.string.genres)
    }

    private fun setListeners() {
        binding.etSearch.setOnClickListener {
            if (binding.mlSearchBar.currentState != R.id.with_cancel_button) {
                binding.mlSearchBar.transitionToState(R.id.with_cancel_button)
                binding.etSearch.isCursorVisible = true
            }
        }

        binding.etSearch.setOnFocusChangeListener { _, _ ->
            if (binding.mlSearchBar.currentState != R.id.with_cancel_button) {
                binding.mlSearchBar.transitionToState(R.id.with_cancel_button)
                binding.etSearch.isCursorVisible = true
            }
        }
        binding.cancelButton.setOnClickListener {
            hideKeyboard()
            if (binding.mlSearchBar.currentState != R.id.no_cancel_button) {
                setTransactionToStateNoCancelButton()
            }
        }
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.etSearch.clearFocus()
                binding.etSearch.setText("")
                if (binding.mlSearchBar.currentState != R.id.no_cancel_button) {
                    setTransactionToStateNoCancelButton()
                }
                return@setOnEditorActionListener false
            } else {
                return@setOnEditorActionListener true
            }
        }
        setTextChangeListenerForSearch()
        setOnClickListeners()
    }

    private fun setTransactionToStateNoCancelButton() {
        binding.mlSearchBar.transitionToState(R.id.no_cancel_button)
        binding.etSearch.isCursorVisible = false
        binding.etSearch.setText("")
        setDefaultItemsForRecyclerView()
    }

    private fun setAdapters() {
        setAdapterForGenresRecyclerView()
    }

    private fun setOnClickListeners() {
        setOnClickListenerForSaveButton()
        setOnClickListenerForResetButton()
        setOnClickListenerForBackButton()
        setOnClickListenerForRepeatButton()
    }

    private fun checkGenresState(state: BasicState<List<GenreWithTitle>>) {
        when (state) {
            is BasicState.SuccessState -> {
                setDataForGenres(state.data)
            }
            is BasicState.ErrorState -> {
                showError()
            }
            is BasicState.LoadingState -> {
                setVisibilityForShimmerLayout(View.VISIBLE, binding.slCategory)
                binding.llError.visibility = View.GONE
                binding.rvCategory.visibility = View.GONE
                binding.tvSave.visibility = View.GONE
            }
            else -> {}
        }
    }

    private fun setDataForGenres(genres: List<GenreWithTitle>) {
        binding.llError.visibility = View.GONE
        binding.rvCategory.visibility = View.VISIBLE
        binding.tvSave.visibility = View.VISIBLE
        setVisibilityForShimmerLayout(View.GONE, binding.slCategory)
        val adapter = binding.rvCategory.adapter as AllGenreFilterAdapter
        allItemInRecyclerView = ArrayList(genres)
        adapter.submitList(allItemInRecyclerView)
    }

    private fun setAdapterForGenresRecyclerView() {
        val adapter = AllGenreFilterAdapter { item -> onClickGenre(item) }
        val layoutManager = GridLayoutManager(requireContext(), 1, RecyclerView.VERTICAL, false)
        binding.rvCategory.layoutManager = layoutManager
        binding.rvCategory.adapter = adapter
        binding.rvCategory.addItemDecoration(itemDecoration)
    }

    private fun onClickGenre(genreWithCheck: GenreWithTitle.Genre) {
        for (item in allItemInRecyclerView) {
            if (item is GenreWithTitle.Genre) {
                if (item.genreWithCheck.genre.id == genreWithCheck.genreWithCheck.genre.id) {
                    item.genreWithCheck.isSelected = !item.genreWithCheck.isSelected
                }
            }
        }
    }

    private fun setOnClickListenerForSaveButton() {
        binding.tvSave.setOnClickListener {
            viewModel.setNewSelectedGenresFromGenresList(allItemInRecyclerView)
            (activity as MainActivity).replaceFragment(FilterFragment())
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setOnClickListenerForResetButton() {
        binding.tvReset.setOnClickListener {
            val adapter = binding.rvCategory.adapter as AllGenreFilterAdapter
            var i = 0
            while (i < allItemInRecyclerView.size) {
                if (allItemInRecyclerView[i] is GenreWithTitle.Genre) {
                    (allItemInRecyclerView[i] as GenreWithTitle.Genre).genreWithCheck.isSelected =
                        false
                    adapter.notifyItemChanged(i)
                }
                i++
            }
        }
    }

    private fun setOnClickListenerForBackButton() {
        binding.ivBackButton.setOnClickListener {
            (activity as MainActivity).popBackStack()
        }
    }

    private fun showError() {
        binding.slCategory.stopShimmer()
        binding.rvCategory.visibility = View.GONE
        binding.tvSave.visibility = View.GONE
        binding.llError.visibility = View.VISIBLE
        setVisibilityForShimmerLayout(View.GONE, binding.slCategory)
    }

    private fun setVisibilityForShimmerLayout(visibility: Int, shimmerLayout: ShimmerFrameLayout) {
        shimmerLayout.visibility = visibility
        if (visibility == View.VISIBLE) {
            shimmerLayout.startShimmer()
        } else {
            shimmerLayout.stopShimmer()
        }
    }

    private fun setOnClickListenerForRepeatButton() {
        binding.layoutError.tvUpdate.setOnClickListener {
            viewModel.getAllGenresForFilter()
        }
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

    @SuppressLint("NotifyDataSetChanged")
    private fun setDefaultItemsForRecyclerView() {
        val adapter = binding.rvCategory.adapter as AllGenreFilterAdapter
        adapter.submitList(allItemInRecyclerView)
    }

    private fun setTextChangeListenerForSearch() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val text = binding.etSearch.text.toString()
                    .replace(REGEX_SPACE, " ")
                    .lowercase(Locale.getDefault())
                    .trim()
                val newList: ArrayList<GenreWithTitle> = ArrayList(allItemInRecyclerView.size)
                if (text.isNotEmpty()) {
                    allItemInRecyclerView.forEach {
                        if (it is GenreWithTitle.Genre) {
                            val tagName =
                                it.genreWithCheck.genre.name.lowercase(Locale.getDefault())
                            if (tagName.contains(text)) {
                                if (!newList.contains(it)) {
                                    newList.add(it)
                                }
                            }
                        }
                    }
                    clearRecyclerView()
                    (binding.rvCategory.adapter as AllGenreFilterAdapter).submitList(newList)
                } else {
                    clearRecyclerView()
                    (binding.rvCategory.adapter as AllGenreFilterAdapter).submitList(
                        allItemInRecyclerView
                    )
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun clearRecyclerView() {
        binding.rvCategory.removeItemDecoration(itemDecoration)
        binding.rvCategory.adapter = null
        setAdapterForGenresRecyclerView()
    }
}