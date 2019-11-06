package com.sammengistu.urbandictionaryapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sammengistu.urbandictionaryapp.viewmodels.DefinitionViewModel

/**
 * This helps provide view models for the activity or fragment
 */
class DefinitionFactory(
    private val repository: DefinitionsRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        DefinitionViewModel(repository) as T
}