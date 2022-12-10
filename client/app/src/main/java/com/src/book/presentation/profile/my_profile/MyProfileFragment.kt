package com.src.book.presentation.profile.my_profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.src.book.R
import com.src.book.databinding.FragmentMyProfileBinding
import com.src.book.domain.model.BookList
import com.src.book.domain.model.user.UserProfile
import com.src.book.domain.model.userReview.UserReview
import com.src.book.domain.utils.BasicState
import com.src.book.presentation.MainActivity
import com.src.book.presentation.author.list_of_books.adapter.ListOfBooksAdapter
import com.src.book.presentation.book.main_page.BookFragment
import com.src.book.presentation.profile.my_profile.viewModel.MyProfileViewModel
import com.src.book.presentation.profile.settings.SettingsFragment
import com.src.book.utils.BOOK_ID

class MyProfileFragment : Fragment() {

    private lateinit var binding: FragmentMyProfileBinding
    private lateinit var viewModel: MyProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyProfileBinding.inflate(inflater)
        viewModel = (activity as MainActivity).getMyProfileViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.liveDataProfileState.observe(
            this.viewLifecycleOwner,
            this::checkProfileState
        )
        viewModel.liveDataIsLoading.observe(
            this.viewLifecycleOwner,
            this::checkIsLoading
        )
        viewModel.setProfile()
        with(binding) {
            llReviews.setOnClickListener(ProfileSectionOnClickListener())
            llBooksRead.setOnClickListener(ProfileSectionOnClickListener())
            llWantRead.setOnClickListener(ProfileSectionOnClickListener())
        }
        setOnClickListenerForEditProfileButton()
        setOnClickListenerForSettingsButton()
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

    private fun checkProfileState(state: BasicState) {
        when (state) {
            is BasicState.SuccessStateWithResources<*> -> {
                loadData(state.data as UserProfile)
            }
            //TODO обработка ошибки загрузки
            is BasicState.ErrorState -> {
                Log.d("Profile", "error")
            }
            else -> {}
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadData(userProfile: UserProfile) {
        Glide.with(requireContext())
            .load(userProfile.image)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(binding.ivUserPhoto)
        binding.tvUserName.text = userProfile.name
        binding.userNickname.text = "@" + userProfile.login
        binding.tvFriendsNumber.text = "${userProfile.countFriends} "
        if (userProfile.savingBooks != null && userProfile.savingBooks.isNotEmpty()) {
            binding.tvWantReadNumber.text = userProfile.savingBooks.size.toString()
            setAdapterForBooksWantReadRecyclerView(userProfile.savingBooks)
        } else {
            //TODO список пуст
            binding.tvWantReadNumber.text = "0"
        }
        if (userProfile.ratedBooks != null && userProfile.ratedBooks.isNotEmpty()) {
            binding.tvBooksReadNumber.text = userProfile.ratedBooks.size.toString()
            setAdapterForBooksReadRecyclerView(userProfile.ratedBooks)
        } else {
            binding.tvBooksReadNumber.text = "0"
            //TODO список пуст
        }
        if (userProfile.reviews != null && userProfile.reviews.isNotEmpty()) {
            binding.tvReviewsNumber.text = userProfile.reviews.size.toString()
            setAdapterForReviewsRecyclerView(userProfile.reviews)
        } else {
            binding.tvReviewsNumber.text = "0"
            //TODO cписок пуст
        }

    }

    private fun checkIsLoading(isLoading: Boolean) {
        if (isLoading) {
            setVisibility(GONE)
            binding.rvReviews.visibility = GONE
            binding.rvWantRead.visibility = GONE
            binding.rvBooksRead.visibility = GONE
            binding.slProfile.startShimmer()
        } else {
            setVisibility(VISIBLE)
            binding.rvReviews.visibility = VISIBLE
            binding.slProfile.stopShimmer()
            binding.slProfile.visibility = GONE
        }
    }

    private fun setVisibility(visibility: Int) {
        binding.clProfileBackground.visibility = visibility
        binding.llReviews.visibility = visibility
        binding.llBooksRead.visibility = visibility
        binding.llWantRead.visibility = visibility
        binding.ivSettings.visibility = visibility
    }

    private fun setAdapterForBooksReadRecyclerView(books: List<BookList>) {
        val listOfBooksAdapter =
            ListOfBooksAdapter(
                onClickBook = { item -> onClickBook(item) })
        listOfBooksAdapter.submitList(books)
        val layoutManager = GridLayoutManager(requireContext(), 1, RecyclerView.VERTICAL, false)
        binding.rvBooksRead.layoutManager = layoutManager
        binding.rvBooksRead.adapter = listOfBooksAdapter
    }

    private fun setAdapterForBooksWantReadRecyclerView(books: List<BookList>) {
        val listOfBooksAdapter =
            ListOfBooksAdapter(
                onClickBook = { item -> onClickBook(item) })
        listOfBooksAdapter.submitList(books)
        val layoutManager = GridLayoutManager(requireContext(), 1, RecyclerView.VERTICAL, false)
        binding.rvWantRead.layoutManager = layoutManager
        binding.rvWantRead.adapter = listOfBooksAdapter
    }

    private fun setAdapterForReviewsRecyclerView(reviews: List<UserReview>) {
        val adapter = UserReviewAdapter()
        adapter.submitList(reviews)
        val layoutManager = GridLayoutManager(requireContext(), 1, RecyclerView.VERTICAL, false)
        binding.rvReviews.layoutManager = layoutManager
        binding.rvReviews.adapter = adapter
    }

    private fun onClickBook(book: BookList) {
        val bundle = Bundle()
        bundle.putLong(BOOK_ID, book.id)
        val fragment = BookFragment()
        fragment.arguments = bundle
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setOnClickListenerForEditProfileButton() {
        binding.tvEditProfileButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, EditMyProfileFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setOnClickListenerForSettingsButton() {
        binding.ivSettings.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SettingsFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}
