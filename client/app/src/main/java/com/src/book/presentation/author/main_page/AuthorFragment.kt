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
import com.src.book.R
import com.src.book.databinding.FragmentAuthorBinding
import com.src.book.databinding.FragmentAuthorShimmerBinding
import com.src.book.domain.model.Author
import com.src.book.domain.model.BookAuthor
import com.src.book.presentation.MainActivity
import com.src.book.presentation.author.AuthorState
import com.src.book.presentation.main.description.DescriptionFragment
import com.src.book.presentation.author.main_page.viewModel.AuthorViewModel
import com.src.book.presentation.book.main_page.BookFragment
import com.src.book.presentation.author.list_of_books.ListOfBooksFragment
import com.src.book.presentation.main.main_page.adapter.BookListAdapter
import com.src.book.presentation.utils.*
import com.src.book.utils.*

class AuthorFragment : Fragment() {
    private lateinit var binding: FragmentAuthorBinding
    private lateinit var bindingShimmer: FragmentAuthorShimmerBinding
    private lateinit var viewModel: AuthorViewModel
    private var authorId: Long = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = this.arguments
        //TODO обработка ошибки если автор не получен
        authorId = if (args?.getLong(AUTHOR_ID) != null) {
            args.getLong(AUTHOR_ID)
        } else {
            1
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.binding = FragmentAuthorBinding.inflate(inflater)
        this.bindingShimmer = binding.layoutShimmer
        viewModel = (activity as MainActivity).getAuthorViewModel()
        return this.binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.liveDataAuthor.observe(
            this.viewLifecycleOwner, this::setState
        )
        viewModel.liveDataIsLoading.observe(
            this.viewLifecycleOwner, this::setView
        )
        viewModel.loadAuthorById(authorId)
        this.binding.ivBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        setOnClickListenerForBackButton()
    }


    //TODO обработка ошибки загрузки автора
    private fun setState(state: AuthorState) {
        when (state) {
            is AuthorState.SuccessState -> state.author?.let { loadData(it) }
            is AuthorState.ErrorState -> Toast.makeText(
                requireContext(),
                "Author loading error",
                Toast.LENGTH_LONG
            ).show()
            else -> {}
        }
    }

    //TODO если книг нет
    private fun loadData(author: Author) {
        if (author.books != null && author.books.isNotEmpty()) {
            setAdapterForBookRecyclerView(author.books)
        }
        //TODO добавить placeholder (картинка, которая будет, если не загружается сама картинка с ссылки)
        Glide.with(requireContext())
            .load(author.photoSrc)
            .into(this.binding.ivAuthor)
        this.binding.tvAuthorName.text = author.name
        this.binding.tvYear.text = author.yearsLife
        if (author.biography != null) {
            this.binding.tvBiography.text = author.biography
        } else {
            this.binding.tvBiography.visibility = View.GONE
            this.binding.tvBiographyTitle.visibility = View.GONE
        }
        with(this.binding.tvGlobalRating) {
            val color = RatingColor.getColor(author.rating)
            setTextColor(ContextCompat.getColor(requireContext(), color))
            text = if (author.rating == 0.0) {
                resources.getText(R.string.no_rating)
            } else {
                author.rating.toString()
            }
        }
        setAuthorShadow()
        setOnClickListenerForBiography(author)
        setOnClickListenerForBooks(author)
    }

    private fun setAdapterForBookRecyclerView(listBookAuthor: List<BookAuthor>) {
        val listBookAdapter = BookListAdapter { item -> onClickBook(item) }
        listBookAdapter.submitList(listBookAuthor)
        val layoutManager = GridLayoutManager(requireContext(), 1, RecyclerView.HORIZONTAL, false)
        this.binding.rvBook.layoutManager = layoutManager
        this.binding.rvBook.adapter = listBookAdapter
        this.binding.rvBook.isNestedScrollingEnabled = false
    }

    private fun setAuthorShadow() {
        val density = Resources.getSystem().displayMetrics.density
        val dx = 20 * density
        val dy = 20 * density
        this.binding.ivAuthor.outlineProvider = object : ViewOutlineProvider() {
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
        this.binding.ivAuthor.elevation = 39 * density
    }
//TODO обрать хардкод строк
    private fun setOnClickListenerForBiography(author: Author) {
        this.binding.tvBiographyMore.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(DescriptionFragment.DESCRIPTION, author.biography)
            bundle.putString(DescriptionFragment.TITLE, "Биография")
            val fragment = DescriptionFragment()
            fragment.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setOnClickListenerForBooks(author: Author) {
        this.binding.tvBookMore.setOnClickListener {
            val bundle = Bundle()
            bundle.putLong(ListOfBooksFragment.AUTHOR_ID, author.id)
            bundle.putString(ListOfBooksFragment.TITLE, author.name)
            val fragment = ListOfBooksFragment()
            fragment.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun onClickBook(book: BookAuthor) {
        val bundle = Bundle()
        bundle.putLong(BookFragment.BOOK_ID, book.id)
        val fragment = BookFragment()
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
        binding.ivAuthor.visibility = visibility
        binding.tvAuthorName.visibility = visibility
        binding.tvYear.visibility = visibility
        binding.tvGlobalRating.visibility = visibility
        binding.tvRatingTitle.visibility = visibility
        binding.clTemp.visibility = visibility
        binding.bottomSheet.visibility = visibility
    }

    companion object {
        const val AUTHOR_ID = "author_id"
    }
}