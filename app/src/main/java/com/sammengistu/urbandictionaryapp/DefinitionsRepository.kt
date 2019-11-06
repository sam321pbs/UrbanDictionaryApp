package com.sammengistu.urbandictionaryapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sammengistu.urbandictionaryapp.models.DefinitionModel
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject

/**
 * DefinitionsRepository is in charge of getting definitions from either the SharedPrefCache or
 * fetching it over the network
 */
class DefinitionsRepository private constructor(private val sharedPrefCache: SharedPrefCache) {

    val data = MutableLiveData<List<DefinitionModel>>()

    fun getDefinitions(word: String) : LiveData<List<DefinitionModel>> {
        if (word.isBlank()) return data
        doAsync {
            val definitions : List<DefinitionModel> = getDefinitions(sharedPrefCache, word)
            uiThread {
                data.value = definitions
            }
        }
        return data
    }

    private fun getDefinitions(sharedPrefCache: SharedPrefCache, word: String): List<DefinitionModel> {
        val cacheDefinitions = getLastDefinitionFromCache(sharedPrefCache, word)
        if (!cacheDefinitions.isNullOrEmpty()) {
            Log.d(TAG, "Retrieved from cache")
            // Retrieve from cache
            return cacheDefinitions
        }

        val client = OkHttpClient()
        val request = Request.Builder()
            .addHeader("x-rapidapi-host", "mashape-community-urban-dictionary.p.rapidapi.com")
            .addHeader("x-rapidapi-key", "49af1b6bc4mshf4dfdeaccaba136p12dd6fjsnee63973120c5")
            .url("https://mashape-community-urban-dictionary.p.rapidapi.com/define?term=${word.trim()}")
            .build()
        val response = client.newCall(request).execute()
        val responseBody = response.body?.string()
        if (!responseBody.isNullOrEmpty()) {
            // Update Cache
            sharedPrefCache.setLastWord(word)
            sharedPrefCache.setLastResponse(responseBody)
        }
        Log.d(TAG, responseBody)
        return convertToObjects(responseBody)
    }

    private fun getLastDefinitionFromCache(
        sharedPref: SharedPrefCache,
        word: String
    ): List<DefinitionModel>? {
        val lastWord: String? = sharedPref.getLastWord()
        val lastResponse: String? = sharedPref.getLastResponse()
        if (!lastWord.isNullOrEmpty() && lastWord == word &&
            !lastResponse.isNullOrEmpty()
        ) {
            return convertToObjects(lastResponse)
        }
        return null
    }

    private fun convertToObjects(responseBody: String?): List<DefinitionModel> {
        val list = ArrayList<DefinitionModel>()
        if (!responseBody.isNullOrEmpty()) {
            try {
                val jsonObject = JSONObject(responseBody)
                val array = jsonObject.getJSONArray("list")
                for (pos in 0 until array.length()) {
                    val jsonObject = array.get(pos) as JSONObject
                    list.add(DefinitionModel.fromJson(jsonObject))
                }
            } catch (e: Exception) {
                Log.e(TAG, "Unsuccessful parsing definitions", e)
            }
        } else {
            Log.e(TAG, "Unsuccessful getting definitions")
        }
        return list
    }

    companion object {
        val TAG = DefinitionsRepository::class.java.simpleName
        // For Singleton instantiation
        @Volatile private var instance: DefinitionsRepository? = null

        fun getInstance(sharedPrefCache: SharedPrefCache) =
            instance ?: synchronized(this) {
                instance ?: DefinitionsRepository(sharedPrefCache).also { instance = it }
            }
    }
}