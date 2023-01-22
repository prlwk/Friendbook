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
import com.src.book.R
import com.src.book.databinding.FragmentBookBinding
import com.src.book.databinding.FragmentBookShimmerBinding
import com.src.book.domain.model.*
import com.src.book.presentation.MainActivity
import com.src.book.presentation.main.description.DescriptionFragment
import com.src.book.presentation.author.main_page.AuthorFragment
import com.src.book.presentation.book.main_page.adapter.AuthorNameAdapter
import com.src.book.presentation.book.main_page.adapter.GenreAdapter
import com.src.book.presentation.book.main_page.adapter.TagAdapter
import com.src.book.presentation.book.main_page.viewModel.BookViewModel
import com.src.book.presentation.book.rate_dialog.RateBookDialog
import com.src.book.presentation.utils.RatingColor


class BookFragment : Fragment() {
    private lateinit var binding: FragmentBookBinding
    private lateinit var bindingShimmer: FragmentBookShimmerBinding
    private lateinit var viewModel: BookViewModel
    private var bookId: Long = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = this.arguments
        //TODO обработка ошибки если книга не получена
        bookId = if (args?.getLong(BOOK_ID) != null) {
            args.getLong(BOOK_ID) as Long
        } else 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.binding = FragmentBookBinding.inflate(inflater)
        this.bindingShimmer = binding.layoutShimmer
        viewModel = (activity as MainActivity).getBookViewModel()
        return this.binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = FragmentBookBinding.bind(view)
        viewModel.liveDataBook.observe(
            this.viewLifecycleOwner, this::setState
        )
        viewModel.liveDataIsLoading.observe(
            this.viewLifecycleOwner, this::setView
        )
        viewModel.loadBookById(bookId)
        setOnClickListenerForBackButton()
        setOnClickListenerForRateButton()
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

    //TODO добавить оценки,рецензии, возможность оценить, сохранить
    private fun loadData(book: Book) {
        Glide.with(requireContext())
            .load(book.linkCover)
            .into(this.binding.ivBook)
        this.binding.tvBookName.text = book.name
        with(this.binding.tvGlobalRating) {
            val color = RatingColor.getColor(book.rating)
            setTextColor(ContextCompat.getColor(requireContext(), color))
            text = if (book.rating == 0.0) {
                resources.getText(R.string.no_rating)
            } else {
                book.rating.toString()
            }
        }
        if (book.description != null && book.description.isNotEmpty()) {
            this.binding.tvDescription.text = book.description
        }
        if (book.tags != null && book.tags.isNotEmpty()) {
            setAdapterForTagsRecyclerView(book.tags)
        } else {
            this.binding.tvTagTitle.visibility = View.GONE
            this.binding.rvTag.visibility = View.GONE
        }
        if (book.genres != null && book.genres.isNotEmpty()) {
            setAdapterForGenresRecyclerView(book.genres)
        } else {
            this.binding.tvGenreTitle.visibility = View.GONE
            this.binding.rvGenre.visibility = View.GONE
        }
        if (book.authors != null && book.authors.isNotEmpty()) {
            setAdapterForAuthorsRecyclerView(book.authors)
        } else {
            this.binding.rvBookAuthor.visibility = View.GONE
        }
        setBookShadow()
        setOnClickListenerForDescription(book)

    }

    private fun setBookShadow() {
        val density = Resources.getSystem().displayMetrics.density
        val dx = 20 * density
        val dy = 20 * density
        this.binding.ivBook.outlineProvider = object : ViewOutlineProvider() {
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
        this.binding.ivBook.elevation = 39 * density
    }

    private fun setAdapterForTagsRecyclerView(tags: List<Tag>) {
        val tagsAdapter = TagAdapter()
        tagsAdapter.submitList(tags)
        val layoutManager = GridLayoutManager(requireContext(), 1, RecyclerView.HORIZONTAL, false)
        this.binding.rvTag.layoutManager = layoutManager
        this.binding.rvTag.adapter = tagsAdapter
        this.binding.rvTag.isNestedScrollingEnabled = false
    }

    private fun setAdapterForGenresRecyclerView(tags: List<Genre>) {
        val genreAdapter = GenreAdapter()
        genreAdapter.submitList(tags)
        val layoutManager = GridLayoutManager(requireContext(), 1, RecyclerView.HORIZONTAL, false)
        this.binding.rvGenre.layoutManager = layoutManager
        this.binding.rvGenre.adapter = genreAdapter
        this.binding.rvGenre.isNestedScrollingEnabled = false
    }

    private fun setAdapterForAuthorsRecyclerView(authors: List<AuthorBook>) {
        val authorAdapter = AuthorNameAdapter { item -> onClickAuthor(item) }
        authorAdapter.submitList(authors)
        val layoutManager = GridLayoutManager(requireContext(), 1, RecyclerView.VERTICAL, false)
        this.binding.rvBookAuthor.layoutManager = layoutManager
        this.binding.rvBookAuthor.adapter = authorAdapter
        this.binding.rvBookAuthor.isNestedScrollingEnabled = false
    }

    private fun onClickAuthor(author: AuthorBook) {
        val bundle = Bundle()
        bundle.putLong(AuthorFragment.AUTHOR_ID, author.id)
        val fragment = AuthorFragment()
        fragment.arguments = bundle
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setOnClickListenerForBackButton() {
        this.binding.ivBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun setOnClickListenerForRateButton() {
        this.binding.clStar.setOnClickListener {
            val a = RateBookDialog()
            a.show(parentFragmentManager, "rate_book_dialog")
        }
    }

    private fun setOnClickListenerForDescription(book: Book) {
        this.binding.tvDescriptionMore.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(DescriptionFragment.DESCRIPTION, book.description)
            bundle.putString(DescriptionFragment.TITLE, "Описание")
            val fragment = DescriptionFragment()
            fragment.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setView(isLoading: Boolean) {
        if (isLoading) {
            setVisibilityMainPage(View.GONE)
            bindingShimmer.slBook.startShimmer()
        } else {
            setVisibilityMainPage(View.VISIBLE)
            bindingShimmer.slBook.stopShimmer()
            bindingShimmer.slBook.visibility = View.GONE
        }
    }

    private fun setVisibilityMainPage(visibility: Int) {
        binding.ivBook.visibility = visibility
        binding.tvBookName.visibility = visibility
        binding.rvBookAuthor.visibility = visibility
        binding.tvBookGenre.visibility = visibility
        binding.clStar.visibility = visibility
        binding.clRating.visibility = visibility
        binding.clSaving.visibility = visibility
        binding.clTemp.visibility = visibility
        binding.bottomSheet.visibility = visibility
    }

    companion object {
        const val BOOK_ID = "book_id"
    }
}