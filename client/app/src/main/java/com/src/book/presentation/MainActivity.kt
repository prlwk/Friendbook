package com.src.book.presentation

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.src.book.R
import com.src.book.app.App
import com.src.book.databinding.ActivityMainBinding
import com.src.book.presentation.author.list_of_books.viewModel.ListOfBooksViewModel
import com.src.book.presentation.author.list_of_books.viewModel.ListOfBooksViewModelFactory
import com.src.book.presentation.author.main_page.viewModel.AuthorViewModel
import com.src.book.presentation.author.main_page.viewModel.AuthorViewModelFactory
import com.src.book.presentation.book.main_page.viewModel.BookViewModel
import com.src.book.presentation.book.main_page.viewModel.BookViewModelFactory
import com.src.book.presentation.friends.add_friends.viewModel.AddFriendsViewModel
import com.src.book.presentation.friends.add_friends.viewModel.AddFriendsViewModelFactory
import com.src.book.presentation.friends.friends_list.FriendsListFragment
import com.src.book.presentation.friends.friends_list.viewModel.FriendsListViewModel
import com.src.book.presentation.friends.friends_list.viewModel.FriendsListViewModelFactory
import com.src.book.presentation.friends.friends_requests.viewModel.RequestsFriendsViewModel
import com.src.book.presentation.friends.friends_requests.viewModel.RequestsFriendsViewModelFactory
import com.src.book.presentation.profile.my_profile.viewModel.MyProfileViewModel
import com.src.book.presentation.profile.my_profile.viewModel.MyProfileViewModelFactory
import com.src.book.presentation.profile.settings.viewModel.SettingsViewModel
import com.src.book.presentation.profile.settings.viewModel.SettingsViewModelFactory
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var authorViewModelFactory: AuthorViewModelFactory

    @Inject
    lateinit var listOfBooksViewModelFactory: ListOfBooksViewModelFactory

    @Inject
    lateinit var bookViewModelFactory: BookViewModelFactory

    @Inject
    lateinit var settingsViewModelFactory: SettingsViewModelFactory

    @Inject
    lateinit var addFriendsViewModelFactory: AddFriendsViewModelFactory

    @Inject
    lateinit var requestsFriendsViewModelFactory: RequestsFriendsViewModelFactory

    @Inject
    lateinit var friendsListViewModelFactory: FriendsListViewModelFactory

    @Inject
    lateinit var myProfileViewModelFactory: MyProfileViewModelFactory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (applicationContext as App).appComponent.inject(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)
        //TODO
        replaceFragment(FriendsListFragment())
        //TODO
        binding.bottomNavigation.setOnItemReselectedListener {
//            when (it.itemId) {
//                R.id.friends ->
//                R.id.home ->
//                R.id.personal_account ->
//            }
        }
        ///   startActivity(Intent(this, LoginActivity::class.java))
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun getAuthorViewModel(): AuthorViewModel =
        ViewModelProvider(this, authorViewModelFactory)[AuthorViewModel::class.java]

    fun getListOfBooksViewModel(): ListOfBooksViewModel =
        ViewModelProvider(this, listOfBooksViewModelFactory)[ListOfBooksViewModel::class.java]

    fun getBookViewModel(): BookViewModel =
        ViewModelProvider(this, bookViewModelFactory)[BookViewModel::class.java]

    fun getSettingsViewModel(): SettingsViewModel =
        ViewModelProvider(this, settingsViewModelFactory)[SettingsViewModel::class.java]

    fun getAddFriendsViewModel(): AddFriendsViewModel =
        ViewModelProvider(this, addFriendsViewModelFactory)[AddFriendsViewModel::class.java]

    fun getRequestsFriendsViewModel(): RequestsFriendsViewModel =
        ViewModelProvider(
            this,
            requestsFriendsViewModelFactory
        )[RequestsFriendsViewModel::class.java]

    fun getFriendsListViewModel(): FriendsListViewModel =
        ViewModelProvider(this, friendsListViewModelFactory)[FriendsListViewModel::class.java]

    fun getMyProfileViewModel(): MyProfileViewModel =
        ViewModelProvider(this, myProfileViewModelFactory)[MyProfileViewModel::class.java]

    @SuppressLint("ShowToast")
    fun showSnackBar() {
        val customView = layoutInflater.inflate(
            R.layout.error_toast,
            findViewById(R.id.cl_toast)
        )
        val snackBar = Snackbar.make(
            findViewById(R.id.fragment_container),
            "",
            Snackbar.LENGTH_LONG
        )
        snackBar.view.setBackgroundColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.transparent
            )
        )
        val layout = snackBar.view as Snackbar.SnackbarLayout
        snackBar.anchorView = findViewById(R.id.bottom_navigation)
        layout.addView(customView)
        snackBar.show()
    }
}