package com.sammengistu.urbandictionaryapp.models

import org.json.JSONObject

data class DefinitionModel(
    val word: String?,
    val definition: String?,
    val thumbsUp: Int?,
    val thumbsDown: Int?
) {
    companion object {
        fun fromJson(jsonObject: JSONObject): DefinitionModel {
            val word = jsonObject.getString("word")
            val definition = jsonObject.getString("definition")
            val thumbsUp = jsonObject.getInt("thumbs_up")
            val thumbsDown = jsonObject.getInt("thumbs_down")
            return DefinitionModel(word, definition, thumbsUp, thumbsDown)
        }

        fun getThumbsUpComparator() =
            Comparator<DefinitionModel> { d1, d2 ->
                if (d1 != null && d2 != null && d1.thumbsUp != null && d2.thumbsUp != null) {
                    when {
                        d1.thumbsUp < d2.thumbsUp -> 1
                        d1.thumbsUp > d2.thumbsUp -> -1
                        else -> 0
                    }
                } else {
                    0
                }
            }

        fun getThumbsDownComparator() =
            Comparator<DefinitionModel> { d1, d2 ->
                if (d1 != null && d2 != null && d1.thumbsDown != null && d2.thumbsDown != null) {
                    when {
                        d1.thumbsDown < d2.thumbsDown -> 1
                        d1.thumbsDown > d2.thumbsDown -> -1
                        else -> 0
                    }
                } else {
                    0
                }
            }
    }
}