package com.lemick.fodmapscanner.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lemick.fodmapscanner.business.FodmapIngredientAnalyzer
import com.lemick.fodmapscanner.model.api.model.Ingredient
import com.lemick.fodmapscanner.model.api.model.Product
import com.lemick.fodmapscanner.model.entity.AnalyzedProduct
import com.lemick.fodmapscanner.model.entity.AnalyzedProductDao
import com.lemick.fodmapscanner.model.fodmap.AnalyzedIngredient
import com.lemick.fodmapscanner.utils.getViewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ProductAnalysisViewModel(
    private val coroutineScopeProvider: CoroutineScope? = null,
    private val fodmapIngredientAnalyzer: FodmapIngredientAnalyzer
) : ViewModel() {

    private val _analyzedIngredientsState = MutableLiveData<List<AnalyzedIngredient>>()
    val analyzedIngredientsState: LiveData<List<AnalyzedIngredient>>
        get() = _analyzedIngredientsState

    fun startProductAnalysis(product: Product) {
        getViewModelScope(coroutineScopeProvider).launch() {
            _analyzedIngredientsState.value = doIngredientAnalysis(product.ingredients)
        }
    }

    private fun doIngredientAnalysis(ingredients: List<Ingredient>): List<AnalyzedIngredient>? {
        return fodmapIngredientAnalyzer.analyzeIngredients(ingredients).sortedWith { a, b ->
            when {
                (a.fodmapEntry == null && b.fodmapEntry == null) -> 0
                (a.fodmapEntry == null) -> -1
                (b.fodmapEntry == null) -> 1
                else -> a.fodmapEntry.fodmap.compareTo(b.fodmapEntry.fodmap)
            }
        }.reversed()
    }
}