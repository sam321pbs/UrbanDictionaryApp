package com.sammengistu.urbandictionaryapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sammengistu.urbandictionaryapp.db.DefinitionDao
import com.sammengistu.urbandictionaryapp.models.Definition
import com.sammengistu.urbandictionaryapp.network.UrbanDictionaryService
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

/**
 * DefinitionsRepository is in charge of getting definitions from either the db or
 * fetching it over the network
 */
class DefinitionsRepository private constructor(
    val service: UrbanDictionaryService,
    val dao: DefinitionDao) {

    fun getDefinitions(term: String) : LiveData<List<Definition>> {
        val liveData = MutableLiveData<List<Definition>>()

        if (term.isBlank()) return liveData

        doAsync {
            // retrieve from db
            val dbDefinitions = dao.findByTerm(term)

            if (dbDefinitions.isNotEmpty()) {
                uiThread { liveData.value = dbDefinitions }
            } else {
                val call = service.getDefinitions(term)
                val response = call.execute()

                if (response.isSuccessful) {
                    Log.d(TAG, "Success \n ${response.message()}")
                    val body = response.body()?.list
                    if (body != null) {
                        // Insert to db
                        dao.insert(body)
                    }
                    uiThread { liveData.value = body }
                } else {
                    Log.e(TAG, "Error code: ${response.message()}")
                    uiThread { liveData.value = Collections.emptyList() }
                }
            }
        }
        return liveData
    }

    companion object {
        val TAG = DefinitionsRepository::class.java.simpleName
        // For Singleton instantiation
        @Volatile private var instance: DefinitionsRepository? = null

        fun getInstance(service: UrbanDictionaryService,
                        dao: DefinitionDao) =
            instance ?: synchronized(this) {
                instance ?: DefinitionsRepository(service, dao).also { instance = it }
            }
    }
}