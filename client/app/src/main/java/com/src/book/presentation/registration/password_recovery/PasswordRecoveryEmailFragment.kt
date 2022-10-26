package com.src.book.presentation.registration.password_recovery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.src.book.R
import com.src.book.databinding.FragmentLoadingBinding
import com.src.book.databinding.FragmentPasswordRecoveryEmailBinding
import com.src.book.domain.utils.CodeState
import com.src.book.presentation.registration.LoginActivity
import com.src.book.presentation.registration.password_recovery.viewModel.passwordRecoveryEmail.PasswordRecoveryEmailViewModel
import com.src.book.utils.BUNDLE_EMAIL
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
        viewModel.liveDataCodeState.observe(this.viewLifecycleOwner, this::checkCodeState)
        setOnClickListenerForNextButton()
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
            if (!onClickNext) {
                val email = binding.etEmail.text.toString()
                if (Pattern.matches(REGEX_EMAIL, email)) {
                    viewModel.sendCode(email)
                    onClickNext = true
                } else {
                    binding.tilEmail.error = "Неккоректный email."
                    binding.tilEmail.errorIconDrawable = null
                }
            }
        }
    }

    //TODO когда ошибка сообщить пользователю∆
    private fun checkCodeState(state: CodeState) {
        if (onClickNext) {
            when (state) {
                is CodeState.SuccessState -> {
                    val bundle = Bundle()
                    bundle.putString(BUNDLE_EMAIL, binding.etEmail.text.toString())
                    val fragment = PasswordRecoveryCodeFragment()
                    fragment.arguments = bundle
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.fragment_container, fragment)
                        ?.addToBackStack(null)
                        ?.commit()
                }
                is CodeState.WrongEmailState -> {
                    binding.tilEmail.error = "Такой почты не существует."
                    binding.tilEmail.errorIconDrawable = null
                    onClickNext = false
                }
                else -> {
                    println("error")
                    onClickNext = false
                }
            }
        }

    }
}