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

    class DummyItem(
        id: String,
        val content: String,
        val details: String,
        val vendeur: String,
        val contact: String,
        val price : Int,
        val images: MutableList<Bitmap>,
        val address: String) {
        val id = if (id == "-1") (ITEMS.size +1 ).toString() else id

        override fun toString(): String = details
    }
}
