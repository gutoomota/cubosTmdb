package com.gutoomota.cuboschallenge.view.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gutoomota.cuboschallenge.R
import com.gutoomota.cuboschallenge.data.Cache
import com.gutoomota.cuboschallenge.model.Movie
import com.gutoomota.cuboschallenge.view.activities.HomeActivity
import com.gutoomota.cuboschallenge.view.adapters.MovieAdapter
import kotlinx.android.synthetic.main.movielist_fragment.*

import java.util.ArrayList

class MovieListFragment : Fragment() {

    private var activity: HomeActivity? = null

    var recyclerView: RecyclerView? = null
    var movies: ArrayList<Movie>? = null
    var movieAdapter: MovieAdapter? = null
    var lastAdded: Int? = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.movielist_fragment, container, false)

        activity = getActivity() as HomeActivity

        val numberOfColumns = 2
        recyclerView = v.findViewById(R.id.recyclerView)
        recyclerView?.layoutManager = GridLayoutManager(getActivity(), numberOfColumns)
        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1)) {

                    activity!!.requestNextPage()
                } else
                    setProgressBarVisible(false)
            }
        })

        return v
    }

    fun setProgressBarVisible(v: Boolean) {
        if (progressBar == null)
            return
        if (v)
            progressBar!!.visibility = View.VISIBLE
        else
            progressBar!!.visibility = View.GONE
    }

    fun updateList() {
        if (movies == null)
            movies = ArrayList()

        val genre = Cache.instance.genre
        val m = Cache.instance.movies
        for (i in lastAdded!! + 1 until m!!.size) {
            m[i].genreIds!!.forEach { id ->
                if (id == genre) {
                    movies!!.add(m[i])
                    lastAdded = i
                }
            }
        }

        if (movieAdapter == null) {
            movieAdapter = MovieAdapter(this.movies, getActivity() as AppCompatActivity)
            recyclerView!!.adapter = movieAdapter
        } else
            movieAdapter!!.notifyDataSetChanged()

        setProgressBarVisible(false)

        if (movies!!.size < 10 && activity!!.totalPages > activity!!.currentPage)
            activity!!.requestNextPage()
    }
}
