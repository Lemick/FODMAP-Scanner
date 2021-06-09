package com.lemick.fodmapscanner.view

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lemick.fodmapscanner.model.utils.Event
import com.lemick.fodmapscanner.model.api.IOpenFoodFactsClient
import com.lemick.fodmapscanner.model.api.model.Product
import com.lemick.fodmapscanner.model.entity.AnalyzedProduct
import com.lemick.fodmapscanner.model.entity.AnalyzedProductDao
import com.lemick.fodmapscanner.utils.getViewModelScope
import kotlinx.coroutines.launch

class ProductScannerViewModel(private val openFoodFactsClient: IOpenFoodFactsClient,
                              private val analyzedProductDao: AnalyzedProductDao,) : ViewModel() {

    private val _productState = MutableLiveData<Event<Product?>>()
    val productState: LiveData<Event<Product?>>
        get() = _productState


    fun fetchProduct(barcode: String) {
        viewModelScope.launch() {
            try {
                val productResult = openFoodFactsClient.findProduct(barcode)
                _productState.value = Event(productResult.product)
            } catch (e: Throwable) {
                Log.e("APP", "Error when retrieving the product infos", e)
                _productState.value = Event(null)
            }
        }
    }

    fun persistAnalyzedProduct(product: Product) {
        viewModelScope.launch() {
            val analyzedProduct = AnalyzedProduct(productBarcode = product.id, productName = product.productName, thumbnailUrl = product.imageThumbUrl)
            analyzedProductDao.insert(analyzedProduct)
        }
    }
}