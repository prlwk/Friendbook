package com.src.book.presentation.profile.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.src.book.R
import com.src.book.databinding.FragmentLoadingBinding
import com.src.book.databinding.FragmentSettingsBinding
import com.src.book.domain.utils.BasicState
import com.src.book.presentation.MainActivity
import com.src.book.presentation.profile.settings.viewModel.SettingsViewModel

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var viewModel: SettingsViewModel
    private lateinit var bindingLoading: FragmentLoadingBinding
    private var isClickExit =
        false //TODO когда кликнута кнопка вы точно хотите выйти она должна поменяться true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = (activity as MainActivity).getSettingsViewModel()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingsBinding.bind(view)
        bindingLoading = binding.loading
        binding.clLogOut.setOnClickListener {
            showBottomSheetDialog()
        }
        viewModel.liveDataIsLoading.observe(
            this.viewLifecycleOwner, this::checkLoading
        )
        viewModel.liveDataBasicState.observe(
            this.viewLifecycleOwner, this::checkState
        )
        //TODO вызвать этот метод, когда нажата кнопка "вы точно хотите выйти"
        // viewModel.logout()
    }

    private fun showBottomSheetDialog() {
        val bottomSheetDialog =
            context?.let { BottomSheetDialog(it, R.style.BottomSheetExitDialog) }
        bottomSheetDialog?.setContentView(R.layout.bottom_sheet_dialog_log_out)

        bottomSheetDialog?.show()
    }

    private fun checkLoading(isLoading: Boolean) {
        if (isLoading) {
            bindingLoading.clLoadingPage.visibility = View.VISIBLE
        } else {
            bindingLoading.clLoadingPage.visibility = View.GONE
        }
    }

    private fun checkState(state: BasicState<Unit>) {
        when (state) {
            is BasicState.SuccessState -> {
                if (isClickExit) {
                    //TODO вернуться на страницу регистрации
                }
            }
            is BasicState.ErrorState -> {
                //TODO сообщить об ошибке (выйти не удалось)
            }
        }
        isClickExit = false
    }
}