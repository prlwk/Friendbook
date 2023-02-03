package com.src.book.presentation.registration.sign_in

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.src.book.databinding.FragmentLoadingBinding
import com.src.book.databinding.FragmentSignInBinding
import com.src.book.domain.utils.LoginState
import com.src.book.presentation.registration.LoginActivity
import com.src.book.presentation.registration.first_registration.RegistrationFragment
import com.src.book.presentation.registration.password_recovery.PasswordRecoveryEmailFragment
import com.src.book.presentation.registration.sign_in.viewModel.SignInViewModel
import com.src.book.utils.REGEX_EMAIL
import java.util.regex.Pattern

class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding
    private lateinit var bindingLoading: FragmentLoadingBinding

    private lateinit var viewModel: SignInViewModel
    private var isClickNext = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.binding = FragmentSignInBinding.inflate(inflater)
        this.bindingLoading = binding.loginLoading
        viewModel = (activity as LoginActivity).getSignInViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.liveDataIsLoading.observe(
            this.viewLifecycleOwner, this::checkLoading
        )
        viewModel.liveDataLoginState.observe(
            this.viewLifecycleOwner, this::checkState
        )
        binding.tvButtonSignIn.setOnClickListener {
            if (!isClickNext) {
                val isEntryByEmail: Boolean
                val emailWithoutSpace = removeAllSpaces(binding.etEmail.text.toString())
                isEntryByEmail = Pattern.matches(REGEX_EMAIL, emailWithoutSpace.toString())
                val passwordWithoutSpace = removeAllSpaces(binding.etPassword.text.toString())
                binding.etEmail.text
                isClickNext = true
                viewModel.signInRequest(
                    emailWithoutSpace!!,
                    passwordWithoutSpace!!,
                    isEntryByEmail
                )
            }
        }
        setOnClickListenerForForgotPassword()
        setOnClickListenerForSkipButton()
        setOnClickListenerForRegisterButton()
    }

    private fun checkLoading(isLoading: Boolean) {
        if (isLoading) {
            bindingLoading.clLoadingPage.visibility = View.VISIBLE
        } else {
            bindingLoading.clLoadingPage.visibility = View.GONE
        }
    }

    private fun checkState(state: LoginState) {
        when (state) {
            is LoginState.SuccessState -> {
                if (isClickNext) {
                    binding.tilEmail.error = ""
                    binding.tilPassword.error = ""
                    binding.tilEmail.error = ""
                    binding.tilPassword.error = ""
                }
            }
            is LoginState.ErrorEmailLoginState -> {
                binding.tilEmail.error = "Неверная почта или никнейм."
                binding.tilEmail.errorIconDrawable = null
            }
            is LoginState.ErrorPasswordState -> {
                binding.tilPassword.error = "Неверный пароль."
                binding.tilPassword.errorIconDrawable = null
            }
            is LoginState.ErrorServerState -> Toast.makeText(
                requireContext(),
                "Ошибка сервера",
                Toast.LENGTH_LONG
            ).show()
        }
        isClickNext = false
    }

    private fun removeAllSpaces(text: String?) = text?.replace("\\s".toRegex(), "")
    private fun setOnClickListenerForForgotPassword() {
        binding.tvForgotPassword.setOnClickListener {
            (activity as LoginActivity).replaceFragment(PasswordRecoveryEmailFragment())
        }
    }

    //TODO перейти в новый фрагмент
    private fun setOnClickListenerForSkipButton() {
        binding.tvSkipButton.setOnClickListener {
            viewModel.loginAsGuest()
        }
    }

    private fun setOnClickListenerForRegisterButton() {
        binding.tvRegister.setOnClickListener {
            (activity as LoginActivity).replaceFragment(RegistrationFragment())
        }
    }
}