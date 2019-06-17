package dz.esi.tdm1.annonces

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.design.widget.Snackbar
import android.widget.TextView

import dz.esi.tdm1.annonces.dummy.DummyContent
import kotlinx.android.synthetic.main.activity_item_list.*
import kotlinx.android.synthetic.main.item_list_content.view.*
import kotlinx.android.synthetic.main.item_list.*
import android.support.annotation.NonNull
import android.util.Log
import android.view.*
import android.widget.ListAdapter
import android.widget.SearchView
import com.bumptech.glide.Glide
import com.myhexaville.smartimagepicker.ImagePicker
import java.io.File


lateinit var recyclerList: RecyclerView
class ItemListActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private var twoPane: Boolean = false
    lateinit var imagePicker : ImagePicker
    var adapter = SimpleItemRecyclerViewAdapter(this, DummyContent.ITEMS, twoPane)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        setSupportActionBar(toolbar)
        toolbar.title = title

        fab.setOnClickListener {
            val intent =  Intent(this, ItemCreateActivity::class.java)
            this.startActivity(intent)
        }

        if (item_detail_container != null) {
            twoPane = true
        }
        recyclerList = item_list
        setupRecyclerView(item_list)

    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        adapter = SimpleItemRecyclerViewAdapter(this, DummyContent.ITEMS, twoPane)
        recyclerList.setAdapter(adapter)
    }

    class SimpleItemRecyclerViewAdapter(
        private val parentActivity: ItemListActivity,
        private var values: MutableList<DummyContent.DummyItem>,
        private val twoPane: Boolean
    ) :
        RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as DummyContent.DummyItem
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

        fun updateList(newList: MutableList<DummyContent.DummyItem>) {
            values = ArrayList<DummyContent.DummyItem>()
            values.addAll(newList)
            notifyDataSetChanged()

        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        //var inflater: MenuInflater = getMenuInflater()
       // inflater.inflate(R.menu.search_menu, menu)
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
        var newList:MutableList<DummyContent.DummyItem> = ArrayList<DummyContent.DummyItem>()
        for(item in DummyContent.ITEMS){
            if(item.content.toLowerCase().contains(userInput as CharSequence)){
                newList.add(item)
            }
        }
        adapter.updateList(newList)
        return true
    }

}
