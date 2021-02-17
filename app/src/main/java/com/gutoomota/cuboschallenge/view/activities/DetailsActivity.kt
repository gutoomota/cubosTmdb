package com.gutoomota.cuboschallenge.view.activities

import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gutoomota.cuboschallenge.Presenter
import com.gutoomota.cuboschallenge.R
import com.gutoomota.cuboschallenge.base.UILogHandler
import com.gutoomota.cuboschallenge.model.Movie
import com.gutoomota.cuboschallenge.util.MovieImageUrlBuilder
import com.gutoomota.cuboschallenge.util.NetworkObserver
import kotlinx.android.synthetic.main.details_activity.*

import java.util.Objects

class DetailsActivity : AppCompatActivity(), UILogHandler, View.OnClickListener {

    private var presenter: Presenter? = null

    private var movie: Movie? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.details_activity)

        Objects.requireNonNull<ActionBar>(supportActionBar).setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        presenter = application as Presenter

        val intent = intent
        movie = intent.getSerializableExtra("movie") as Movie

        supportActionBar!!.title = movie!!.title
        tvOverview.text = movie!!.overview

        ivFavorite.isActivated = movie!!.favorite
        ivFavorite.setOnClickListener(this)

        loadImage()
    }

    private fun loadImage() {
        val backdropPath = movie!!.backdropPath
        if (!TextUtils.isEmpty(backdropPath)) {
            Glide.with(this)
                    .load(MovieImageUrlBuilder.instance.buildBackdropUrl(backdropPath!!))
                    .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                    .into(ivBackdropImage)
        }
    }

    override fun displayLog(log: String) {
        tvLog.visibility = View.VISIBLE
        tvLog.text = log
    }

    override fun hideLog() {
        tvLog.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()

        if (!NetworkObserver.getInstance().isNetworkAvailable(presenter!!))
            displayLog(resources.getStringArray(R.array.warning)[0])
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.ivFavorite -> {
                if (movie!!.favorite) {
                    presenter!!.movieDao?.deleteRegister(movie!!.id)
                    movie!!.favorite = false
                } else {
                    movie!!.favorite = true
                    presenter!!.movieDao?.insertRegister(movie!!)
                }

                ivFavorite.isActivated = movie!!.favorite
            }
            else -> {
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
