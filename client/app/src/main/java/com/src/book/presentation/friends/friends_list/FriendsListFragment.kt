package com.src.book.presentation.friends.friends_list

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.src.book.R
import com.src.book.databinding.FragmentFriendsListBinding
import com.src.book.domain.model.Friend
import com.src.book.presentation.MainActivity
import com.src.book.presentation.friends.friends_list.adapter.FriendsAdapter
import com.src.book.presentation.friends.friends_list.viewModel.FriendsListViewModel
import com.src.book.presentation.friends.friends_requests.RequestsFriendsFragment
import com.src.book.utils.REGEX_SPACE
import java.util.*
import kotlin.collections.ArrayList

//TODO добавить количество заявок
class FriendsListFragment : Fragment() {
    private lateinit var binding: FragmentFriendsListBinding
    private lateinit var viewModel: FriendsListViewModel
    private lateinit var friendsList: ArrayList<Friend>
    private lateinit var fullFriendsList: ArrayList<Friend>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.binding = FragmentFriendsListBinding.inflate(inflater)
        viewModel = (activity as MainActivity).getFriendsListViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.liveDataFriends.observe(
            this.viewLifecycleOwner, this::checkState
        )
        viewModel.liveDataIsLoading.observe(
            this.viewLifecycleOwner, this::setView
        )
        viewModel.loadFriends()
        binding.ivAddFriends.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, RequestsFriendsFragment())
                .addToBackStack(null)
                .commit()
        }
        setTextChangeListenerForSearch()

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
                friendsList.clear()
                val newList: ArrayList<Friend> = ArrayList(friendsList.size)
                if (text.isNotEmpty()) {
                    fullFriendsList.forEach {
                        val nameLowerCase = it.name.lowercase(Locale.getDefault())
                        val loginLowerCase = it.login.lowercase(Locale.getDefault())
                        if (nameLowerCase.contains(text) || loginLowerCase.contains(text)) {
                            if (!newList.contains(it)) {
                                newList.add(it)
                            }
                        }
                    }
                    friendsList.addAll(newList)
                } else {
                    friendsList.addAll(fullFriendsList)
                }
                binding.rvFriends.adapter?.notifyDataSetChanged()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    //TODO обработка ошибки загрузки друзей
    private fun checkState(state: FriendsListState) {
        when (state) {
            is FriendsListState.SuccessState -> loadData(state.friend)
            is FriendsListState.ErrorState -> {}
            else -> {}
        }
    }

    private fun setView(isLoading: Boolean) {
        if (isLoading) {
            binding.rvFriends.visibility = View.GONE
            binding.slFriends.startShimmer()
        } else {
            binding.rvFriends.visibility = View.VISIBLE
            binding.slFriends.stopShimmer()
            binding.slFriends.visibility = View.GONE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadData(friends: List<Friend>) {
        setAdapterForFriendsRecyclerView(friends)
        binding.tvTitle.text = "${getString(R.string.friends_with_holder)} (${friends.size})"
    }

    private fun setAdapterForFriendsRecyclerView(friends: List<Friend>) {
        friendsList = ArrayList(friends)
        fullFriendsList = ArrayList(friends)
        val adapter = FriendsAdapter()
        adapter.submitList(friendsList)
        val layoutManager = GridLayoutManager(requireContext(), 1, RecyclerView.VERTICAL, false)
        binding.rvFriends.layoutManager = layoutManager
        binding.rvFriends.adapter = adapter
    }
}