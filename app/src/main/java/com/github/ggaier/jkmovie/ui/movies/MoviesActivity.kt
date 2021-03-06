package com.github.ggaier.jkmovie.ui.movies

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.MenuItem
import com.github.ggaier.jkmovie.R
import com.github.ggaier.jkmovie.data.vo.Video
import com.github.ggaier.jkmovie.databinding.ActivityMoviesBinding
import com.github.ggaier.jkmovie.di.Injections
import com.github.ggaier.jkmovie.ui.activity.BaseActivity
import com.github.ggaier.jkmovie.ui.adapters.BaseAdapter
import com.github.ggaier.jkmovie.ui.movieinfo.MovieInfoActivity
import com.github.ggaier.jkmovie.ui.widget.SpacesItemDecoration
import com.github.ggaier.jkmovie.util.load
import kotlinx.android.synthetic.main.activity_movies.*
import kotlinx.android.synthetic.main.list_item_movie_1.view.*
import org.jetbrains.anko.dip

class MoviesActivity : BaseActivity() {

    lateinit var mAdapter: MoviesAdapter
    lateinit var mMoviesModel: MovieListViewModel
    lateinit var mBinding: ActivityMoviesBinding
    private var mStartPage: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,
                R.layout.activity_movies)
        setActivityActionBar(toolbar, homeAsUp = false, title = getString(R
                .string.app_name))
        onCreateToolbarOptionsMenu(R.menu.menu_main)
        mMoviesModel = Injections.getMoviesViewModel(this)
        refresh_layout.setColorSchemeResources(R.color.colorPrimary)
        mBinding.isLoading = true
        initRecyclerView()
    }


    private fun initRecyclerView() {
        mAdapter = MoviesAdapter(this, { adapter, position ->
            MovieInfoActivity.show(this, adapter.mDatas[position])
        })
        mBinding.recyclerView.adapter = mAdapter
        val lm = GridLayoutManager(this, 2)
        recycler_view.layoutManager = lm
        lm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                return 1
            }
        }
        recycler_view.addItemDecoration(SpacesItemDecoration(dip(4)))
        mMoviesModel.setMovieTag(page = mStartPage)
        mAdapter.mOnLoadMoreListener = { mMoviesModel.setMovieTag(page = ++mStartPage) }
        mMoviesModel.getMovies().observe(this,
                Observer<List<Video>?> {
                    mBinding.isLoading = false
                    if (it == null || it.isEmpty()) {
                        return@Observer
                    }
                    if (mStartPage > 1) {
                        mAdapter.loadMoreComplete()
                    }
                    mAdapter.addDatas(it)
                })
    }

    override fun onToolbarMenuSelected(it: MenuItem?): Boolean {
        return super.onToolbarMenuSelected(it)
    }

    class MoviesAdapter(val activity: MoviesActivity,
                        listener: (adapter: BaseAdapter<Video>, position: Int) -> Unit)
        : BaseAdapter<Video>(defaultLayoutId = R.layout.list_item_movie_1,
            mListener = listener) {


        override fun bindDefault(holder: BaseViewHolder?, itemData: Video) {
            holder?.itemView?.poster?.load(activity, itemData.realBackdropPath)
            holder?.itemView?.title?.text = itemData.title
        }
    }


}
