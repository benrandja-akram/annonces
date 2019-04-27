package dz.esi.tdm1.annonces

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import com.myhexaville.smartimagepicker.ImagePicker
import kotlinx.android.synthetic.main.activity_item_create.*
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.widget.GridLayoutManager
import android.widget.Toast
import dz.esi.tdm1.annonces.dummy.DummyContent

class ItemCreateActivity : AppCompatActivity() {
    var images = mutableListOf<Uri>()
    val myAdapter = MyAdapter(images)
    lateinit var imagePicker: ImagePicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_create)
        images_recycler.apply {
            layoutManager = GridLayoutManager(this@ItemCreateActivity, 3)
            adapter = myAdapter
        }
        addPhoto.setOnClickListener{
            imagePicker = ImagePicker(this, null){
                val element = it
                if(images.find { e -> e.toString() == element.toString()} != null){
                    Toast.makeText(this, "Cet image deja existe", Toast.LENGTH_LONG)
                }
                else {
                    images.add(it)
                    myAdapter.notifyDataSetChanged()
                }
            }
            imagePicker.choosePicture(false)
        }
        save.setOnClickListener{
            val bitmaps = arrayListOf<Bitmap>()
            for (uri in images){
                val parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r")
                val fileDescriptor = parcelFileDescriptor.getFileDescriptor()
                val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
                parcelFileDescriptor.close()
                bitmaps.add(image)
            }
            val newAnnonce = DummyContent.DummyItem(
                Integer(-1).toString(),
                name.text.toString(),
                description.toString(),
                vendeur.text.toString(),
                contact.text.toString(),
                Integer.parseInt(price.text.toString()),
                bitmaps
            )
            DummyContent.ITEMS.add(newAnnonce)
            recyclerList.adapter!!.notifyDataSetChanged()
            super.onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        imagePicker.handleActivityResult(resultCode, requestCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        imagePicker.handlePermission(requestCode, grantResults)
    }
}


class MyAdapter(private val myDataset: MutableList<Uri>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    class MyViewHolder(val image: ImageView) : RecyclerView.ViewHolder(image)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.MyViewHolder {
        val annonce = LayoutInflater.from(parent.context)
            .inflate(R.layout.annonce_image, parent, false) as ImageView

        return MyViewHolder(annonce)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.image.setImageURI( myDataset[position] )
    }

    override fun getItemCount() = myDataset.size
}