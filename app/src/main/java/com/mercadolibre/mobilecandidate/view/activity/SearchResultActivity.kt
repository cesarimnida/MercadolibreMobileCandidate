package com.mercadolibre.mobilecandidate.view.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.mercadolibre.mobilecandidate.R
import com.mercadolibre.mobilecandidate.model.SearchResult
import com.mercadolibre.mobilecandidate.view.adapter.ProductAdapter
import com.mercadolibre.mobilecandidate.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.activity_search_result.*
import kotlinx.android.synthetic.main.content_search_bar.*

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 19/02/2019
 * ************************************************************
 */
class SearchResultActivity : AppCompatActivity() {
    companion object {
        private const val SEARCH_RESULT =
            "com.mercadolibre.mobilecandidate.view.activity.SearchResultActivity.SEARCH_RESULT"

        fun startActivity(context: Context, searchResult: SearchResult) {
            val i = Intent(context, SearchResultActivity::class.java)
            val bundle = Bundle()
            bundle.putSerializable(SEARCH_RESULT, searchResult)
            i.putExtras(bundle)
            context.startActivity(i)
        }
    }

    private lateinit var viewModel: SearchViewModel
    private val adapter = ProductAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)
        initViewModel()
        setupRecyclerView()
        setSearchResult()
        observeProducts()
        observeSearchResult()
        setSearchListener()
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
    }

    private fun setSearchResult() {
        val searchResult = intent?.extras?.getSerializable(SEARCH_RESULT) as SearchResult? ?: return
        viewModel.setSearchResult(searchResult)
        intent!!.removeExtra(SEARCH_RESULT)
    }

    private fun observeProducts() {
        viewModel.products.observe(this, Observer { products -> adapter.updateList(products) })
    }

    private fun observeSearchResult() {
        viewModel.searchResult.observe(this, Observer { searchResult -> et_search_bar.setText(searchResult!!.query) })
    }

    private fun setSearchListener() {
        iv_search_bar.setOnClickListener { viewModel.fetchProducts(et_search_bar.text.toString()) }
    }

    private fun setupRecyclerView() {
        rv_search_result.adapter = adapter
        rv_search_result.layoutManager = LinearLayoutManager(this)
        addDividerToRecyclerView()
    }

    private fun addDividerToRecyclerView() {
        val dividerItemDecoration = DividerItemDecoration(
            rv_search_result.context,
            (rv_search_result.layoutManager as LinearLayoutManager).orientation
        )
        rv_search_result.addItemDecoration(dividerItemDecoration)
    }
}