package com.gutoomota.cuboschallenge.view.activities

import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View

import com.gutoomota.cuboschallenge.Presenter
import com.gutoomota.cuboschallenge.R
import com.gutoomota.cuboschallenge.model.Movie
import com.gutoomota.cuboschallenge.view.adapters.MovieAdapter
import kotlinx.android.synthetic.main.movielist_fragment.*

import java.util.Objects

class FavoriteListActivity : AppCompatActivity() {

    private var presenter: Presenter? = null


    private val movies: MutableList<Movie>? by lazy { mutableListOf<Movie>() }
    private var movieAdapter: MovieAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.movielist_fragment)

        Objects.requireNonNull<ActionBar>(supportActionBar).setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        presenter = application as Presenter

        progressBar!!.visibility = View.GONE
        movieAdapter = MovieAdapter(this.movies, this)

        val numberOfColumns = 2
        recyclerView?.layoutManager = GridLayoutManager(this, numberOfColumns)
        recyclerView!!.adapter = movieAdapter

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.favorite_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.action_favorites -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onResume() {
        super.onResume()

        movies?.clear()
        movies?.addAll(presenter!!.movieDao!!.allRegisters)
        movieAdapter?.notifyDataSetChanged()
    }
}
