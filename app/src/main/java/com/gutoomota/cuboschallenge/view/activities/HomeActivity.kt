package com.gutoomota.cuboschallenge.view.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.support.v7.widget.SearchView
import android.widget.Toast

import com.gutoomota.cuboschallenge.Presenter
import com.gutoomota.cuboschallenge.R
import com.gutoomota.cuboschallenge.base.MovieListReceiver
import com.gutoomota.cuboschallenge.data.Cache
import com.gutoomota.cuboschallenge.model.Movie
import com.gutoomota.cuboschallenge.util.RequestKeyBuilder
import com.gutoomota.cuboschallenge.view.adapters.ViewPagerAdapter
import com.gutoomota.cuboschallenge.view.fragments.MovieListFragment
import kotlinx.android.synthetic.main.home_activity.*


import java.util.ArrayList

class HomeActivity : AppCompatActivity(), MovieListReceiver, TextWatcher {

    private var presenter: Presenter? = null

    private val tabs: MutableList<MovieListFragment>? by lazy { mutableListOf<MovieListFragment>() }
    private var currentTab: MovieListFragment? = null
    private var searchView: SearchView? = null

    private var requestKey: String? = null
    private val requestKeyBuilder = RequestKeyBuilder()
    private val keyLength = 20
    var totalPages: Int = 1
    var currentPage: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        presenter = application as Presenter
        presenter!!.setMovieListReceiver(this)

        setupViewPager(viewPager)

        tabLayout.setupWithViewPager(viewPager)

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                val i = tab?.position
                currentTab = tabs!![i!!]
                Cache.instance.setGenre(i)

                startSearch(searchView!!.query.toString())
            }
            override fun onTabUnselected(tab:TabLayout.Tab?){
            }
            override fun onTabReselected(tab:TabLayout.Tab?) {
            }
        })

        requestKey = requestKeyBuilder.generateRandomKey(keyLength)
        presenter!!.getInitialData(requestKey!!)
    }

    private fun startSearch(query: String) {
        resetSearch()

        if (query.isEmpty()) {

            currentTab!!.setProgressBarVisible(true)

            requestKey = requestKeyBuilder.generateRandomKey(keyLength)
            presenter!!.getInitialData(requestKey!!)

            hideLog()
        } else
            searchByQuery(query)
    }

    private fun searchByQuery(query: String) {
        requestKey = requestKeyBuilder.generateRandomKey(keyLength)

        if (currentPage < totalPages) {
            currentTab!!.setProgressBarVisible(true)

            presenter!!.getMovieByQuery(query, currentPage + 1, requestKey!!)
        }

    }

    private fun requestMoviePage() {
        if (currentPage < totalPages) {
            currentTab!!.setProgressBarVisible(true)

            presenter!!.getMovies(currentPage + 1, requestKey!!)
        }
    }

    private fun resetSearch() {
        totalPages = 1
        currentPage = 0
        currentTab!!.movieAdapter = null
        currentTab!!.movies = ArrayList()
        currentTab!!.lastAdded = -1
        Cache.instance.movies?.clear()
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val numTabs = 4
        val adapter = ViewPagerAdapter(supportFragmentManager)

        for (i in 0 until numTabs) {
            tabs?.add(MovieListFragment())
        }

        adapter.addFragment(tabs!![0], getString(R.string.action))
        adapter.addFragment(tabs!![1], getString(R.string.drama))
        adapter.addFragment(tabs!![2], getString(R.string.fantasy))
        adapter.addFragment(tabs!![3], getString(R.string.fiction))

        viewPager.adapter = adapter

        currentTab = tabs!![0]
        Cache.instance.setGenre(0)
    }

    fun requestNextPage() {
        var query = ""
        requestKey = requestKeyBuilder.generateRandomKey(keyLength)

        if (searchView != null)
            query = searchView!!.query.toString()

        if (query.isEmpty())
            requestMoviePage()
        else
            searchByQuery(query)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                startSearch(s)
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        when (item.itemId) {
            R.id.action_favorites -> startActivity(Intent(this, FavoriteListActivity::class.java))
            else -> {
            }
        }
        return true
    }

    override fun updateList(movies: List<Movie>, totalPages: Int, currentPage: Int, requestKey: String) {

        if (this.requestKey == requestKey) {
            Cache.instance.movies?.addAll(movies)
            this.totalPages = totalPages
            this.currentPage = currentPage

            currentTab!!.updateList()
        }
    }

    override fun displayLog(log: String) {
        currentTab!!.displayLog(log)
    }

    override fun hideLog() {
        currentTab!!.hideLog()
    }

    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
        startSearch(charSequence.toString())

        Toast.makeText(this, searchView!!.query.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
    override fun afterTextChanged(editable: Editable) {}


    override fun onResume() {
        super.onResume()

        if (searchView != null)
            startSearch(searchView!!.query.toString())
    }
}
