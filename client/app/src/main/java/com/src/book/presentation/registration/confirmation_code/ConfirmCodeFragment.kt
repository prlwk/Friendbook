package com.src.book.presentation.registration.confirmation_code

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.src.book.R
import com.src.book.databinding.FragmentConfirmCodeBinding

class ConfirmCodeFragment : Fragment() {

    private lateinit var binding: FragmentConfirmCodeBinding
    private lateinit var editTextList: List<EditText>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConfirmCodeBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirm_code, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentConfirmCodeBinding.bind(view)

        setCodeEditTextListeners()

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

    companion object {
    }
}