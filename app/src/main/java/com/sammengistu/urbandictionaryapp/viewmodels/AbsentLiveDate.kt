package com.sammengistu.urbandictionaryapp.viewmodels

import androidx.lifecycle.LiveData

class AbsentLiveDate<T : Any?> private constructor() : LiveData<T>() {

    init {
        postValue(null)
    }

    companion object {
        fun <T> create() : LiveData<T> {
            return AbsentLiveDate<T>()
        }
    }
}