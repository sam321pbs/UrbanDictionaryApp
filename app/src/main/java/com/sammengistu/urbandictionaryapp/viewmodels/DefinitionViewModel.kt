package com.sammengistu.urbandictionaryapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.sammengistu.urbandictionaryapp.DefinitionsRepository
import com.sammengistu.urbandictionaryapp.models.DefinitionModel

/**
 * DefinitionViewModel will work with definitionsRepository to get definitions from the repository
 */
class DefinitionViewModel(private val repository: DefinitionsRepository) : ViewModel() {
    var definitionModels: LiveData<List<DefinitionModel>> = repository.data

    fun getNewDefinition(word: String) {
        definitionModels = repository.getDefinitions(word)
    }
}