package com.src.book.presentation.search.filter

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.src.book.R
import com.src.book.databinding.FragmentAllFiltersBinding


class AllFiltersFragment : Fragment() {
    private lateinit var binding: FragmentAllFiltersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllFiltersBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etGenreTagName.setOnClickListener {
            if (binding.mlSearchBar.currentState != R.id.with_cancel_button) {
                binding.mlSearchBar.transitionToState(R.id.with_cancel_button)
                binding.etGenreTagName.isCursorVisible = true
            }
        }

        binding.etGenreTagName.setOnFocusChangeListener { _, _ ->
            if (binding.mlSearchBar.currentState != R.id.with_cancel_button) {
                binding.mlSearchBar.transitionToState(R.id.with_cancel_button)
                binding.etGenreTagName.isCursorVisible = true
            }
        }
        binding.cancelButton.setOnClickListener {
            hideKeyboard()
            if (binding.mlSearchBar.currentState != R.id.no_cancel_button) {
                binding.mlSearchBar.transitionToState(R.id.no_cancel_button)
                binding.etGenreTagName.isCursorVisible = false
            }
        }

    }

    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    companion object {
    }
}