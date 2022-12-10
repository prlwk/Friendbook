package com.src.book.presentation.registration.first_registration

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.src.book.R
import com.src.book.databinding.FragmentConfirmCodeBinding
import com.src.book.databinding.FragmentLoadingBinding
import com.src.book.domain.utils.BasicState
import com.src.book.domain.utils.CodeState
import com.src.book.presentation.registration.LoginActivity
import com.src.book.presentation.registration.first_registration.viewModel.RegistrationViewModel

class ConfirmCodeFragment : Fragment() {

    private lateinit var binding: FragmentConfirmCodeBinding
    private lateinit var editTextList: List<EditText>
    private lateinit var viewModel: RegistrationViewModel
    private lateinit var bindingLoading: FragmentLoadingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConfirmCodeBinding.inflate(inflater)
        bindingLoading = binding.loading
        viewModel = (activity as LoginActivity).getRegistrationViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.liveDataIsLoading.observe(
            this.viewLifecycleOwner, this::checkLoading
        )
        viewModel.liveDataCodeState.observe(
            this.viewLifecycleOwner, this::checkState
        )
        viewModel.liveDataRepeatingCodeState.observe(
            this.viewLifecycleOwner, this::checkSendRepeatingCodeState
        )
        setCodeEditTextListeners()
        setListenerForLastEditText()
        //TODO вызвать метод, когда нужно будет отправить код на поту повторно
        // viewModel.sendRepeatingCodeState()

    }

    private fun checkLoading(isLoading: Boolean) {
        if (isLoading) {
            bindingLoading.clLoadingPage.visibility = View.VISIBLE
        } else {
            bindingLoading.clLoadingPage.visibility = View.GONE
        }
    }

    //TODO обработать ошибки (подверждение кода)
    private fun checkState(state: CodeState) {
        when (state) {
            is CodeState.SuccessState -> {
                val fragment = CongratulationRegistrationFragment()
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragment_container, fragment)
                    ?.addToBackStack(null)
                    ?.commit()
            }
            is CodeState.WrongCodeState -> {
                viewModel.setDefaultValueForCodeState()
                println("wrong code")
            }
            is CodeState.ErrorState -> {
                viewModel.setDefaultValueForCodeState()
                println("error")
            }
            else ->{}
        }
    }

    //TODO обработать ошибки (повторная отправка кода)
    private fun checkSendRepeatingCodeState(state: BasicState) {
        when (state) {
            is BasicState.SuccessState -> {
                println("код отправлен")
            }
            is BasicState.ErrorState ->{
                println("ошибка")
            }
            else -> {}
        }
    }

    private fun setListenerForLastEditText() {
        binding.etCodeSymbol6.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                var code = ""
                for (i in editTextList) {
                    code += i.text.toString()
                }
                editTextList[0].text.toString()
                viewModel.checkRecoveryCode(code = code)
            }
        })
    }

    private fun setCodeEditTextListeners() {
        with(binding) {
            editTextList = listOf(
                etCodeSymbol1,
                etCodeSymbol2,
                etCodeSymbol3,
                etCodeSymbol4,
                etCodeSymbol5,
                etCodeSymbol6
            )
        }

        for (i in editTextList.indices) {
            editTextList[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(p0: Editable?) {
                    if (p0 != null) {
                        if (p0.length == 1 && i != editTextList.lastIndex) {
                            editTextList[i + 1].requestFocus()
                        } else if (p0.isEmpty() && i != 0) {
                            editTextList[i - 1].requestFocus()
                        }
                    }
                }
            })
        }
    }
}