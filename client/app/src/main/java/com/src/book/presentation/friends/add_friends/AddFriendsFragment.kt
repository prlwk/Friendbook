package com.src.book.presentation.friends.add_friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.src.book.databinding.FragmentAddFriendsBinding
import com.src.book.databinding.FragmentLoadingBinding
import com.src.book.domain.utils.SendFriendRequestState
import com.src.book.presentation.MainActivity
import com.src.book.presentation.friends.add_friends.viewModel.AddFriendsViewModel

class AddFriendsFragment : Fragment() {
    private lateinit var binding: FragmentAddFriendsBinding
    private lateinit var viewModel: AddFriendsViewModel
    private var isClick = false
    private lateinit var bindingLoading: FragmentLoadingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.binding = FragmentAddFriendsBinding.inflate(inflater)
        bindingLoading = binding.loading
        viewModel = (activity as MainActivity).getAddFriendsViewModel()
        return this.binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.liveDataIsLoading.observe(
            this.viewLifecycleOwner, this::checkLoading
        )
        viewModel.liveDataState.observe(
            this.viewLifecycleOwner, this::checkState
        )
        setOcClickListenerForSendRequestButton()
        setOnClickListenerForBackButton()
    }

    private fun setOcClickListenerForSendRequestButton() {
        binding.btSendRequest.setOnClickListener {
            isClick = true
            val login = binding.etEnterUsername.text.toString()
            viewModel.sendFriendRequest(login.substring(1))

        }
    }
    private fun setOnClickListenerForBackButton(){
        binding.ivBackButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun checkLoading(isLoading: Boolean) {
        if (isLoading) {
            bindingLoading.clLoadingPage.visibility = View.VISIBLE
        } else {
            bindingLoading.clLoadingPage.visibility = View.GONE
        }
    }

    //TODO в зависимости от ситуации обработать ошибки
    private fun checkState(sendFriendRequestState: SendFriendRequestState) {
        if (isClick) {
            when (sendFriendRequestState) {
                is SendFriendRequestState.SuccessState -> {
                    println("success")
                }
                is SendFriendRequestState.ErrorState -> {
                    println("error")
                }
                is SendFriendRequestState.ErrorLoginState -> {
                    println("такого логина не существует")
                }
                is SendFriendRequestState.FriendAlreadyExists -> {
                    println("друг уже в друзьях")
                }
                is SendFriendRequestState.SuchRequestAlreadyExists -> {
                    println("запрос уже отправлен")
                }
            }
            isClick = false
        }
    }
}