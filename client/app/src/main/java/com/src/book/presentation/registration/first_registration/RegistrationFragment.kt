package com.src.book.presentation.registration.first_registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.src.book.databinding.FragmentLoginLoadingBinding
import com.src.book.databinding.FragmentRegistrationBinding
import com.src.book.presentation.registration.LoginActivity
import com.src.book.presentation.registration.first_registration.viewModel.RegistrationViewModel

class RegistrationFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationBinding
    private lateinit var viewModel: RegistrationViewModel
    private lateinit var bindingLoading: FragmentLoginLoadingBinding
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
        binding.tvButtonNext.setOnClickListener {
//            binding.tilEmail.error = "Некорректная почта"
//            binding.tilPassword.error = "Некорректный пароль"
//            binding.tilPassword2.error = "Некорректный пароль снова"
//            binding.tilEmail.errorIconDrawable = null
//            binding.tilPassword.errorIconDrawable = null
//            binding.tilPassword2.errorIconDrawable = null
            //TODO сюда передать почту из эддит текста
            if (!onClickNext) {
                viewModel.checkEmailExists("alena-alena-2002@yandex.r")
                onClickNext = true
            }
        }
    }

    private fun checkEmailExists(isExists: Boolean) {
        if (onClickNext) {
            if (isExists) {
                //TODO добавить ошибку для поля с имейлом (что такой уже существует)
                println("Существует")
                onClickNext = false
            } else {
                //TODO перейти далее
                println("Не Существует")
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

}