package com.src.book.presentation.registration.first_registration

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.src.book.databinding.FragmentLoadingBinding
import com.src.book.databinding.FragmentRegistrationBinding
import com.src.book.domain.utils.BasicState
import com.src.book.presentation.registration.LoginActivity
import com.src.book.presentation.registration.first_registration.viewModel.RegistrationViewModel
import com.src.book.presentation.registration.sign_in.SignInFragment
import com.src.book.utils.REGEX_EMAIL
import java.util.regex.Pattern

class RegistrationFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationBinding
    private lateinit var viewModel: RegistrationViewModel
    private lateinit var bindingLoading: FragmentLoadingBinding
    private var onClickNext = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistrationBinding.inflate(inflater)
        bindingLoading = binding.loginLoading
        viewModel = (activity as LoginActivity).getRegistrationViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.liveDataIsLoading.observe(this.viewLifecycleOwner, this::checkLoading)
        viewModel.liveDataEmailExists.observe(this.viewLifecycleOwner, this::checkEmailExists)
        if (viewModel.liveDataEmail.value != null) {
            binding.etEmail.setText(viewModel.liveDataEmail.value)
        }
        binding.tvButtonNext.setOnClickListener {
            if (!onClickNext) {
                val emailWithoutSpace = removeAllSpaces(binding.etEmail.text.toString())

                if (emailWithoutSpace == null ||
                    !Pattern.matches(REGEX_EMAIL, emailWithoutSpace.toString())
                ) {
                    binding.tilEmail.error = "Некорректная почта."
                } else {
                    viewModel.setEmail(emailWithoutSpace)
                    viewModel.checkEmailExists(emailWithoutSpace)
                    onClickNext = true

                }
            }
        }
        setOnClickListenerForSkipButton()
        setOnClickListenerForSignInButton()
    }

    private fun checkEmailExists(state: BasicState<Boolean>) {
        if (onClickNext) {
            onClickNext = false
            Log.d("onClickNext", "onClick")
            if (state is BasicState.SuccessState<*>) {
                val isExists = state.data as Boolean
                if (isExists) {
                    binding.tilEmail.error = "Такой email уже существует"
                    binding.tilEmail.errorIconDrawable = null
                    onClickNext = false
                } else {
                    val password1 = removeAllSpaces(binding.etPassword.text.toString())
                    val password2 = removeAllSpaces(binding.etPassword2.text.toString())
                    if (password1 == null || password1.isEmpty()) {
                        binding.tilPassword.error = "Заполните поле."
                    }
                    if (password2 == null || password2.isEmpty()) {
                        binding.tilPassword2.error = "Заполните поле."
                    }
                    if (password1 != password2) {
                        //TODO сообщить пользователю, что пароли не совпадают
                    }
                    if (password1 != null && password2 != null && password1.isNotEmpty() && password2.isNotEmpty() && password1 == password2) {
                        viewModel.setPassword(password1)
                        (activity as LoginActivity).replaceFragment(RegistrationUserInfoFragment())
                    }
                }
            } else {
                //TODo обработать ошибку сервера
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

    //TODO перейти в новый фрагмент
    private fun setOnClickListenerForSkipButton() {
        binding.tvSkipButton.setOnClickListener {
            viewModel.loginAsGuest()
        }
    }

    private fun setOnClickListenerForSignInButton() {
        binding.tvSignin.setOnClickListener {
            (activity as LoginActivity).replaceFragment(SignInFragment())
        }
    }

    private fun removeAllSpaces(text: String?) = text?.replace("\\s".toRegex(), "")

}