package com.github.ggaier.jkmovie.ui.movies

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.ggaier.jkmovie.R
import com.github.ggaier.jkmovie.data.vo.Video
import com.github.ggaier.jkmovie.di.Injections
import com.github.ggaier.jkmovie.ui.widget.SpacestemDecoration
import com.github.ggaier.jkmovie.util.load
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_movies.*
import kotlinx.android.synthetic.main.list_item_movie_1.view.*
import org.jetbrains.anko.dip

class MoviesActivity : AppCompatActivity(), MoviesView {

    lateinit var mAdapter: MoviesAdapter
    lateinit var mPresenter: MoviesPresenterIn

    override fun setProgressIndicator(active: Boolean) {

    }

    override fun showPopularMovies(movies: List<Video>) {
        Logger.d("movies $movies")
        mAdapter.add(movies)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)

        mPresenter = Injections.getMoviesPresenter(this)

        mAdapter = MoviesAdapter(this)
        recycler_view.adapter = mAdapter
        recycler_view.layoutManager = GridLayoutManager(this, 2)
        recycler_view.addItemDecoration(SpacestemDecoration(dip(4)))
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        mPresenter.init(page = 1)
    }


    class MoviesAdapter(
            val activity: MoviesActivity,
            val mMovies: MutableList<Video> = mutableListOf()) : Adapter<MoviesAdapter.MovieViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MovieViewHolder {
            return MovieViewHolder(
                    LayoutInflater.from(parent?.context).inflate(R.layout.list_item_movie_1,
                            parent, false))
        }

        override fun getItemCount(): Int {
            return mMovies.size
        }

        override fun onBindViewHolder(holder: MovieViewHolder?, position: Int) {
            val movie = mMovies[position]
            holder?.itemView?.poster?.load(activity, movie.realBackdropPath)
            holder?.itemView?.title?.text = movie.title
        }


        class MovieViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)

        fun add(movies: List<Video>) {
            val from = mMovies.size
            mMovies.addAll(movies)
            notifyItemRangeChanged(from, mMovies.size)
        }
    }


}
