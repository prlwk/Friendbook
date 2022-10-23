package com.src.book.presentation.main.description

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.src.book.databinding.FragmentDescriptionBinding
import com.src.book.utils.DESCRIPTION
import com.src.book.utils.TITLE

class DescriptionFragment : Fragment() {
    private lateinit var binding: FragmentDescriptionBinding
    private lateinit var biography: String
    private lateinit var title: String

    //TODO обработка ошибки если биография не получена
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ):
            View? {
        val args = this.arguments
        biography = args?.getString(DESCRIPTION).toString()
        title = args?.getString(TITLE).toString()
        binding = FragmentDescriptionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvBiography.text = biography
        binding.tvTitle.text = title

        binding.ivBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
}