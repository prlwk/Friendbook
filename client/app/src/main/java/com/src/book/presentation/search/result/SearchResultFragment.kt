package com.src.book.presentation.search.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.src.book.databinding.FragmentSearchResultBinding
import com.src.book.presentation.MainActivity
import com.src.book.presentation.main.main_page.viewModel.MainPageViewModel
import com.src.book.presentation.search.result.adapter.DefaultLoadStateAdapter

class SearchResultFragment : Fragment() {
    private lateinit var binding: FragmentSearchResultBinding
    private lateinit var viewModel: MainPageViewModel
    private lateinit var viewHolder: DefaultLoadStateAdapter.Holder
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchResultBinding.inflate(inflater)
        viewModel = (activity as MainActivity).getMainPageViewModel()
        return binding.root
    }
}