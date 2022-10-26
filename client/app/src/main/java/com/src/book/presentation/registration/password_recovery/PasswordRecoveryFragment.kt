package com.src.book.presentation.registration.password_recovery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.src.book.R
import com.src.book.databinding.FragmentLoadingBinding
import com.src.book.databinding.FragmentPassswordRecoveryBinding
import com.src.book.domain.utils.ChangePasswordState
import com.src.book.domain.utils.CodeState
import com.src.book.presentation.registration.LoginActivity
import com.src.book.presentation.registration.password_recovery.viewModel.passwordRecovery.PasswordRecoveryViewModel

class PasswordRecoveryFragment : Fragment() {
    private lateinit var binding: FragmentPassswordRecoveryBinding
    private lateinit var bindingLoading: FragmentLoadingBinding
    private lateinit var viewModel: PasswordRecoveryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPassswordRecoveryBinding.inflate(inflater)
        bindingLoading = binding.loading
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = (activity as LoginActivity).getPasswordRecoveryViewModel()
        viewModel.liveDataIsLoading.observe(
            this.viewLifecycleOwner, this::checkLoading
        )
        viewModel.liveDataChangePasswordState.observe(
            this.viewLifecycleOwner, this::checkState
        )
        binding.tvButtonConfirm.setOnClickListener {
            val password1 = binding.etPassword.text.toString().replace("\\s".toRegex(), "")
            val password2 = binding.etPassword2.text.toString().replace("\\s".toRegex(), "")
            if (password1 == password2 && password1.isNotEmpty()) {
                viewModel.changePassword(binding.etPassword.text.toString())
            } else {
                //TODO обработать ошибку с несовпадением пароля/поля пустные
            }
        }
    }

    private fun checkLoading(isLoading: Boolean) {
        if (isLoading) {
            bindingLoading.clLoadingPage.visibility = View.VISIBLE
        } else {
            bindingLoading.clLoadingPage.visibility = View.GONE
        }
    }

    //TODO обработать ошибки (подверждение кода)
    private fun checkState(state: ChangePasswordState) {
        when (state) {
            is ChangePasswordState.SuccessState -> {
                println("password has changed")
            }
            else -> {
                println("error")
            }
        }
    }
}