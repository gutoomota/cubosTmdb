package com.gutoomota.cuboschallenge.util

import com.gutoomota.cuboschallenge.api.TmdbApi

class MovieImageUrlBuilder private constructor() {

    private var backdropUrl = ""
    private var posterUrl = ""

    fun buildPosterUrl(posterPath: String): String =
            posterUrl + posterPath + "?api_key=" + TmdbApi.API_KEY

    fun buildBackdropUrl(backdropPath: String): String =
            backdropUrl + backdropPath + "?api_key=" + TmdbApi.API_KEY

    fun init(backdropUrl: String, posterUrl: String) {
        this.backdropUrl = backdropUrl
        this.posterUrl = posterUrl
    }

    private object Holder { val INSTANCE = MovieImageUrlBuilder() }

    companion object {
        val instance: MovieImageUrlBuilder by lazy { Holder.INSTANCE }
    }


}
