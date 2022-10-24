package com.src.book.presentation.registration.first_registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.src.book.R
import com.src.book.databinding.FragmentLoadingBinding
import com.src.book.databinding.FragmentRegistrationUserInformationBinding
import com.src.book.databinding.FragmentSignInBinding
import com.src.book.domain.utils.LoginState
import com.src.book.presentation.registration.LoginActivity
import com.src.book.presentation.registration.sign_in.viewModel.SignInViewModel

class RegistrationUserInfoFragment : Fragment() {
    private lateinit var binding: FragmentRegistrationUserInformationBinding
    private lateinit var bindingLoading: FragmentLoadingBinding
    private lateinit var viewModel: SignInViewModel
    private var isClickNext = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.binding = FragmentRegistrationUserInformationBinding.inflate(inflater)
        viewModel = (activity as LoginActivity).getSignInViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.tvButtonNext.setOnClickListener {
            binding.tilNickname.error = "Введите корректный никнейм"
            binding.tilNickname.errorIconDrawable = null
            binding.tilEnterName.error = "Введите корректное имя"
            binding.tilEnterName.errorIconDrawable = null
        }
    }

}