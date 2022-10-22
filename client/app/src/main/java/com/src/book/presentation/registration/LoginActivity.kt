package com.src.book.presentation.registration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.src.book.R
import com.src.book.app.App
import com.src.book.presentation.registration.sign_in.SignInFragment
import com.src.book.presentation.registration.sign_in.viewModel.SignInViewModel
import com.src.book.presentation.registration.sign_in.viewModel.SignInViewModelFactory
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {
    @Inject
    lateinit var signInViewModelFactory: SignInViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (applicationContext as App).appComponent.inject(this)
        setContentView(R.layout.activity_login)
        replaceFragment(SignInFragment())
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun getSignInViewModel():SignInViewModel =
        ViewModelProvider(this, signInViewModelFactory)[SignInViewModel::class.java]
}