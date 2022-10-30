package com.src.book.presentation.friends.friends_requests

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.src.book.R
import com.src.book.databinding.FragmentRequestsFriendsBinding
import com.src.book.domain.model.friend.FriendRequest.FriendRequest
import com.src.book.domain.utils.BasicState
import com.src.book.presentation.MainActivity
import com.src.book.presentation.friends.add_friends.AddFriendsFragment
import com.src.book.presentation.friends.friends_requests.viewModel.FriendRequestsState
import com.src.book.presentation.friends.friends_requests.viewModel.RequestsFriendsViewModel
import com.src.book.presentation.friends.friends_requests.viewModel.adapter.IncomingRequestsAdapter
import com.src.book.presentation.friends.friends_requests.viewModel.adapter.OutgoingRequestsAdapter

class RequestsFriendsFragment : Fragment() {
    private lateinit var binding: FragmentRequestsFriendsBinding
    private lateinit var viewModel: RequestsFriendsViewModel
    private lateinit var incomingRequests: ArrayList<FriendRequest>
    private lateinit var outgoingRequest: ArrayList<FriendRequest>
    private var countIncomingRequests = 0
    private var countOutgoingRequests = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRequestsFriendsBinding.inflate(inflater)
        viewModel = (activity as MainActivity).getRequestsFriendsViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.liveDataIncomingRequests.observe(
            this.viewLifecycleOwner, this::checkStateForIncomingRequest
        )
        viewModel.liveDataIncomingIsLoading.observe(
            this.viewLifecycleOwner, this::setView
        )
        viewModel.liveDataSubmitFriendState.observe(
            this.viewLifecycleOwner, this::checkStateForSubmitFriendRequest
        )
        viewModel.liveDataIncomingRejectFriendState.observe(
            this.viewLifecycleOwner, this::checkStateForIncomingRejectFriendRequest
        )
        viewModel.liveDataOutgoingRequests.observe(
            this.viewLifecycleOwner, this::checkStateForOutgoingRequest
        )
        viewModel.liveDataOutgoingRejectFriendState.observe(
            this.viewLifecycleOwner, this::checkStateForOutgoingRejectFriendRequest
        )

        //TODO поменять бэкграунды по нажатию
        binding.incomingButton.setOnClickListener {
            binding.rvOutgoingFriends.visibility = View.GONE
            binding.rvIncomingFriends.visibility = View.VISIBLE
            viewModel.loadIncomingRequests()
        }
        binding.sentButton.setOnClickListener {
            binding.rvIncomingFriends.visibility = View.GONE
            binding.rvOutgoingFriends.visibility = View.VISIBLE
            viewModel.loadOutgoingRequests()
        }
        viewModel.loadIncomingRequests()
        viewModel.loadOutgoingRequests()
        setOnClickListenerForAddFriend()
        setTextForIncomingButton(0)
        setTextForSentButton(0)
    }

    private fun setOnClickListenerForAddFriend() {
        binding.btAddFriend.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AddFriendsFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    //TODO eсли произошла ошибка когда принимали заявку в друзья
    private fun checkStateForSubmitFriendRequest(state: BasicState) {
        if (state is BasicState.ErrorState) {

        }
    }

    //TODO eсли произошла ошибка когда отклонили заявку в друзья
    private fun checkStateForIncomingRejectFriendRequest(state: BasicState) {
        if (state is BasicState.ErrorState) {

        }
    }

    //TODO eсли произошла ошибка когда отклонили заявку в друзья
    private fun checkStateForOutgoingRejectFriendRequest(state: BasicState) {
        if (state is BasicState.ErrorState) {

        }
    }

    private fun checkStateForIncomingRequest(state: FriendRequestsState) {
        when (state) {
            is FriendRequestsState.DefaultState -> {
                if (state.friendsRequest.isNotEmpty()) {
                    loadIncomingRequest(state.friendsRequest)
                } else {
                    //TODO нет заявок
                }
            }
            else -> {
                //TODO обработка ошибки входящих заявок
            }
        }
    }

    private fun checkStateForOutgoingRequest(state: FriendRequestsState) {
        when (state) {
            is FriendRequestsState.DefaultState -> {
                if (state.friendsRequest.isNotEmpty()) {
                    loadOutgoingRequest(state.friendsRequest)
                } else {
                    //TODO нет заявок
                }
            }
            else -> {
                //TODO обработка ошибки исходящих заявок
            }
        }
    }

    private fun loadIncomingRequest(requests: List<FriendRequest>) {
        setIncomingAdapterForRecyclerView(requests)
        setTextForIncomingButton(requests.size)
        countIncomingRequests = requests.size
    }

    private fun loadOutgoingRequest(requests: List<FriendRequest>) {
        setOutgoingAdapterForRecyclerView(requests)
        setTextForSentButton(requests.size)
        countOutgoingRequests = requests.size
    }

    @SuppressLint("SetTextI18n")
    private fun setTextForIncomingButton(size: Int) {
        binding.incomingButton.text = "Входящие ($size)"
    }

    @SuppressLint("SetTextI18n")
    private fun setTextForSentButton(size: Int) {
        binding.sentButton.text = "Исходящие ($size)"
    }

    private fun setIncomingAdapterForRecyclerView(requests: List<FriendRequest>) {
        incomingRequests = ArrayList(requests)
        val adapter =
            IncomingRequestsAdapter({ item, position -> onClickIncomingAddFriend(item, position) },
                { item, position -> onClickIncomingRejectFriend(item, position) })
        adapter.submitList(incomingRequests)
        val layoutManager = GridLayoutManager(requireContext(), 1, RecyclerView.VERTICAL, false)
        binding.rvIncomingFriends.adapter = adapter
        binding.rvIncomingFriends.layoutManager = layoutManager
    }

    private fun setOutgoingAdapterForRecyclerView(requests: List<FriendRequest>) {
        outgoingRequest = ArrayList(requests)
        val adapter =
            OutgoingRequestsAdapter { item, position ->
                onClickOutgoingRejectFriend(
                    item,
                    position
                )
            }
        adapter.submitList(outgoingRequest)
        val layoutManager = GridLayoutManager(requireContext(), 1, RecyclerView.VERTICAL, false)
        binding.rvOutgoingFriends.adapter = adapter
        binding.rvOutgoingFriends.layoutManager = layoutManager
    }

    private fun setView(isLoading: Boolean) {
        if (isLoading) {
            binding.slRequests.visibility = View.VISIBLE
        } else {
            binding.slRequests.visibility = View.GONE
        }
    }

    private fun onClickIncomingAddFriend(friend: FriendRequest, position: Int) {
        viewModel.submitFriendRequest(friend.id)
        incomingRequests.removeAt(position)
        binding.rvIncomingFriends.adapter?.notifyItemRemoved(position)
        countIncomingRequests--;
        setTextForIncomingButton(countIncomingRequests)
    }

    private fun onClickIncomingRejectFriend(friend: FriendRequest, position: Int) {
        viewModel.rejectIncomingFriendRequest(friend.id)
        incomingRequests.removeAt(position)
        binding.rvIncomingFriends.adapter?.notifyItemRemoved(position)
        countIncomingRequests--
        setTextForIncomingButton(countIncomingRequests)
    }

    private fun onClickOutgoingRejectFriend(friend: FriendRequest, position: Int) {
        viewModel.rejectOutgoingFriendRequest(friend.id)
        outgoingRequest.removeAt(position)
        binding.rvOutgoingFriends.adapter?.notifyItemRemoved(position)
        countOutgoingRequests--
        setTextForSentButton(countOutgoingRequests)
    }
}