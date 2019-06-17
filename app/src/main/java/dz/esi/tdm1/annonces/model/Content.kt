package dz.esi.tdm1.annonces.model

import android.graphics.Bitmap
import java.util.*


object Content {

    /**
     * An array of sample (dummy) annonces.
     */
    val ITEMS: MutableList<Annonce> = ArrayList()

    /**
     * A map of sample (dummy) items, by ID.
     */
    val ITEM_MAP: MutableMap<String, Annonce> = HashMap()

    class Annonce(
        id: String,
        val content: String,
        val details: String,
        val vendeur: String,
        val contact: String,
        val price : Int,
        val images: MutableList<Bitmap>,
        val address: String
    )
    {
        val id = if (id == "-1") (ITEMS.size +1 ).toString() else id
        val date = Calendar.getInstance().time
        override fun toString(): String = details
    }
}
