package com.mercadolibre.mobilecandidate.viewmodel

import android.app.Application
import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import android.view.View
import com.mercadolibre.mobilecandidate.RxImmediateSchedulerRule
import com.mercadolibre.mobilecandidate.mockfactory.InstallmentMockFactory
import com.mercadolibre.mobilecandidate.mockfactory.ProductMockFactory
import com.mercadolibre.mobilecandidate.mockfactory.SearchResultMockFactory
import com.mercadolibre.mobilecandidate.mockfactory.ShippingMockFactory
import com.mercadolibre.mobilecandidate.model.Product
import com.mercadolibre.mobilecandidate.model.SearchResult
import com.mercadolibre.mobilecandidate.service.SearchService
import io.reactivex.Single
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner


/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 20/02/2019
 * ************************************************************
 */
@RunWith(MockitoJUnitRunner::class)
@Suppress("UNCHECKED_CAST")
class SearchViewModelTest {
    @Mock
    lateinit var application: Application
    @Mock
    var searchService = SearchService()
    lateinit var searchViewModel: SearchViewModel
    @get:Rule
    val rule = InstantTaskExecutorRule()
    @get:Rule
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        searchViewModel = SearchViewModel(application, searchService)
    }

    @Test
    fun buildInstallmentUnit_successCase() {
        assertEquals("10x R$", SearchViewModel.buildInstallmentUnit(InstallmentMockFactory.filled()))
    }

    @Test
    fun buildInstallmentUnit_allNull() {
        assertEquals("", SearchViewModel.buildInstallmentUnit(InstallmentMockFactory.allNull()))
    }

    @Test
    fun buildInstallmentUnit_quantityNull() {
        Assert.assertEquals("", SearchViewModel.buildInstallmentUnit(InstallmentMockFactory.nullQuantity()))
    }

    @Test
    fun buildInstallmentUnit_unitNull() {
        Assert.assertEquals("", SearchViewModel.buildInstallmentUnit(InstallmentMockFactory.nullUnit()))
    }

    @Test
    fun freeShippingVisibility_trueFreeShipping() {
        assertEquals(View.VISIBLE, SearchViewModel.freeShippingVisibility(ShippingMockFactory.trueFreeShipping()))
    }

    @Test
    fun freeShippingVisibility_falseFreeShipping() {
        assertEquals(View.GONE, SearchViewModel.freeShippingVisibility(ShippingMockFactory.falseFreeShipping()))
    }

    @Test
    fun freeShippingVisibility_nullShipping() {
        assertEquals(View.GONE, SearchViewModel.freeShippingVisibility(ShippingMockFactory.nullShipping()))
    }

    @Test
    fun freeShippingVisibility_nullFreeShipping() {
        assertEquals(View.GONE, SearchViewModel.freeShippingVisibility(ShippingMockFactory.nullFreeShipping()))
    }

    @Test
    fun interestFreeVisibility_zeroRate() {
        assertEquals(View.VISIBLE, SearchViewModel.interestFreeVisibility(InstallmentMockFactory.filled()))
    }

    @Test
    fun interestFreeVisibility_withRate() {
        assertEquals(View.GONE, SearchViewModel.interestFreeVisibility(InstallmentMockFactory.withRate()))
    }

    @Test
    fun setSearchResult_anyValue() {
        val searchResult = SearchResultMockFactory.filled()
        searchViewModel.setSearchResult(searchResult)
        assertEquals(searchResult, searchViewModel.searchResult.value!!.data)
        assertEquals(searchResult.results, searchViewModel.products.value)
    }

    @Test
    fun removeSearchResult_check() {
        val searchResult = SearchResultMockFactory.filled()
        searchViewModel.setSearchResult(searchResult)
        assertEquals(searchResult, searchViewModel.searchResult.value!!.data)
        assertEquals(searchResult.results, searchViewModel.products.value)
        searchViewModel.removeSearchResult()
        assertNull(searchViewModel.searchResult.value)
    }

    @Test
    fun currentPage_check() {
        val searchResult = SearchResultMockFactory.filled()
        searchViewModel.setSearchResult(searchResult)
        assertEquals(searchResult, searchViewModel.searchResult.value!!.data)
        assertEquals(searchResult.results, searchViewModel.products.value)
        assertEquals("1", searchViewModel.currentPage(searchResult.paging))
    }

    @Test
    fun previousPage_check() {
        val searchResult = SearchResultMockFactory.filled()
        searchViewModel.setSearchResult(searchResult)
        assertEquals(searchResult, searchViewModel.searchResult.value!!.data)
        assertEquals(searchResult.results, searchViewModel.products.value)
        assertEquals("0", searchViewModel.previousPage(searchResult.paging))
    }

    @Test
    fun nextPage_check() {
        val searchResult = SearchResultMockFactory.filled()
        searchViewModel.setSearchResult(searchResult)
        assertEquals(searchResult, searchViewModel.searchResult.value!!.data)
        assertEquals(searchResult.results, searchViewModel.products.value)
        assertEquals("2", searchViewModel.nextPage(searchResult.paging))
    }

    @Test
    fun previousPageVisibility_check() {
        val searchResult = SearchResultMockFactory.filled()
        searchViewModel.setSearchResult(searchResult)
        assertEquals(searchResult, searchViewModel.searchResult.value!!.data)
        assertEquals(searchResult.results, searchViewModel.products.value)
        assertEquals(View.INVISIBLE, searchViewModel.previousPageVisibility())
    }

    @Test
    fun nextPageVisibility_check() {
        val searchResult = SearchResultMockFactory.lastPage()
        searchViewModel.setSearchResult(searchResult)
        assertEquals(searchResult, searchViewModel.searchResult.value!!.data)
        assertEquals(searchResult.results, searchViewModel.products.value)
        assertEquals(View.INVISIBLE, searchViewModel.nextPageVisibility())
    }

    @Test
    fun fetchProducts_valuesFilled() {
        val products = ProductMockFactory.simpleList()
        val searchResult = SearchResultMockFactory.filled()
        Mockito.`when`(searchService.queryProducts("chromecast", 0)).thenAnswer {
            return@thenAnswer Single.just(searchResult)
        }
        val searchResultObserver = Mockito.mock(Observer::class.java) as Observer<StatusEvent<SearchResult>>
        val productObserver = Mockito.mock(Observer::class.java) as Observer<ArrayList<Product>>
        searchViewModel.searchResult.observeForever(searchResultObserver)
        searchViewModel.products.observeForever(productObserver)
        searchViewModel.fetchProducts("chromecast")
        assertNotNull(searchViewModel.searchResult.value)
        assertEquals(searchViewModel.searchResult.value!!.status, StatusEvent.Status.SUCCESS)
        assertEquals(searchViewModel.searchResult.value!!.data, searchResult)
        assertEquals(searchViewModel.products.value, products)
    }

    @Test
    fun fetchProducts_exceptionThrown() {
        Mockito.`when`(searchService.queryProducts("chromecast", 0)).thenAnswer {
            return@thenAnswer Single.error<Exception>(Exception())
        }
        val searchResultObserver = Mockito.mock(Observer::class.java) as Observer<StatusEvent<SearchResult>>
        val productObserver = Mockito.mock(Observer::class.java) as Observer<ArrayList<Product>>
        searchViewModel.searchResult.observeForever(searchResultObserver)
        searchViewModel.products.observeForever(productObserver)
        searchViewModel.fetchProducts("chromecast")
        assertNotNull(searchViewModel.searchResult.value)
        assertEquals(searchViewModel.searchResult.value!!.status, StatusEvent.Status.ERROR)
        assertNull(searchViewModel.searchResult.value!!.data)
        assertNull(searchViewModel.products.value)
    }

    @Test
    fun navigateToPreviousPage_valuesFilled() {
        val products = ProductMockFactory.simpleList()
        val secondPage = SearchResultMockFactory.secondPage()
        val thirdPage = SearchResultMockFactory.thirdPage()
        Mockito.`when`(searchService.queryProducts("chromecast", 50)).thenAnswer {
            return@thenAnswer Single.just(secondPage)
        }
        searchViewModel.setSearchResult(thirdPage)
        val searchResultObserver = Mockito.mock(Observer::class.java) as Observer<StatusEvent<SearchResult>>
        val productObserver = Mockito.mock(Observer::class.java) as Observer<ArrayList<Product>>
        searchViewModel.searchResult.observeForever(searchResultObserver)
        searchViewModel.products.observeForever(productObserver)
        searchViewModel.navigateToPreviousPage()
        assertNotNull(searchViewModel.searchResult.value)
        assertEquals(searchViewModel.searchResult.value!!.status, StatusEvent.Status.SUCCESS)
        assertEquals(searchViewModel.searchResult.value!!.data, secondPage)
        assertEquals(searchViewModel.products.value, products)
    }

    @Test
    fun navigateToNextPage_valuesFilled() {
        val products = ProductMockFactory.simpleList()
        val firstPage = SearchResultMockFactory.filled()
        val secondPage = SearchResultMockFactory.secondPage()
        Mockito.`when`(searchService.queryProducts("chromecast", 50)).thenAnswer {
            return@thenAnswer Single.just(secondPage)
        }
        searchViewModel.setSearchResult(firstPage)
        val searchResultObserver = Mockito.mock(Observer::class.java) as Observer<StatusEvent<SearchResult>>
        val productObserver = Mockito.mock(Observer::class.java) as Observer<ArrayList<Product>>
        searchViewModel.searchResult.observeForever(searchResultObserver)
        searchViewModel.products.observeForever(productObserver)
        searchViewModel.navigateToNextPage()
        assertNotNull(searchViewModel.searchResult.value)
        assertEquals(searchViewModel.searchResult.value!!.status, StatusEvent.Status.SUCCESS)
        assertEquals(searchViewModel.searchResult.value!!.data, secondPage)
        assertEquals(searchViewModel.products.value, products)
    }
}