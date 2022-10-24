package com.src.book.presentation.registration.password_recovery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.src.book.databinding.FragmentLoadingBinding
import com.src.book.databinding.FragmentPasswordRecoveryEmailBinding
import com.src.book.presentation.registration.LoginActivity
import com.src.book.presentation.registration.password_recovery.viewModel.PasswordRecoveryEmailViewModel
import com.src.book.utils.REGEX_EMAIL
import java.util.regex.Pattern

class PasswordRecoveryEmailFragment : Fragment() {
    private lateinit var binding: FragmentPasswordRecoveryEmailBinding
    private lateinit var viewModel: PasswordRecoveryEmailViewModel
    private lateinit var bindingLoading: FragmentLoadingBinding
    private var onClickNext = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPasswordRecoveryEmailBinding.inflate(inflater)
        bindingLoading = binding.loginLoading
        viewModel = (activity as LoginActivity).getPasswordRecoveryEmailViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.liveDataIsLoading.observe(this.viewLifecycleOwner, this::checkLoading)
        viewModel.liveDataEmailExists.observe(this.viewLifecycleOwner, this::checkEmailExists)
        setOnClickListenerForNextButton()
    }

    private fun checkEmailExists(isExists: Boolean) {
        if (onClickNext) {
            if (isExists) {
                binding.tilEmail.error = ""
                //TODO перейти далее на ввод кода
            } else {
                binding.tilEmail.error = "Такой почты не существует."
                binding.tilEmail.errorIconDrawable = null
                onClickNext = false
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

    private fun setOnClickListenerForNextButton() {
        binding.tvButtonNext.setOnClickListener {
            val email = binding.etEmail.text.toString()
            if (Pattern.matches(REGEX_EMAIL, email)) {
                viewModel.checkEmailExists(binding.etEmail.text.toString())
            } else {
                binding.tilEmail.error = "Неккоректный email."
                binding.tilEmail.errorIconDrawable = null
            }
        }
    }
}