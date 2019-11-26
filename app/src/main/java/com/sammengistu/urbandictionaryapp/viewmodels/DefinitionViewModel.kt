package com.sammengistu.urbandictionaryapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.sammengistu.urbandictionaryapp.DefinitionsRepository
import com.sammengistu.urbandictionaryapp.models.Definition

/**
 * DefinitionViewModel will work with definitionsRepository to get definitions from the repository
 */
class DefinitionViewModel(private val repository: DefinitionsRepository) : ViewModel() {
    private val _term = MutableLiveData<String>()
    val term: LiveData<String>
        get() = _term

    val definitionModels: LiveData<List<Definition>> = Transformations
        .switchMap(_term) { term ->
            if (term == null) {
                AbsentLiveDate.create()
            } else {
                repository.getDefinitions(term)
            }
        }

    fun setTerm(term: String) {
        if (!_term.value.equals(term)) {
            _term.value = term
        }
    }
}