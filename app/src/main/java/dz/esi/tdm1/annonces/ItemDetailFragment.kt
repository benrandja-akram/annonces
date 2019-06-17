package dz.esi.tdm1.annonces

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import dz.esi.tdm1.annonces.model.Content
import kotlinx.android.synthetic.main.activity_item_detail.*
import kotlinx.android.synthetic.main.item_detail.view.*

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [ItemListActivity]
 * in two-pane mode (on tablets) or a [ItemDetailActivity]
 * on handsets.
 */
class ItemDetailFragment : Fragment() {

    /**
     * The dummy content this fragment is presenting.
     */
    private var item: Content.Annonce? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                // Load the dummy content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.
                item = Content.ITEM_MAP[it.getString(ARG_ITEM_ID)]
                activity?.toolbar_layout?.title = item?.content
                activity?.fab?.setOnClickListener { view ->
                    startActivity(Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", item?.contact, null)))
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.item_detail, container, false)

        // Show the dummy content as text in a TextView.
        item?.let {
            rootView.price.text = it.details
        }
        rootView.apply {
            price.text = item?.price.toString()
            description.text = item?.details
            vendeur.text = item?.vendeur
            contact.text = item?.contact
//            images.setImageBitmap(item!!.images[0])
            Glide.with(this!!)
                .load(item?.images!![0])
                .centerCrop()
                .into(images)
            address.text = item?.address


        }
        rootView?.nextImage?.setOnClickListener(this::onClick)
        rootView?.previousImage?.setOnClickListener(this::onClick)
        Log.d("imagesnext", (rootView?.nextImage === null).toString())
        return rootView
    }
    private var imageIndex = 0
    fun onClick(v: View?){
        val size = item?.images!!.size
        when(v?.id){
            view?.nextImage?.id ->   imageIndex++
            view?.previousImage?.id -> imageIndex--
        }
        Glide.with(view!!)
            .load(item?.images?.get(Math.abs((imageIndex) % size)))
            .centerCrop()
            .into(view!!.images)
    }
    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "item_id"
    }
}
