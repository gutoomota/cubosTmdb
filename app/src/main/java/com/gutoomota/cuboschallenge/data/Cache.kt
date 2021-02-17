package com.gutoomota.cuboschallenge.data

import com.gutoomota.cuboschallenge.model.Movie

class Cache private constructor() {

    var movies: MutableList<Movie>? = null
    private var genreId: IntArray? = null
    var genre: Int? = 0
    var url = ""
    var defaultLanguage = ""
    var defaultRegion = ""
    var apiKey = ""

    fun init(url: String, default_language: String, default_region: String, api_key: String, genreId: IntArray) {
        this.url = url
        this.defaultLanguage = default_language
        this.defaultRegion = default_region
        this.apiKey = api_key

        movies = mutableListOf()
        this.genreId = genreId
    }

    fun setGenre(i: Int) {
        this.genre = genreId?.get(i)
    }

    private object Holder { val INSTANCE = Cache() }

    companion object {
        val instance: Cache by lazy { Holder.INSTANCE }
    }
}
