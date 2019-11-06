package com.sammengistu.urbandictionaryapp

import android.content.Context
import android.content.SharedPreferences

/**
 * Will be in charge of storing last response and last word to the shared preference
 */
class SharedPrefCache(context: Context) {

    var sharedPref: SharedPreferences =
        context.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE)

    fun getLastWord() : String? {
        return sharedPref.getString(LAST_WORD, null)
    }

    fun getLastResponse() : String? {
        return sharedPref.getString(LAST_RESPONSE, null)
    }

    fun setLastWord(word: String) {
        sharedPref.edit()
            .putString(LAST_WORD, word)
            .commit()
    }

    fun setLastResponse(response: String) {
        sharedPref.edit()
            .putString(LAST_RESPONSE, response)
            .commit()
    }

    companion object {
        private const val SHARED_PREF_KEY = "urban_shared_pref_key"
        private const val LAST_WORD = "last_word"
        private const val LAST_RESPONSE = "last_response"


    }
}