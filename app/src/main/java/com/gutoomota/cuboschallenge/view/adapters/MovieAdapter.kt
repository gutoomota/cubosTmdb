package com.gutoomota.cuboschallenge.view.adapters

import android.content.Intent
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gutoomota.cuboschallenge.Controller
import com.gutoomota.cuboschallenge.R
import com.gutoomota.cuboschallenge.model.Movie
import com.gutoomota.cuboschallenge.util.MovieImageUrlBuilder
import com.gutoomota.cuboschallenge.view.activities.DetailsActivity

class MovieAdapter(private val movies: MutableList<Movie>?, private val activity: AppCompatActivity) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    class ViewHolder(itemView: View, private val activity: AppCompatActivity) : RecyclerView.ViewHolder(itemView) {
        private val controller: Controller = activity.applicationContext as Controller

        private val clItem: ConstraintLayout = itemView.findViewById(R.id.clItem)
        private val titleTextView: TextView = itemView.findViewById(R.id.tvTitle)
        private val posterImageView: ImageView = itemView.findViewById(R.id.posterImageView)
        private val ivFavorite: ImageView = itemView.findViewById(R.id.ivFavorite)

        fun bind(movie: Movie) {

            ivFavorite.isActivated = movie.favorite

            ivFavorite.setOnClickListener {
                if (movie.favorite) {
                    controller.movieDao?.deleteRegister(movie.id)
                    movie.favorite = false
                } else {
                    movie.favorite = true
                    controller.movieDao?.insertRegister(movie)
                }

                ivFavorite.isActivated = movie.favorite

            }

            clItem.setOnClickListener {
                val intent = Intent(activity, DetailsActivity::class.java)
                intent.putExtra("movie", movie)

                activity.startActivity(intent)
            }

            titleTextView.text = movie.title

            val posterPath = movie.posterPath
            if (!TextUtils.isEmpty(posterPath)) {
                Glide.with(itemView)
                        .load(MovieImageUrlBuilder.instance.buildPosterUrl(posterPath!!))
                        .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                        .into(posterImageView)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return ViewHolder(view, activity)
    }

    override fun getItemCount(): Int {
        return movies!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movies!![position])
    }
}
