package com.github.ggaier.jkmovie.ui.movies

import android.arch.lifecycle.LifecycleActivity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.github.ggaier.jkmovie.R
import com.github.ggaier.jkmovie.data.vo.Video
import com.github.ggaier.jkmovie.databinding.ActivityMoviesBinding
import com.github.ggaier.jkmovie.ui.adapters.BaseAdapter
import com.github.ggaier.jkmovie.ui.widget.SpacestemDecoration
import com.github.ggaier.jkmovie.util.load
import com.github.ggaier.jkmovie.viewmodel.MovieListModel
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_movies.*
import kotlinx.android.synthetic.main.list_item_movie_1.view.*
import org.jetbrains.anko.dip

class MoviesActivity : LifecycleActivity(), MoviesView {

    lateinit var mAdapter: MoviesAdapter
    lateinit var mMoviesModel: MovieListModel
    lateinit var mBinding: ActivityMoviesBinding
    private var mStartPage: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_movies)
        refresh_layout.setColorSchemeResources(R.color.colorPrimary)

        mMoviesModel = ViewModelProviders.of(this).get(MovieListModel::class.java)
        mBinding.isLoading = true
        initRecyclerView()
    }


    private fun initRecyclerView() {
        mAdapter = MoviesAdapter(this)
        mBinding.recyclerView.adapter = mAdapter
        recycler_view.layoutManager = GridLayoutManager(this, 1) as RecyclerView.LayoutManager
        recycler_view.addItemDecoration(SpacestemDecoration(dip(4)))
        mMoviesModel.setMovieTag(page = mStartPage)
        mAdapter.mOnLoadMoreListener = { mMoviesModel.setMovieTag(page = ++mStartPage) }
        mMoviesModel.getMovies().observe(this,
                Observer<List<Video>> {
                    Logger.d("videos are $it , ${it!!::class.java}")
                    if (mStartPage > 1) {
                        mAdapter.loadMoreComplete()
                    }
                    mBinding.isLoading = it == null
                    mAdapter.addDatas(it)
                })
    }

    class MoviesAdapter(val activity: MoviesActivity,
                        layoutResId: Int = R.layout.list_item_movie_1) :
            BaseAdapter<Video>(defaultLayoutId = layoutResId) {

        override fun bindDefault(holder: BaseViewHolder?, itemData: Video) {
            holder?.itemView?.poster?.load(activity, itemData.realBackdropPath)
            holder?.itemView?.title?.text = itemData.title
        }
    }


}
