package com.mercadolibre.mobilecandidate.view.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.mercadolibre.mobilecandidate.R
import com.mercadolibre.mobilecandidate.model.SearchResult
import com.mercadolibre.mobilecandidate.viewmodel.SearchViewModel
import com.mercadolibre.mobilecandidate.viewmodel.StatusEvent
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_search_bar.*

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 19/02/2019
 * ************************************************************
 */
class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViewModel()
        observeSearchResult()
        setSearchListener()
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
    }

    private fun observeSearchResult() {
        viewModel.searchResult.observe(this, Observer { searchResult -> handleSearchResult(searchResult) })
    }

    private fun handleSearchResult(searchResult: StatusEvent<SearchResult>?) {
        if (searchResult == null) return
        when (searchResult.status) {
            StatusEvent.Status.SUCCESS -> {
                viewModel.removeSearchResult()
                SearchResultActivity.startActivity(this, searchResult.data!!)
                stopLoading()
            }
            StatusEvent.Status.LOADING -> {
                startLoading()
            }

            StatusEvent.Status.ERROR -> {
                Toast.makeText(this, R.string.status_error, Toast.LENGTH_SHORT).show()
                stopLoading()
            }
        }
    }

    private fun startLoading() {
        iv_search_bar.isEnabled = false
        pb_main.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        Handler().postDelayed({
            iv_search_bar.isEnabled = true
            pb_main.visibility = View.GONE
        }, 1000)
    }

    private fun setSearchListener() {
        iv_search_bar.setOnClickListener { viewModel.fetchProducts(et_search_bar.text.toString()) }
    }
}
