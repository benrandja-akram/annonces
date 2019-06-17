package dz.esi.tdm1.annonces

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView

import dz.esi.tdm1.annonces.model.Content
import kotlinx.android.synthetic.main.activity_item_list.*
import kotlinx.android.synthetic.main.item_list_content.view.*
import kotlinx.android.synthetic.main.item_list.*
import android.view.*
import android.widget.*
import com.bumptech.glide.Glide
import com.myhexaville.smartimagepicker.ImagePicker


lateinit var recyclerList: RecyclerView
class ItemListActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private var twoPane: Boolean = false
    lateinit var imagePicker : ImagePicker
    var adapter = SimpleItemRecyclerViewAdapter(this, Content.ITEMS, twoPane)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        setSupportActionBar(toolbar)
        toolbar.title = title

        fab.setOnClickListener {
            val intent =  Intent(this, AnnonceCreateActivity::class.java)
            this.startActivity(intent)
        }

        if (item_detail_container != null) {
            twoPane = true
        }
        setupSpinner()
        recyclerList = item_list
        setupRecyclerView(item_list)


    }

    private fun setupSpinner() {
        val spinner: Spinner = findViewById(R.id.sort_spinner)


        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                when (pos) {
                    0 -> {
                        adapter.nameSort()
                    }
                    1 -> {
                        adapter.dateSort()
                    }
                    else -> {
                        adapter.prixSort()
                    }

                }
                adapter.notifyDataSetChanged()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }

        }

    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        adapter = SimpleItemRecyclerViewAdapter(this, Content.ITEMS, twoPane)

        recyclerList.setAdapter(adapter)
    }

    class SimpleItemRecyclerViewAdapter(
        private val parentActivity: ItemListActivity,
        private var values: MutableList<Content.Annonce>,
        private val twoPane: Boolean
    ) :
        RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {


        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as Content.Annonce
                if (twoPane) {
                    val fragment = ItemDetailFragment().apply {
                        arguments = Bundle().apply {
                            putString(ItemDetailFragment.ARG_ITEM_ID, item.id)
                        }
                    }
                    parentActivity.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.item_detail_container, fragment)
                        .commit()
                } else {
                    val intent = Intent(v.context, ItemDetailActivity::class.java).apply {
                        putExtra(ItemDetailFragment.ARG_ITEM_ID, item.id)
                    }
                    v.context.startActivity(intent)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
//            holder.idView.text = item.id
            holder.contentView.text = item.content
            holder.apply {
                price.text = item.price.toString()
                if(!item.images.isEmpty()){
                    Glide.with(holder.contentView.context)
                        .load(item.images[0])
                        .centerCrop()
                        .into(annonce_image)
                }
            }
            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//            val idView: TextView = view.id_text
            val contentView: TextView = view.content
            val annonce_image = view.annonce_image
            val price = view.price
        }

        fun updateList(newList: MutableList<Content.Annonce>) {
            values = ArrayList<Content.Annonce>()
            values.addAll(newList)
            notifyDataSetChanged()

        }

        fun nameSort() {
            values.sortWith(Comparator {item1, item2 ->
                item1.content.compareTo(item2.content)
            })
        }


        fun dateSort() {
            values.sortWith(Comparator {item1, item2 ->
                val date1 = (item1.date)
                val date2 = (item2.date)
                date1.compareTo(date2)
            })
        }

        fun prixSort() {
            values.sortWith(Comparator {item1, item2 ->
                (item1.price - item2.price).toInt()

            })
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        getMenuInflater().inflate(R.menu.search_menu, menu)
        var menuItem : MenuItem? = menu?.findItem(R.id.search_bar)
        var searchView:SearchView = menuItem?.getActionView() as SearchView
        searchView.setOnQueryTextListener(this)
        return true

    }
    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        var userInput:String? = newText?.toLowerCase()
        var newList:MutableList<Content.Annonce> = ArrayList<Content.Annonce>()
        for(item in Content.ITEMS){
            if(item.content.toLowerCase().contains(userInput as CharSequence) ){
                newList.add(item)
            }
        }
        adapter.updateList(newList)
        return true
    }

}
