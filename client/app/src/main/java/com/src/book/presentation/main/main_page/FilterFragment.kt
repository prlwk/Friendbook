package com.src.book.presentation.main.main_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.src.book.R
import com.src.book.databinding.FragmentFiltersBinding

class FilterFragment : Fragment() {

    private lateinit var binding : FragmentFiltersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_filters, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFiltersBinding.bind(view)

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
}