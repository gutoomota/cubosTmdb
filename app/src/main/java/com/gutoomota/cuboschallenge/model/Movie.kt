package com.gutoomota.cuboschallenge.model

import com.squareup.moshi.Json

import java.io.Serializable

import io.realm.RealmObject
import io.realm.annotations.Ignore
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class Movie : RealmObject, Serializable {
    @PrimaryKey
    var id: Int = 0
    var title: String? = null
    var overview: String? = null
    var favorite = false

    @Ignore
    @Json(name = "genre_ids")
    var genreIds: MutableList<Int>? = null
    @Json(name = "poster_path")
    var posterPath: String? = null
    @Json(name = "backdrop_path")
    var backdropPath: String? = null
    @Json(name = "release_date")
    var releaseDate: String? = null

    override fun toString(): String {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\''.toString() +
                ", overview='" + overview + '\''.toString() +
                ", genreIds=" + genreIds +
                ", posterPath='" + posterPath + '\''.toString() +
                ", backdropPath='" + backdropPath + '\''.toString() +
                ", releaseDate='" + releaseDate + '\''.toString() +
                '}'.toString()
    }

    constructor()

    constructor(id: Int, title: String, overview: String, favorite: Boolean,
                posterPath: String, backdropPath: String, releaseDate: String) {
        this.id = id
        this.title = title
        this.overview = overview
        this.favorite = favorite
        this.posterPath = posterPath
        this.backdropPath = backdropPath
        this.releaseDate = releaseDate
    }

    fun copy(): Movie {
        return Movie(id, title!! , overview!!, favorite, posterPath!!, backdropPath!!, releaseDate!!)
    }
}
