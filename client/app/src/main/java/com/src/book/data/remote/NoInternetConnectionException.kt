package com.src.book.data.remote

import java.io.IOException

class NoInternetConnectionException : IOException() {
    override val message: String = "No internet connection"
}