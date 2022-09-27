package com.src.book.presentation.author.biography

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.src.book.databinding.FragmentBiographyBinding
import com.src.book.presentation.utils.BIOGRAPHY

class BiographyFragment : Fragment() {
    private lateinit var binding: FragmentBiographyBinding
    private lateinit var biography: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val args = this.arguments
        biography = args?.getString(BIOGRAPHY).toString()
        binding = FragmentBiographyBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvBiography.text = biography

        binding.ivBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
}