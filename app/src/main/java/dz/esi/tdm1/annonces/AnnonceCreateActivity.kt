package dz.esi.tdm1.annonces

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.myhexaville.smartimagepicker.ImagePicker
import kotlinx.android.synthetic.main.activity_item_create.*
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.design.widget.Snackbar
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import dz.esi.tdm1.annonces.adapters.ImagesAdapter
import dz.esi.tdm1.annonces.model.Content
import kotlinx.android.synthetic.main.stepper.*
import java.lang.Exception

class AnnonceCreateActivity : AppCompatActivity() {
    lateinit var images : ArrayList<Uri>
    lateinit var imagesAdapter : ImagesAdapter
    lateinit var imagePicker: ImagePicker
    val IMAGES_KEY = "images"
    val STEP_KEY = "step"
    var savedStep = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_create)
        if(savedInstanceState != null)
        {
            images = savedInstanceState.getSerializable(IMAGES_KEY) as ArrayList<Uri>
            imagesAdapter = ImagesAdapter(images)
            Log.i(STEP_KEY, savedInstanceState.getInt(STEP_KEY).toString())
            savedStep = savedInstanceState.getInt(STEP_KEY)

        }
        else {
            images = arrayListOf<Uri>()
            imagesAdapter = ImagesAdapter(images)
        }

        images_recycler.apply {
            layoutManager = GridLayoutManager(this@AnnonceCreateActivity, 3)
            adapter = imagesAdapter
        }
        addPhoto.setOnClickListener {
            imagePicker = ImagePicker(this, null) {
                val element = it
                if (images.find { e -> e.toString() == element.toString() } != null) {
                    Toast.makeText(this, "Cet image deja existe", Toast.LENGTH_LONG).show()
                } else {
                    images.add(it)
                    imagesAdapter.notifyDataSetChanged()
                }
            }
            imagePicker.choosePicture(false)
        }

        back.setOnClickListener(this::onStep)
        next.setOnClickListener(this::onStep)

    }

    override fun onStart() {
        super.onStart()
//        for (i in 1..savedStep){
//            Log.i("step i ", i.toString())
//            step(1)
//        }
        supportActionBar?.title = "Créer une annonce"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        imagePicker.handleActivityResult(resultCode, requestCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        imagePicker.handlePermission(requestCode, grantResults)
    }

    var step = 0
    fun step(offset: Int) {
        if(offset == 1 ){
            when (step) {
                0 -> {

                    for(v in arrayOf(name, price, description)){
                        if(v.text.toString().isEmpty()){
                            v.error = "Champ obligatoire"
                            return
                        }
                    }
                    try {
                        val p = price.text.toString().toDouble()
                        if(p<0) throw  Exception()
                    }catch (e: Exception){
                        price.error = "valeur non valide"
                        return
                    }
                }
                1 -> {
                    for(v in arrayOf(vendeur, contact, address)){
                        if(v.text.toString().isEmpty()){
                            v.error = "champ obligatoire"
                            return
                        }
                    }
                }
                2 -> {
                    if(images.isEmpty()){
                        Snackbar.make(
                            scrollView2,
                            "Aucune image sélectionnée",
                            Snackbar.LENGTH_LONG
                        ).setAction("x", this::onCloseSnack).show()
                        return
                    }
                }

            }
        }
        step += offset
        when (step) {
            0 -> {
                stepTitle.text = "Informations generales"
                back.isEnabled = false
                this.render(
                    arrayOf(
                        vendeurContainer, telContainer, addressContainer, addPhoto, images_recycler
                    ),
                    arrayOf(
                        nameContainer, priceContainer,
                        descriptionContainer
                    )
                )
            }
            1 -> {
                stepTitle.text = "Informations du vendeur"
                back.isEnabled = true
                next.text = "Next"
                this.render(
                    arrayOf(
                        nameContainer, priceContainer, descriptionContainer, addPhoto, images_recycler
                    ),
                    arrayOf(
                        vendeurContainer, telContainer, addressContainer
                    )
                )
            }
            2 -> {
                stepTitle.text = "Images"
                next.text = "Save"
                this.render(
                    arrayOf(
                        nameContainer, priceContainer, addressContainer, descriptionContainer, vendeurContainer, telContainer
                    ),
                    arrayOf(
                        addPhoto, images_recycler
                    )
                )
            }
            3 -> {
                this.save()
            }
        }
        stepProgress.progress = step * 100 / 3


    }

    fun onStep(view: View) {
        when (view.id) {
            back.id -> {
                this.step(-1)
            }
            next.id -> {
                this.step(1)
            }
        }
    }

    fun render(gones: Array<View>, visibles: Array<View>) {
        for (view in gones) {
            view.visibility = View.GONE
        }
        for (view in visibles) {
            view.visibility = View.VISIBLE
        }
    }

    fun save() {
        val bitmaps = arrayListOf<Bitmap>()
        for (uri in images) {
            val parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r")
            val fileDescriptor = parcelFileDescriptor.getFileDescriptor()
            val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
            parcelFileDescriptor.close()
            bitmaps.add(image)
        }
        val newAnnonce = Content.Annonce(
            "-1",
            name.text.toString(),
            description.text.toString(),
            vendeur.text.toString(),
            contact.text.toString(),
            Integer.parseInt(price.text.toString()),
            bitmaps,
            address.text.toString()
        )
        Content.ITEMS.add(newAnnonce)
        Content.ITEM_MAP[newAnnonce.id] = newAnnonce
        recyclerList.adapter!!.notifyDataSetChanged()
        Log.i("fdsfsd", Content.ITEMS.size.toString())
        super.onBackPressed()
    }

    fun onCloseSnack(v: View?){

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putSerializable(IMAGES_KEY, images)
        outState?.putInt(STEP_KEY, step)
        super.onSaveInstanceState(outState)
    }
}


