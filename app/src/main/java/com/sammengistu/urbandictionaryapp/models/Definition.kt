package com.sammengistu.urbandictionaryapp.models

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = ["defid"], tableName = "definitions")
data class Definition(
    @field:SerializedName("defid")
    val defid: String,
    @field:SerializedName("word")
    val word: String?,
    @field:SerializedName("definition")
    val definition: String?,
    @field:SerializedName("thumbs_up")
    val thumbsUp: Int?,
    @field:SerializedName("thumbs_down")
    val thumbsDown: Int?
) {
    companion object {
        fun getThumbsUpComparator() =
            Comparator<Definition> { d1, d2 ->
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
            Comparator<Definition> { d1, d2 ->
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