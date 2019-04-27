package dz.esi.tdm1.annonces.dummy

import android.graphics.Bitmap
import android.net.Uri
import android.os.ParcelFileDescriptor
import java.io.FileDescriptor
import java.util.ArrayList
import java.util.HashMap


object DummyContent {

    /**
     * An array of sample (dummy) annonces.
     */
    val ITEMS: MutableList<DummyItem> = ArrayList()

    /**
     * A map of sample (dummy) items, by ID.
     */
    val ITEM_MAP: MutableMap<String, DummyItem> = HashMap()

    private val COUNT = 1

    init {
        // Add some sample items.
        for (i in 1..COUNT) {
            addItem(createDummyItem(i))
        }
    }

    private fun addItem(item: DummyItem) {
        ITEMS.add(item)
        ITEM_MAP.put(item.id, item)
    }

    private fun createDummyItem(position: Int): DummyItem {
        return DummyItem(position.toString(),
            "Item " + position,
            "Details",
            "contact",
            "contact",
            0,
            arrayListOf()
        )
    }
    /**
     * A dummy item representing a piece of content.
     */
    class DummyItem(
        id: String,
        val content: String,
        val details: String,
        val vendeur: String,
        val contact: String,
        val price : Int,
        val images: MutableList<Bitmap>) {
        val id = if (id == "-1") (ITEMS.size +1 ).toString() else id

        override fun toString(): String = content
    }
}
