package com.gutoomota.cuboschallenge.dao

import android.content.Context

import com.gutoomota.cuboschallenge.base.DaoAbstract
import com.gutoomota.cuboschallenge.model.Movie

import io.realm.Realm

/**
 * Created by Guto on 03/03/2017.
 */

class MovieDao(context: Context) : DaoAbstract(context) {

    val allRegisters: MutableList<Movie>
        get() {
            val realm = Realm.getInstance(realmConfig)
            val moviesRealm = realm.where(Movie::class.java).findAll()
            val movies = mutableListOf<Movie>()

            for (m in moviesRealm)
                movies.add(m.copy())

            realm.close()
            return movies

        }

    fun getRegister(id: Int): Movie? {
        val realm = Realm.getInstance(realmConfig)

        val movie = realm.where(Movie::class.java).equalTo("id", id).findFirst()

        realm.close()
        return movie
    }

    fun insertRegister(movie: Movie) {
        val realm = Realm.getInstance(realmConfig)

        if (getRegister(movie.id) == null)
            realm.executeTransaction { realm1 -> realm1.copyToRealm(movie) }

        realm.close()
    }

    fun deleteRegister(id: Int) {
        val realm = Realm.getInstance(realmConfig)

        realm.executeTransaction { getRegister(id)!!.deleteFromRealm() }

        realm.close()
    }
}