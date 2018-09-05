package com.gutoomota.cuboschallenge.api

import com.gutoomota.cuboschallenge.data.Cache
import com.gutoomota.cuboschallenge.model.MoviesResponse

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbApi {

    @GET("movie/top_rated")
    fun topRated(
            @Query("api_key") apiKey: String,
            @Query("language") language: String,
            @Query("page") page: Long?,
            @Query("region") region: String
    ): Observable<MoviesResponse>

    @GET("search/movie")
    fun searchMovies(
            @Query("api_key") apiKey: String,
            @Query("language") language: String,
            @Query("query") query: String,
            @Query("page") page: Long?,
            @Query("region") region: String
    ): Observable<MoviesResponse>

    companion object {

        val URL = Cache.instance.url
        val DEFAULT_LANGUAGE = Cache.instance.defaultLanguage
        val DEFAULT_REGION = Cache.instance.defaultRegion
        val API_KEY = Cache.instance.apiKey
    }
}
