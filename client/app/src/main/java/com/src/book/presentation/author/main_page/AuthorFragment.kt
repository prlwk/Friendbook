package com.src.book.presentation.author.main_page

import android.content.res.Resources
import android.graphics.Outline
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.src.book.R
import com.src.book.databinding.AuthorBottomSheetBinding
import com.src.book.databinding.AuthorMainViewBinding
import com.src.book.databinding.FragmentAuthorBinding
import com.src.book.domain.model.Author
import com.src.book.domain.model.BookAuthor
import com.src.book.presentation.MainActivity
import com.src.book.presentation.author.AuthorState
import com.src.book.presentation.author.biography.BiographyFragment
import com.src.book.presentation.author.main_page.viewModel.AuthorViewModel
import com.src.book.presentation.main.list_of_books.ListOfBooksFragment
import com.src.book.presentation.main.main_page.adapter.BookListAdapter
import com.src.book.presentation.utils.*
import com.src.book.utlis.AUTHOR_ID
import com.src.book.utlis.BIOGRAPHY
import com.src.book.utlis.TITLE
import com.src.book.utlis.TITLE_SECTION_NAME

class AuthorFragment : Fragment() {
    private lateinit var binding: FragmentAuthorBinding
    private lateinit var mainBinding: AuthorMainViewBinding
    private lateinit var bottomSheetBinding: AuthorBottomSheetBinding
    private lateinit var viewModel: AuthorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).getAuthorViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthorBinding.inflate(inflater)
        bottomSheetBinding = binding.bottomSheet
        mainBinding = binding.main
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.liveDataAuthor.observe(
            this.viewLifecycleOwner, this::setState
        )
        viewModel.loadAuthorById(1)
        mainBinding.ivBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }


    //TODO обработка ошибки загрузки автора
    private fun setState(state: AuthorState) {
        when (state) {
            is AuthorState.DefaultState -> state.author?.let { loadData(it) }
            is AuthorState.ErrorState -> Toast.makeText(
                requireContext(),
                "Author loading error",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    //TODO если книг нет
    private fun loadData(author: Author) {
        if (author.books != null) {
            setAdapterForBookRecyclerView(author.books)
        }
        //TODO добавить placeholder (картинка, которая будет, если не загружается сама картинка с ссылки)
        Glide.with(requireContext())
            .load(author.photoSrc)
            .into(mainBinding.ivAuthor)
        mainBinding.tvAuthorName.text = author.name
        mainBinding.tvYear.text = author.yearsLife
        if (author.biography != null) {
            bottomSheetBinding.tvBiography.text = author.biography
        } else {
            bottomSheetBinding.tvBiography.visibility = View.GONE
            bottomSheetBinding.tvBiographyTitle.visibility = View.GONE
        }
        with(mainBinding.tvGlobalRating) {
            val color = RatingColor.getColor(author.rating)
            setTextColor(ContextCompat.getColor(requireContext(), color))
            text = if (author.rating == 0.0) {
                resources.getText(R.string.no_rating)
            } else {
                author.rating.toString()
            }
        }
        setPeekHeight()
        setAuthorShadow()
        setOnClickListenerForBiography(author)
        setOnClickListenerForBooks(author)
    }

    private fun setAdapterForBookRecyclerView(listBookAuthor: List<BookAuthor>) {
        val listBookAdapter = BookListAdapter()
        listBookAdapter.submitList(listBookAuthor)
        println("BOOOK")
        val layoutManager = GridLayoutManager(requireContext(), 1, RecyclerView.HORIZONTAL, false)
        bottomSheetBinding.rvBook.layoutManager = layoutManager
        bottomSheetBinding.rvBook.adapter = listBookAdapter
//        bottomSheetBinding.rvBook.isNestedScrollingEnabled=false
    }

    private fun setPeekHeight() {
        val observer = mainBinding.tvGlobalRating.viewTreeObserver
        var coordinates = IntArray(2)
        observer.addOnGlobalLayoutListener {
            mainBinding.tvGlobalRating.getLocationInWindow(coordinates)
            val heightScreen = Resources.getSystem().displayMetrics.heightPixels
            val density = Resources.getSystem().displayMetrics.density
            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetBinding.bottomSheet)
            val y = (heightScreen - coordinates[1] - 100 * density).toInt()
            if (y > 0) {
                bottomSheetBehavior.peekHeight =
                    (heightScreen - coordinates[1] - 100 * density).toInt()
            } else {
                bottomSheetBehavior.peekHeight = 100 * density.toInt()
            }
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

    private fun setOnClickListenerForBiography(author: Author) {
        bottomSheetBinding.tvBiographyMore.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(BIOGRAPHY, author.biography)
            val fragment = BiographyFragment()
            fragment.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setOnClickListenerForBooks(author: Author) {
        bottomSheetBinding.tvBookMore.setOnClickListener {
            val bundle = Bundle()
            bundle.putLong(AUTHOR_ID, author.id)
            bundle.putString(TITLE_SECTION_NAME, "Автор")
            bundle.putString(TITLE, author.name)
            val fragment = ListOfBooksFragment()
            fragment.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }
}