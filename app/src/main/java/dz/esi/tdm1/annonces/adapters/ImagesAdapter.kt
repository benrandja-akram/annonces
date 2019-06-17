package dz.esi.tdm1.annonces.adapters

import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import dz.esi.tdm1.annonces.R
import kotlinx.android.synthetic.main.annonce_image.view.*

class ImagesAdapter(private val myDataset: MutableList<Uri>) :
    RecyclerView.Adapter<ImagesAdapter.MyViewHolder>() {
    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesAdapter.MyViewHolder {
        val annonce = LayoutInflater.from(parent.context)
            .inflate(R.layout.annonce_image, parent, false)

        return MyViewHolder(annonce)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide
            .with(holder.view.annonce_image.context)
            .load(myDataset[position])
            .fitCenter()
            .into(holder.view.annonce_image)
//        holder.image.setImageURI(myDataset[position])
    }

    override fun getItemCount() = myDataset.size
}