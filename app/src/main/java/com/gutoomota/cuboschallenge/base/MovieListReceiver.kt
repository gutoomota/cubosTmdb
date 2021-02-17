package com.gutoomota.cuboschallenge.base

import com.gutoomota.cuboschallenge.model.Movie

interface MovieListReceiver : UILogHandler {

    fun updateList(movies: List<Movie>, totalPages: Int, currentPage: Int, requestKey: String)
}
