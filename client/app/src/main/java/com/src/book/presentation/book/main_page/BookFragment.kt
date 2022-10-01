package com.src.book.presentation.book.main_page

import android.content.res.Resources
import android.graphics.Outline
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.src.book.R
import com.src.book.databinding.BookBottomSheetBinding
import com.src.book.databinding.BookMainViewBinding
import com.src.book.databinding.FragmentBookBinding
import com.src.book.domain.model.*
import com.src.book.presentation.MainActivity
import com.src.book.presentation.book.main_page.adapter.AuthorNameAdapter
import com.src.book.presentation.book.main_page.adapter.GenreAdapter
import com.src.book.presentation.book.main_page.adapter.TagAdapter
import com.src.book.presentation.book.main_page.viewModel.BookViewModel
import com.src.book.presentation.utils.RatingColor


class BookFragment : Fragment() {
    private lateinit var binding: FragmentBookBinding
    private lateinit var bottomSheetBinding: BookBottomSheetBinding
    private lateinit var mainBinding: BookMainViewBinding
    private lateinit var viewModel: BookViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).getBookViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookBinding.inflate(inflater)
        bottomSheetBinding = binding.bottomSheet
        mainBinding = binding.main
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBookBinding.bind(view)
        viewModel.liveDataBook.observe(
            this.viewLifecycleOwner, this::setState
        )
        viewModel.loadBookById(1)
    }


    //TODO обработка ошибки загрузки книги
    private fun setState(state: BookState) {
        when (state) {
            is BookState.DefaultState -> state.book?.let { loadData(it) }
            is BookState.ErrorState -> Toast.makeText(
                requireContext(),
                "Book loading error",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    //TODO добавить оценки, возможность оценить, сохранить
    private fun loadData(book: Book) {
        Glide.with(requireContext())
            .load(book.linkCover)
            .into(mainBinding.ivBook)
        mainBinding.tvBookName.text = book.name
        with(mainBinding.tvGlobalRating) {
            val color = RatingColor.getColor(book.rating)
            setTextColor(ContextCompat.getColor(requireContext(), color))
            text = if (book.rating == 0.0) {
                resources.getText(R.string.no_rating)
            } else {
                book.rating.toString()
            }
        }
        if (book.description != null && book.description.isNotEmpty()) {
            bottomSheetBinding.tvDescription.text = book.description
        }
        if (book.tags != null && book.tags.isNotEmpty()) {
            setAdapterForTagsRecyclerView(book.tags)
        } else {
            //TODO
        }
        if (book.genres != null && book.genres.isNotEmpty()) {
            setAdapterForGenresRecyclerView(book.genres)
        } else {
            //TODO
        }
        if (book.authors != null && book.authors.isNotEmpty()) {
            setAdapterForAuthorsRecyclerView(book.authors)
        } else {
            //TODO
        }
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
        val observer = mainBinding.clRating.viewTreeObserver
        var coordinates = IntArray(2)
        observer.addOnGlobalLayoutListener {
            linearLayout.getLocationInWindow(coordinates)
            val heightScreen = Resources.getSystem().displayMetrics.heightPixels
            val density = Resources.getSystem().displayMetrics.density
            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetBinding.bottomSheet)
            val y = (heightScreen - coordinates[1] - 100 * density).toInt()
            if (y > 50) {
                bottomSheetBehavior.peekHeight = y
            } else {
                bottomSheetBehavior.peekHeight = 50 * density.toInt()
            }
        }
    }

    private fun setAdapterForTagsRecyclerView(tags: List<Tag>) {
        val tagsAdapter = TagAdapter()
        tagsAdapter.submitList(tags)
        val layoutManager = GridLayoutManager(requireContext(), 1, RecyclerView.HORIZONTAL, false)
        bottomSheetBinding.rvTag.layoutManager = layoutManager
        bottomSheetBinding.rvTag.adapter = tagsAdapter
        bottomSheetBinding.rvTag.isNestedScrollingEnabled = false
    }

    private fun setAdapterForGenresRecyclerView(tags: List<Genre>) {
        val genreAdapter = GenreAdapter()
        genreAdapter.submitList(tags)
        val layoutManager = GridLayoutManager(requireContext(), 1, RecyclerView.HORIZONTAL, false)
        bottomSheetBinding.rvGenre.layoutManager = layoutManager
        bottomSheetBinding.rvGenre.adapter = genreAdapter
        bottomSheetBinding.rvGenre.isNestedScrollingEnabled = false
    }

    private fun setAdapterForAuthorsRecyclerView(authors: List<AuthorBook>) {
        val authorAdapter = AuthorNameAdapter()
        authorAdapter.submitList(authors)
        val layoutManager = GridLayoutManager(requireContext(), 1, RecyclerView.VERTICAL, false)
        mainBinding.rvBookAuthor.layoutManager = layoutManager
        mainBinding.rvBookAuthor.adapter = authorAdapter
        mainBinding.rvBookAuthor.isNestedScrollingEnabled = false
    }
}