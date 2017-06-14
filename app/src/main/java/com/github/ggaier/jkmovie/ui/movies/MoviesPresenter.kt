package com.github.ggaier.jkmovie.ui.movies

import com.github.ggaier.jkmovie.data.MoviesRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by ggaier
 * jwenbo52@gmail.com
 */
open class MoviesPresenter(val mMoviesView: MoviesView,
                           val mMoviesRepository: MoviesRepository) : MoviesPresenterIn {

    override fun onUnsubscribe() {

    }

    override fun init(tag: String, language: String, page: Int) {
        mMoviesRepository.getPopularMovies(language = language, page = page, region = "US")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mMoviesView.showPopularMovies(it)
                })
    }

}