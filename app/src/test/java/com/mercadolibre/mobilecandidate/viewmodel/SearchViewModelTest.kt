package com.mercadolibre.mobilecandidate.viewmodel

import android.app.Application
import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import android.view.View
import com.mercadolibre.mobilecandidate.RxImmediateSchedulerRule
import com.mercadolibre.mobilecandidate.mockfactory.InstallmentMockFactory
import com.mercadolibre.mobilecandidate.mockfactory.ProductMockFactory
import com.mercadolibre.mobilecandidate.mockfactory.ShippingMockFactory
import com.mercadolibre.mobilecandidate.model.SearchResult
import com.mercadolibre.mobilecandidate.service.SearchService
import io.reactivex.Single
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
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

    @Suppress("UNCHECKED_CAST")
    @Test
    fun queryProducts_valuesFilled() {
        val products = ProductMockFactory.simpleList()
        Mockito.`when`(searchService.queryProducts("chromecast", 0)).thenAnswer {
            return@thenAnswer Single.just(
                SearchResult(
                    "chromecast",
                    ProductMockFactory.simpleList(),
                    SearchResult.Paging(10, 0, 50, 10)
                )
            )
        }
        val observer = Mockito.mock(Observer::class.java) as Observer<SearchResult>
        searchViewModel.searchResult.observeForever(observer)
        searchViewModel.fetchProducts("chromecast")
        assertNotNull(searchViewModel.searchResult.value)
        assertEquals(searchViewModel.products.value, products)
    }
}