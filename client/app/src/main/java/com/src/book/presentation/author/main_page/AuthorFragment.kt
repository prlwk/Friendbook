package com.src.book.presentation.author.main_page

import android.content.res.Resources
import android.graphics.Outline
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.src.book.R
import com.src.book.databinding.AuthorBottomSheetBinding
import com.src.book.databinding.AuthorMainViewBinding
import com.src.book.databinding.FragmentAuthorBinding
import com.src.book.databinding.FragmentBookBinding

class AuthorFragment : Fragment() {
    private lateinit var binding: FragmentAuthorBinding
    private lateinit var mainBinding: AuthorMainViewBinding
    private lateinit var bottomSheetBinding: AuthorBottomSheetBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_author, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAuthorBinding.bind(view)
        bottomSheetBinding=binding.bottomSheet
        mainBinding = binding.main
        setPeekHeight()
        setAuthorShadow()
    }

    private fun setPeekHeight() {
        val linearLayout = mainBinding.ivAuthor
        val observer = mainBinding.clRating.viewTreeObserver
        var coordinates = IntArray(2)
        observer.addOnGlobalLayoutListener {
            linearLayout.getLocationInWindow(coordinates)
            val heightScreen = Resources.getSystem().displayMetrics.heightPixels
            val density = Resources.getSystem().displayMetrics.density
            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetBinding.bottomSheet)
            bottomSheetBehavior.peekHeight =
                ((heightScreen - coordinates[1]) - 120 * density).toInt()
        }
    }
    private fun setAuthorShadow() {
        val density = Resources.getSystem().displayMetrics.density
        val dx = 20 * density
        val dy = 20 * density
        mainBinding.ivAuthor.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setRoundRect(
                    -dx.toInt(),
                    -dy.toInt(),
                    (view.width + dx).toInt(),
                    (view.height + dy).toInt(),
                    30f
                )
            }
        }
        mainBinding.ivAuthor.elevation = 39 * density
    }
}