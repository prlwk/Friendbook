package com.src.book.presentation.book.review

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import com.src.book.R
import com.src.book.databinding.FragmentReviewListBinding

//TODO: при большом общем количестве отзывов весь текст числа отзывов не влазит
//TODO: надо сделать чтоб пятизначные цифры из, например, "11563" превращались в "11к"

class ReviewListFragment : Fragment() {

    private lateinit var binding: FragmentReviewListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_review_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentReviewListBinding.bind(view);
    }
}