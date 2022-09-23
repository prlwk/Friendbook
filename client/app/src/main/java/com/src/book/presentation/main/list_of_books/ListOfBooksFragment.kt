package com.src.book.presentation.main.list_of_books

import android.app.Dialog
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
import androidx.core.content.ContextCompat
import com.src.book.R
import com.src.book.databinding.FragmentListOfBooksBinding
import com.src.book.presentation.utils.MarginItemDecoration

class ListOfBooksFragment : Fragment() {
    private lateinit var binding: FragmentListOfBooksBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_of_books, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentListOfBooksBinding.bind(view)
        setTitle()
        //TODO  после создания адаптера проверить, что работает
        binding.rvBooks.addItemDecoration(
            MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.space_5x))
        )
    }

    private fun setTitle() {
        val title = SpannableString("Поиск: ")
        title.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.basic_color)),
            0,
            title.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvTitle.text = title
        //TODO
        val searchText = SpannableString(" Результат поиска")

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

    //TODO по нажатию у книги кнопки "3 точки" вызвать эту функцию
    private fun showDialog() {
        val dialog = Dialog(requireContext())
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
}