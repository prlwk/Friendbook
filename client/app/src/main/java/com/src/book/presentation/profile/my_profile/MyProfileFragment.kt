package com.src.book.presentation.profile.my_profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import com.src.book.R
import com.src.book.databinding.FragmentMyProfileBinding

class MyProfileFragment : Fragment() {

    private lateinit var binding: FragmentMyProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMyProfileBinding.bind(view)

        with(binding) {
            llReviews.setOnClickListener(ProfileSectionOnClickListener())
            llBooksRead.setOnClickListener(ProfileSectionOnClickListener())
            llWantRead.setOnClickListener(ProfileSectionOnClickListener())
        }
    }

    inner class ProfileSectionOnClickListener : OnClickListener {
        override fun onClick(v: View?) {
            when (v) {
                binding.llReviews -> {
                    binding.rvReviews.visibility = VISIBLE
                    binding.rvBooksRead.visibility = INVISIBLE
                    binding.rvWantRead.visibility = INVISIBLE
                    binding.llReviews.setBackgroundResource(R.drawable.books_btn_click_background)
                    binding.llBooksRead.setBackgroundResource(R.drawable.profile_books_btn_on_click)
                    binding.llWantRead.setBackgroundResource(R.drawable.profile_books_btn_on_click)
                }
                binding.llBooksRead -> {
                    binding.rvReviews.visibility = INVISIBLE
                    binding.rvBooksRead.visibility = VISIBLE
                    binding.rvWantRead.visibility = INVISIBLE
                    binding.llReviews.setBackgroundResource(R.drawable.profile_books_btn_on_click)
                    binding.llBooksRead.setBackgroundResource(R.drawable.books_btn_click_background)
                    binding.llWantRead.setBackgroundResource(R.drawable.profile_books_btn_on_click)
                }
                binding.llWantRead -> {
                    binding.rvReviews.visibility = INVISIBLE
                    binding.rvBooksRead.visibility = INVISIBLE
                    binding.rvWantRead.visibility = VISIBLE
                    binding.llReviews.setBackgroundResource(R.drawable.profile_books_btn_on_click)
                    binding.llBooksRead.setBackgroundResource(R.drawable.profile_books_btn_on_click)
                    binding.llWantRead.setBackgroundResource(R.drawable.books_btn_click_background)
                }
            }
        }

    }
}