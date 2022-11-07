package com.src.book.presentation.author.list_of_books

import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.src.book.R
import com.src.book.databinding.FragmentListOfBooksBinding
import com.src.book.domain.model.BookList
import com.src.book.presentation.MainActivity
import com.src.book.presentation.book.main_page.BookFragment
import com.src.book.presentation.author.list_of_books.adapter.ListOfBooksAdapter
import com.src.book.presentation.author.list_of_books.viewModel.ListOfBooksViewModel
import com.src.book.utils.AUTHOR_ID
import com.src.book.utils.BOOK_ID
import com.src.book.utils.TITLE

class ListOfBooksFragment : Fragment() {
    private lateinit var binding: FragmentListOfBooksBinding
    private lateinit var viewModel: ListOfBooksViewModel
    private var authorId: Long = 0
    private var title: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val args = this.arguments
        authorId = args?.getLong(AUTHOR_ID) as Long
        title = args.getString(TITLE) as String
        binding = FragmentListOfBooksBinding.inflate(inflater)
        viewModel = (activity as MainActivity).getListOfBooksViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.liveDataBooks.observe(
            this.viewLifecycleOwner, this::setState
        )
        viewModel.liveDataIsLoading.observe(
            this.viewLifecycleOwner, this::setView
        )
        viewModel.loadBooksByAuthorId(authorId)

        setTitle()
        binding.ivBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    //TODO обработка ошибки загрузки книг
    private fun setState(state: ListOfBooksState) {
        when (state) {
            is ListOfBooksState.SuccessState -> loadData(state.books)
            is ListOfBooksState.ErrorState -> Toast.makeText(
                requireContext(),
                "Books loading error",
                Toast.LENGTH_LONG
            ).show()
            else -> {}
        }
    }

    private fun loadData(books: List<BookList>) {
        setAdapterForRecyclerView(books)
    }

    private fun setTitle() {
        val title = SpannableString("Автор: ")
        title.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.basic_color)),
            0,
            title.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvTitle.text = title
        val searchText = SpannableString(this.title)

        var textColor = 0
        when (requireContext().resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> textColor = R.color.white
            Configuration.UI_MODE_NIGHT_NO -> textColor = R.color.black
            Configuration.UI_MODE_NIGHT_UNDEFINED -> println() //TODO
        }
        searchText.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(requireContext(), textColor)),
            0,
            searchText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvTitle.append(searchText)
    }

    private fun showDialog(book: BookList) {
        val dialog = BookDialog(requireContext(), book)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.book_dialog)
        dialog.show()
        with(dialog.window!!) {
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attributes.windowAnimations = R.style.BookDialogAnimation
            setGravity(Gravity.BOTTOM)
        }
    }

    private fun setAdapterForRecyclerView(books: List<BookList>) {
        val listOfBooksAdapter =
            ListOfBooksAdapter({ item -> onClickMore(item) }, { item -> onClickBook(item) })
        listOfBooksAdapter.submitList(books)
        val layoutManager = GridLayoutManager(requireContext(), 1, RecyclerView.VERTICAL, false)
        binding.rvBooks.layoutManager = layoutManager
        binding.rvBooks.adapter = listOfBooksAdapter
    }

    private fun onClickMore(book: BookList) {
        showDialog(book = book)
    }

    private fun onClickBook(book: BookList) {
        val bundle = Bundle()
        bundle.putLong(BOOK_ID, book.id)
        val fragment = BookFragment()
        fragment.arguments = bundle
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setView(isLoading: Boolean) {
        if (isLoading) {
            binding.rvBooks.visibility = View.GONE
            binding.slBook.startShimmer()
        } else {
            binding.rvBooks.visibility = View.VISIBLE
            binding.slBook.stopShimmer()
            binding.slBook.visibility = View.GONE
        }
    }
}