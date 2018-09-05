package com.gutoomota.cuboschallenge

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log

import com.gutoomota.cuboschallenge.api.TmdbApi
import com.gutoomota.cuboschallenge.base.MovieListReceiver
import com.gutoomota.cuboschallenge.dao.MovieDao
import com.gutoomota.cuboschallenge.data.Cache
import com.gutoomota.cuboschallenge.model.MoviesResponse
import com.gutoomota.cuboschallenge.util.MovieImageUrlBuilder
import com.gutoomota.cuboschallenge.util.NetworkObserver

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class Presenter : Application() {

    private var movieListReceiver: MovieListReceiver? = null

    private var api: TmdbApi? = null

    var movieDao: MovieDao? = null

    override fun onCreate() {
        super.onCreate()
        initSingletonClasses()

        api = Retrofit.Builder()
                .baseUrl(TmdbApi.URL)
                .client(OkHttpClient.Builder().build())
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(TmdbApi::class.java)

        movieDao = MovieDao(this)
    }

    private fun initSingletonClasses() {
        val url = getString(R.string.url)
        val backdropUrl = getString(R.string.backdropurl)
        val posterUrl = getString(R.string.posterurl)
        val defaultLanguage = getString(R.string.default_language)
        val defaultRegion = getString(R.string.default_region)

        //apiKey is Distributed on string_array to make it harder to reassemble in case of a reverse engineering on the .apk file
        val apiKey = resources.getStringArray(R.array.api_key)[6] +
                resources.getStringArray(R.array.api_key)[2] +
                resources.getStringArray(R.array.api_key)[7] +
                resources.getStringArray(R.array.api_key)[1] +
                resources.getStringArray(R.array.api_key)[4]

        val genres = resources.getIntArray(R.array.genreArgs)

        Cache.instance.init(url, defaultLanguage, defaultRegion, apiKey, genres)
        MovieImageUrlBuilder.instance.init(backdropUrl, posterUrl)
    }

    fun setMovieListReceiver(movieListReceiver: MovieListReceiver) {
        this.movieListReceiver = movieListReceiver
    }

    @SuppressLint("CheckResult")
    fun getInitialData(requestKey: String) {
        if (NetworkObserver.getInstance().isNetworkAvailable(this)) {
            movieListReceiver!!.hideLog()

            getMovies(1, requestKey)
        } else
            movieListReceiver!!.displayLog(resources.getStringArray(R.array.warning)[0])
    }

    @SuppressLint("CheckResult")
    fun getMovies(page: Int?, requestKey: String) {

        if (NetworkObserver.getInstance().isNetworkAvailable(this)) {
            movieListReceiver!!.hideLog()
            try {
                api?.topRated(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, java.lang.Long.valueOf(page!!.toLong()), TmdbApi.DEFAULT_REGION)
                        ?.subscribeOn(Schedulers.io())
                        ?.observeOn(AndroidSchedulers.mainThread())
                        ?.onErrorReturn { retrofitErrorHandler() }
                        ?.subscribe { response -> returnDataToList(response, requestKey) }

            } catch (e: Exception) {
                Log.d("RetrofitError", e.toString())
            }

        } else
            movieListReceiver!!.displayLog(resources.getStringArray(R.array.warning)[0])
    }

    @SuppressLint("CheckResult")
    fun getMovieByQuery(query: String, page: Int?, requestKey: String) {

        if (NetworkObserver.getInstance().isNetworkAvailable(this)) {
            movieListReceiver!!.hideLog()
            try {
                api?.searchMovies(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, query, java.lang.Long.valueOf(page!!.toLong()), TmdbApi.DEFAULT_REGION)
                        ?.subscribeOn(Schedulers.io())
                        ?.observeOn(AndroidSchedulers.mainThread())
                        ?.onErrorReturn { retrofitErrorHandler() }
                        ?.subscribe { response -> returnDataToList(response, requestKey) }
            } catch (e: Exception) {
                Log.d("RetrofitError", e.toString())
            }

        } else
            movieListReceiver!!.displayLog(resources.getStringArray(R.array.warning)[0])
    }

    private fun retrofitErrorHandler() : MoviesResponse {
        Log.d("MAMM", "called by retrofit")
        movieListReceiver!!.displayLog(resources.getStringArray(R.array.warning)[0])
        return MoviesResponse()
    }

    private fun returnDataToList(response: MoviesResponse, requestKey: String) {

        if (response.results != null) {
            for (movie in response.results!!)
                if (movieDao?.getRegister(movie.id) != null)
                    movie.favorite = true

            movieListReceiver!!.updateList(response.results!!, response.totalPages, response.page, requestKey)
        }
    }
}
