package com.mercadolibre.mobilecandidate.view.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.mercadolibre.mobilecandidate.R
import com.mercadolibre.mobilecandidate.model.Product
import com.mercadolibre.mobilecandidate.model.SearchResult
import com.mercadolibre.mobilecandidate.view.adapter.ProductAdapter
import com.mercadolibre.mobilecandidate.viewmodel.SearchViewModel
import com.mercadolibre.mobilecandidate.viewmodel.StatusEvent
import kotlinx.android.synthetic.main.activity_search_result.*
import kotlinx.android.synthetic.main.content_paging.*
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
        setPagingButtonsListener()
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
        viewModel.products.observe(this, Observer { products -> handleProducts(products) })
    }

    private fun handleProducts(products: ArrayList<Product>?) {
        if (products == null || products.isEmpty()) {
            cv_empty_search_list_search_result.visibility = View.VISIBLE
            rv_search_result.visibility = View.GONE
            return
        }
        cv_empty_search_list_search_result.visibility = View.GONE
        rv_search_result.visibility = View.VISIBLE
        adapter.updateList(products)
    }

    private fun smoothScrollToTop(): RecyclerView.SmoothScroller? {
        val smoothScroller = object : LinearSmoothScroller(this) {
            override fun getVerticalSnapPreference(): Int {
                return LinearSmoothScroller.SNAP_TO_START
            }
        }
        smoothScroller.targetPosition = 0
        return smoothScroller
    }

    private fun observeSearchResult() {
        viewModel.searchResult.observe(this, Observer { searchResult -> handleSearchResult(searchResult) })
    }

    private fun handleSearchResult(searchResult: StatusEvent<SearchResult>?) {
        if (searchResult == null) return
        when (searchResult.status) {
            StatusEvent.Status.SUCCESS -> {
                et_search_bar.setText(searchResult.data!!.query)
                et_search_bar.setSelection(searchResult.data.query.length)
                setupPaging(searchResult.data.paging)
                stopLoading()
            }
            StatusEvent.Status.LOADING -> {
                startLoading()
                rv_search_result.layoutManager!!.startSmoothScroll(smoothScrollToTop())
            }

            StatusEvent.Status.ERROR -> {
                Toast.makeText(this, R.string.status_error, Toast.LENGTH_SHORT).show()
                stopLoading()
            }
        }
    }

    private fun startLoading() {
        cv_empty_search_list_search_result.visibility = View.GONE
        pb_search_result.visibility = View.VISIBLE
        rv_search_result.visibility = View.GONE
        iv_search_bar.isEnabled = false
    }

    private fun stopLoading() {
        pb_search_result.visibility = View.GONE
        iv_search_bar.isEnabled = true
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

    private fun setPagingButtonsListener() {
        bt_previous_paging.setOnClickListener { viewModel.navigateToPreviousPage() }
        bt_next_paging.setOnClickListener { viewModel.navigateToNextPage() }
    }

    private fun setupPaging(paging: SearchResult.Paging) {
        bt_previous_paging.text = viewModel.previousPage(paging)
        bt_previous_paging.visibility = viewModel.previousPageVisibility()
        tv_current_paging.text = viewModel.currentPage(paging)
        bt_next_paging.text = viewModel.nextPage(paging)
        bt_next_paging.visibility = viewModel.nextPageVisibility()
    }
}