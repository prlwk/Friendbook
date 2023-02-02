package com.src.book.presentation.main.main_page.filter

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.src.book.R
import com.src.book.databinding.FragmentFiltersBinding
import com.src.book.domain.model.book.genre.GenreWithCheck
import com.src.book.domain.model.book.tag.TagWithCheck
import com.src.book.domain.utils.BasicState
import com.src.book.presentation.MainActivity
import com.src.book.presentation.main.main_page.adapter.GenreFilterAdapter
import com.src.book.presentation.main.main_page.adapter.TagFilterAdapter
import com.src.book.presentation.main.main_page.viewModel.MainPageViewModel

class FilterFragment : Fragment() {
    private lateinit var binding: FragmentFiltersBinding
    private lateinit var viewModel: MainPageViewModel

    private var popularTags: List<TagWithCheck> = emptyList()
    private var allTags: ArrayList<TagWithCheck> = ArrayList(listOf())
    private var popularGenres: List<GenreWithCheck> = emptyList()
    private var allGenres: ArrayList<GenreWithCheck> = ArrayList(listOf())
    private var isLoadingTags: Boolean = false
    private var isLoadingGenres: Boolean = false
    private var isLoadingPopularTags: Boolean = false
    private var isLoadingPopularGenres: Boolean = false
    private var isErrorTags: Boolean = false
    private var isErrorGenres: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFiltersBinding.inflate(inflater)
        viewModel = (activity as MainActivity).getMainPageViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBackgroundForHideRead()
        setAdapters()
        setOnClickListeners()
        setObservers()
        viewModel.getPopularTags()
        viewModel.getTagsForMainFilter()
        viewModel.getPopularGenresForFilter()
        viewModel.getGenresForMainFilter()
    }

    private fun setObservers() {
        viewModel.liveDataTagsForMainFilter.observe(
            this.viewLifecycleOwner,
            this::checkAllTagsState
        )
        viewModel.liveDataPopularTags.observe(
            this.viewLifecycleOwner,
            this::checkPopularTagsState
        )
        viewModel.liveDataGenresForMainFilter.observe(
            this.viewLifecycleOwner,
            this::checkAllGenresState
        )
        viewModel.liveDataPopularGenresForFilter.observe(
            this.viewLifecycleOwner,
            this::checkPopularGenresState
        )
    }

    private fun setAdapters() {
        setAdapterForTagsRecyclerView()
        setAdapterForGenresRecyclerView()
    }

    private fun setOnClickListeners() {
        setOnClickListenerForMoreTags()
        setOnClickListenerForMoreGenres()
        setOnCLickListenerForBackButton()
    }

    //TODO
    private fun checkAllTagsState(state: BasicState<List<TagWithCheck>>) {
        when (state) {
            is BasicState.SuccessState -> {
                setTags(state.data)
                isLoadingTags = true
                if (isLoadingGenres && isLoadingPopularTags && isLoadingPopularGenres) {
                    setVisibilityForShimmer(View.GONE)
                }
            }
            is BasicState.LoadingState -> {
                setVisibilityForShimmer(View.VISIBLE)
            }
            is BasicState.ErrorState -> {
                isErrorTags = true
                isLoadingTags = true
                // setVisibilityForShimmer(View.GONE)
                setVisibilityForTag(View.GONE)
                (activity as MainActivity).showSnackBar()
            }
            else -> {}
        }
    }

    //TODO
    private fun checkPopularTagsState(state: BasicState<List<TagWithCheck>>) {
        when (state) {
            is BasicState.SuccessState -> {
                popularTags = state.data
                if (isLoadingGenres && isLoadingTags && isLoadingPopularGenres) {
                    setVisibilityForShimmer(View.GONE)
                }
            }
            is BasicState.LoadingState -> {
                isLoadingPopularTags = true
                setVisibilityForShimmer(View.VISIBLE)
            }
            is BasicState.ErrorState -> {
                isErrorTags = true
                isLoadingPopularTags = true
                (activity as MainActivity).showSnackBar()
            }
            else -> {}
        }
    }

    private fun setAdapterForTagsRecyclerView() {
        val adapter = TagFilterAdapter { item -> onClickTag(item) }
        val layoutManager = GridLayoutManager(requireContext(), 1, RecyclerView.HORIZONTAL, false)
        binding.rvTag.layoutManager = layoutManager
        binding.rvTag.adapter = adapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun onClickTag(tagWithCheck: TagWithCheck) {
        if (tagWithCheck.isSelected) {
            tagWithCheck.isSelected = false
            if (!popularTags.contains(tagWithCheck)) {
                allTags.remove(tagWithCheck)
                val adapter = binding.rvTag.adapter as TagFilterAdapter
                adapter.notifyDataSetChanged()
            }
        } else {
            tagWithCheck.isSelected = true
            allTags.add(0, tagWithCheck)
        }
    }

    private fun setTags(tags: List<TagWithCheck>) {
        allTags = ArrayList(tags)
        val adapter = binding.rvTag.adapter as TagFilterAdapter
        adapter.submitList(allTags)
    }

    private fun setOnClickListenerForMoreTags() {
        binding.tvMoreTags.setOnClickListener {
            viewModel.setNewSelectedTagsFromMainFilter(allTags)
            (activity as MainActivity).replaceFragment(AllTagsFilterFragment())
        }
    }

    //TODO
    private fun checkAllGenresState(state: BasicState<List<GenreWithCheck>>) {
        when (state) {
            is BasicState.SuccessState -> {
                setGenres(state.data)
                if (isLoadingPopularTags && isLoadingTags && isLoadingPopularGenres) {
                    setVisibilityForShimmer(View.GONE)
                }
            }
            is BasicState.LoadingState -> {
                isLoadingGenres = true
                setVisibilityForShimmer(View.VISIBLE)
            }
            is BasicState.ErrorState -> {
                isErrorGenres = true
                isLoadingGenres = true
                setVisibilityForGenre(View.GONE)
                (activity as MainActivity).showSnackBar()
            }
            else -> {}
        }
    }

    //TODO
    private fun checkPopularGenresState(state: BasicState<List<GenreWithCheck>>) {
        when (state) {
            is BasicState.SuccessState -> {
                popularGenres = state.data
                if (isLoadingPopularTags && isLoadingTags && isLoadingPopularGenres) {
                    setVisibilityForShimmer(View.GONE)
                }
            }
            is BasicState.LoadingState -> {
                isLoadingPopularGenres = true
                setVisibilityForShimmer(View.VISIBLE)
            }
            is BasicState.ErrorState -> {
                isLoadingPopularGenres = true
                isErrorGenres = true
                setVisibilityForGenre(View.GONE)
                (activity as MainActivity).showSnackBar()
            }
            else -> {}
        }
    }

    private fun setAdapterForGenresRecyclerView() {
        val adapter = GenreFilterAdapter { item -> onClickGenre(item) }
        val layoutManager = GridLayoutManager(requireContext(), 1, RecyclerView.HORIZONTAL, false)
        binding.rvGenre.layoutManager = layoutManager
        binding.rvGenre.adapter = adapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun onClickGenre(genreWithCheck: GenreWithCheck) {
        if (genreWithCheck.isSelected) {
            genreWithCheck.isSelected = false
            if (!popularGenres.contains(genreWithCheck)) {
                allGenres.remove(genreWithCheck)
                val adapter = binding.rvGenre.adapter as GenreFilterAdapter
                adapter.notifyDataSetChanged()
            }
        } else {
            genreWithCheck.isSelected = true
            allGenres.add(0, genreWithCheck)
        }
    }

    private fun setGenres(tags: List<GenreWithCheck>) {
        allGenres = ArrayList(tags)
        val adapter = binding.rvGenre.adapter as GenreFilterAdapter
        adapter.submitList(allGenres)
    }

    private fun setBackgroundForHideRead() {
        binding.scHideRead.setOnCheckedChangeListener { v, checked ->
            run {
                if (checked) {
                    v.setBackgroundResource(R.drawable.switch_background_active)
                } else {
                    v.setBackgroundResource(R.drawable.switch_background_inactive)
                }
            }
        }
    }

    private fun setOnClickListenerForMoreGenres() {
        binding.tvMoreGenres.setOnClickListener {
            viewModel.setNewSelectedGenresFromMainFilter(allGenres)
            (activity as MainActivity).replaceFragment(AllGenresFilterFragment())
        }
    }

    private fun setOnCLickListenerForBackButton() {
        binding.ivBackButton.setOnClickListener {
            (activity as MainActivity).popBackStack()
        }
    }

    private fun setVisibilityForContent(visibility: Int) {
        binding.tvSortTitle.visibility = visibility
        binding.tvSortPopular.visibility = visibility
        binding.tvSortRating.visibility = visibility
        binding.tvRatingTitle.visibility = visibility
        binding.sliderRating.visibility = visibility
        binding.tvHideRead.visibility = visibility
        binding.scHideRead.visibility = visibility
        binding.tvSearch.visibility = visibility
        if (isErrorTags) {
            setVisibilityForTag(View.GONE)
        } else {
            setVisibilityForTag(visibility)
        }
        if (isErrorGenres) {
            setVisibilityForGenre(View.GONE)
        } else {
            setVisibilityForGenre(visibility)
        }
    }

    private fun setVisibilityForTag(visibility: Int) {
        binding.tvTagTitle.visibility = visibility
        binding.rvTag.visibility = visibility
        binding.tvMoreTags.visibility = visibility
    }

    private fun setVisibilityForGenre(visibility: Int) {
        binding.tvGenreTitle.visibility = visibility
        binding.rvGenre.visibility = visibility
        binding.tvMoreGenres.visibility = visibility
    }


    private fun setVisibilityForShimmer(visibility: Int) {
        binding.slFilter.visibility = visibility
        if (visibility == View.VISIBLE) {
            setVisibilityForContent(View.GONE)
            binding.slFilter.startShimmer()
        } else {
            setVisibilityForContent(View.VISIBLE)
            binding.slFilter.stopShimmer()
        }
    }
}