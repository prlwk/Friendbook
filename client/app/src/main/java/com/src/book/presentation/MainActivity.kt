package com.src.book.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.src.book.R
import com.src.book.app.App
import com.src.book.databinding.ActivityMainBinding
import com.src.book.presentation.author.list_of_books.viewModel.ListOfBooksViewModel
import com.src.book.presentation.author.list_of_books.viewModel.ListOfBooksViewModelFactory
import com.src.book.presentation.author.main_page.AuthorFragment
import com.src.book.presentation.author.main_page.viewModel.AuthorViewModel
import com.src.book.presentation.author.main_page.viewModel.AuthorViewModelFactory
import com.src.book.presentation.book.main_page.BookFragment
import com.src.book.presentation.book.main_page.viewModel.BookViewModel
import com.src.book.presentation.book.main_page.viewModel.BookViewModelFactory
import com.src.book.presentation.main.main_page.FilterFragment
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var authorViewModelFactory: AuthorViewModelFactory

    @Inject
    lateinit var listOfBooksViewModelFactory: ListOfBooksViewModelFactory

    @Inject
    lateinit var bookViewModelFactory: BookViewModelFactory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (applicationContext as App).appComponent.inject(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)
        //TODO
        replaceFragment(FilterFragment())
        //TODO
        binding.bottomNavigation.setOnItemReselectedListener {
//            when (it.itemId) {
//                R.id.friends ->
//                R.id.home ->
//                R.id.personal_account ->
//            }
        }
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun getAuthorViewModel(): AuthorViewModel =
        ViewModelProvider(this, authorViewModelFactory).get(AuthorViewModel::class.java)

    fun getListOfBooksViewModel(): ListOfBooksViewModel =
        ViewModelProvider(this, listOfBooksViewModelFactory).get(ListOfBooksViewModel::class.java)

    fun getBookViewModel(): BookViewModel =
        ViewModelProvider(this, bookViewModelFactory).get(BookViewModel::class.java)
}