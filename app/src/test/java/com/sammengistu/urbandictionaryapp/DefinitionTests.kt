package com.sammengistu.urbandictionaryapp

import com.sammengistu.urbandictionaryapp.models.DefinitionModel
import org.junit.Assert
import org.junit.Test
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class DefinitionTests {
    @Test
    fun sortByThumbsUp() {
        val def1 = DefinitionModel("test", "test", 1, 0)
        val def2 = DefinitionModel("test", "test", 2, 0)
        val def3 = DefinitionModel("test", "test", 3, 0)
        val list = listOf(def1, def2, def3)
        Collections.sort(list, DefinitionModel.getThumbsUpComparator())

        Assert.assertEquals(list[0].thumbsUp, 3)
        Assert.assertEquals(list[1].thumbsUp, 2)
        Assert.assertEquals(list[2].thumbsUp, 1)
    }

    @Test
    fun sortByThumbsDown() {
        val def1 = DefinitionModel("test", "test", 0, 1)
        val def2 = DefinitionModel("test", "test", 0, 2)
        val def3 = DefinitionModel("test", "test", 0, 3)
        val list = listOf(def1, def2, def3)
        Collections.sort(list, DefinitionModel.getThumbsDownComparator())

        Assert.assertEquals(list[0].thumbsDown, 3)
        Assert.assertEquals(list[1].thumbsDown, 2)
        Assert.assertEquals(list[2].thumbsDown, 1)
    }
}
