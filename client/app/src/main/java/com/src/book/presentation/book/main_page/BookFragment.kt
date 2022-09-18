package com.src.book.presentation.book.main_page

import android.content.res.Resources
import android.graphics.Outline
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.src.book.R
import com.src.book.databinding.BookBottomSheetBinding
import com.src.book.databinding.BookMainViewBinding
import com.src.book.databinding.FragmentBookBinding
import com.src.book.presentation.book.main_page.adapter.TagAdapter


class BookFragment : Fragment() {
    private lateinit var binding: FragmentBookBinding
    private lateinit var bottomSheetBinding: BookBottomSheetBinding
    private lateinit var mainBinding: BookMainViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBookBinding.bind(view)
        bottomSheetBinding = binding.bottomSheet
        mainBinding = binding.main
        // setAdapterForTagsRecyclerView(listOf("tag1", "tag2", "tag3"))
        // setAdapterForGenresRecyclerView(listOf("genre1", "genre2", "genre3"))
        setPeekHeight()
        setBookShadow()
    }

    private fun setBookShadow() {
        val density = Resources.getSystem().displayMetrics.density
        val dx = 20 * density
        val dy = 20 * density
        mainBinding.ivBook.outlineProvider = object : ViewOutlineProvider() {
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
        mainBinding.ivBook.elevation = 39 * density
    }

    private fun setPeekHeight() {
        val linearLayout = mainBinding.ivBookmark
        val observer = mainBinding.buttons.viewTreeObserver
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

    private fun setAdapterForTagsRecyclerView(tags: List<String>) {
        val tagsAdapter = TagAdapter()
        tagsAdapter.submitList(tags)
        val layoutManager = GridLayoutManager(requireContext(), 1, RecyclerView.HORIZONTAL, false)
        bottomSheetBinding.rvTag.layoutManager = layoutManager
        bottomSheetBinding.rvTag.adapter = tagsAdapter
        bottomSheetBinding.rvTag.isNestedScrollingEnabled = false
    }

    private fun setAdapterForGenresRecyclerView(tags: List<String>) {
        val tagsAdapter = TagAdapter()
        tagsAdapter.submitList(tags)
        val layoutManager = GridLayoutManager(requireContext(), 1, RecyclerView.HORIZONTAL, false)
        bottomSheetBinding.rvGenre.layoutManager = layoutManager
        bottomSheetBinding.rvGenre.adapter = tagsAdapter
        bottomSheetBinding.rvGenre.isNestedScrollingEnabled = false
    }

    companion object {
    }
}