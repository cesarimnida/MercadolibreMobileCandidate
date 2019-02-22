package com.mercadolibre.mobilecandidate.viewmodel

import android.app.Application
import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import android.view.View
import com.mercadolibre.mobilecandidate.R
import com.mercadolibre.mobilecandidate.RxImmediateSchedulerRule
import com.mercadolibre.mobilecandidate.mockfactory.ProductMockFactory
import com.mercadolibre.mobilecandidate.mockfactory.SellerMockFactory
import com.mercadolibre.mobilecandidate.model.Product
import com.mercadolibre.mobilecandidate.model.ProductDescription
import com.mercadolibre.mobilecandidate.model.Seller
import com.mercadolibre.mobilecandidate.service.SearchService
import io.reactivex.Single
import junit.framework.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 21/02/2019
 * ************************************************************
 */
@RunWith(MockitoJUnitRunner::class)
@Suppress("UNCHECKED_CAST")
class ProductViewModelTest {
    @Mock
    lateinit var application: Application
    @Mock
    var searchService = SearchService()
    lateinit var productViewModel: ProductViewModel
    @get:Rule
    val rule = InstantTaskExecutorRule()
    @get:Rule
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        productViewModel = ProductViewModel(application, searchService)
    }

    @Test
    fun platinumVisibility_platinumPowerSellerStatus() {
        assertEquals(
            View.VISIBLE,
            productViewModel.platinumVisibility(SellerMockFactory.filled().seller_reputation)
        )
    }

    @Test
    fun platinumVisibility_otherPowerSellerStatus() {
        assertEquals(
            View.GONE,
            productViewModel.platinumVisibility(SellerMockFactory.otherPowerSellerStatus().seller_reputation)
        )
    }

    @Test
    fun platinumVisibility_nullPowerSellerStatus() {
        assertEquals(
            View.GONE,
            productViewModel.platinumVisibility(SellerMockFactory.nullPowerSellerStatus().seller_reputation)
        )
    }

    @Test
    fun platinumVisibility_nullSellerReputation() {
        assertEquals(
            View.GONE,
            productViewModel.platinumVisibility(SellerMockFactory.nullSeller().seller_reputation)
        )
    }

    @Test
    fun sellerLocation_filledAddress() {
        assertEquals(
            "city/state",
            productViewModel.sellerLocation(SellerMockFactory.filled().address)
        )
    }

    @Test
    fun sellerLocation_nullAddress() {
        assertEquals(
            "",
            productViewModel.sellerLocation(SellerMockFactory.nullSeller().address)
        )
    }

    @Test
    fun sellerLocation_nullCity() {
        assertEquals(
            "-/state",
            productViewModel.sellerLocation(SellerMockFactory.nullCity().address)
        )
    }

    @Test
    fun sellerLocation_nullState() {
        assertEquals(
            "city/-",
            productViewModel.sellerLocation(SellerMockFactory.nullState().address)
        )
    }

    @Test
    fun productCondition_nullCondition() {
        assertEquals(
            R.string.empty_string,
            productViewModel.productCondition(null)
        )
    }

    @Test
    fun productCondition_newCondition() {
        assertEquals(
            R.string.new_product,
            productViewModel.productCondition("new")
        )
    }

    @Test
    fun productCondition_anyCondition() {
        assertEquals(
            R.string.used_product,
            productViewModel.productCondition("anynonemptystring")
        )
    }

    @Test
    fun fetchDetailedProduct_valuesFilled() {
        val product = ProductMockFactory.filled()
        Mockito.`when`(searchService.fetchDetailedProduct(ArgumentMatchers.anyString())).thenAnswer {
            return@thenAnswer Single.just(product)
        }
        val observer = Mockito.mock(Observer::class.java) as Observer<StatusEvent<Product>>
        productViewModel.detailedProduct.observeForever(observer)
        productViewModel.setProduct(ProductMockFactory.filled())
        productViewModel.fetchDetailedProduct()
        assertNotNull(productViewModel.detailedProduct.value)
        assertEquals(productViewModel.detailedProduct.value!!.status, StatusEvent.Status.SUCCESS)
        assertEquals(productViewModel.detailedProduct.value!!.data, product)
    }

    @Test
    fun fetchDetailedProduct_exceptionThrown() {
        Mockito.`when`(searchService.fetchDetailedProduct(ArgumentMatchers.anyString())).thenAnswer {
            return@thenAnswer Single.error<Exception>(Exception())
        }
        val observer = Mockito.mock(Observer::class.java) as Observer<StatusEvent<Product>>
        productViewModel.detailedProduct.observeForever(observer)
        productViewModel.setProduct(ProductMockFactory.filled())
        productViewModel.fetchDetailedProduct()
        assertNotNull(productViewModel.detailedProduct.value)
        assertEquals(productViewModel.detailedProduct.value!!.status, StatusEvent.Status.ERROR)
        assertNull(productViewModel.detailedProduct.value!!.data)
    }

    @Test
    fun fetchProductDescription_valuesFilled() {
        val productDescription = ProductMockFactory.filledProductDescription()
        Mockito.`when`(searchService.fetchProductDescription(ArgumentMatchers.anyString())).thenAnswer {
            return@thenAnswer Single.just(productDescription)
        }
        val observer = Mockito.mock(Observer::class.java) as Observer<StatusEvent<ProductDescription>>
        productViewModel.productDescription.observeForever(observer)
        productViewModel.setProduct(ProductMockFactory.filled())
        productViewModel.fetchProductDescription()
        assertNotNull(productViewModel.productDescription.value)
        assertEquals(productViewModel.productDescription.value!!.status, StatusEvent.Status.SUCCESS)
        assertEquals(productViewModel.productDescription.value!!.data, productDescription)
    }

    @Test
    fun fetchProductDescription_exceptionThrown() {
        Mockito.`when`(searchService.fetchProductDescription(ArgumentMatchers.anyString())).thenAnswer {
            return@thenAnswer Single.error<Exception>(Exception())
        }
        val observer = Mockito.mock(Observer::class.java) as Observer<StatusEvent<ProductDescription>>
        productViewModel.productDescription.observeForever(observer)
        productViewModel.setProduct(ProductMockFactory.filled())
        productViewModel.fetchProductDescription()
        assertNotNull(productViewModel.productDescription.value)
        assertEquals(productViewModel.productDescription.value!!.status, StatusEvent.Status.ERROR)
        assertNull(productViewModel.productDescription.value!!.data)
    }

    @Test
    fun fetchSeller_valuesFilled() {
        val seller = SellerMockFactory.filled()
        Mockito.`when`(searchService.fetchSeller(ArgumentMatchers.anyString())).thenAnswer {
            return@thenAnswer Single.just(seller)
        }
        val observer = Mockito.mock(Observer::class.java) as Observer<StatusEvent<Seller>>
        productViewModel.seller.observeForever(observer)
        productViewModel.setProduct(ProductMockFactory.filled())
        productViewModel.fetchSeller()
        assertNotNull(productViewModel.seller.value)
        assertEquals(productViewModel.seller.value!!.status, StatusEvent.Status.SUCCESS)
        assertEquals(productViewModel.seller.value!!.data, seller)
    }

    @Test
    fun fetchSeller_exceptionThrown() {
        Mockito.`when`(searchService.fetchSeller(ArgumentMatchers.anyString())).thenAnswer {
            return@thenAnswer Single.error<Exception>(Exception())
        }
        val observer = Mockito.mock(Observer::class.java) as Observer<StatusEvent<Seller>>
        productViewModel.seller.observeForever(observer)
        productViewModel.setProduct(ProductMockFactory.filled())
        productViewModel.fetchSeller()
        assertNotNull(productViewModel.seller.value)
        assertEquals(productViewModel.seller.value!!.status, StatusEvent.Status.ERROR)
        assertNull(productViewModel.seller.value!!.data)
    }
}