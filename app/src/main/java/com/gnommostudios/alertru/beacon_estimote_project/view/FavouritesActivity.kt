package com.gnommostudios.alertru.beacon_estimote_project.view

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import com.gnommostudios.alertru.beacon_estimote_project.API.MyOperationalFilm
import com.gnommostudios.alertru.beacon_estimote_project.R
import com.gnommostudios.alertru.beacon_estimote_project.adapter.FavouritesListAdapter
import com.gnommostudios.alertru.beacon_estimote_project.pojo.Favourite

class FavouritesActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    private var mof: MyOperationalFilm? = null

    private var favourites: ArrayList<Favourite>? = null

    private var listFavs: ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourites)

        mof = MyOperationalFilm.getInstance(this)

        listFavs = findViewById<View>(R.id.list_favourites) as ListView
        listFavs!!.onItemClickListener = this

        favourites = mof!!.allFavs

        listFavs!!.adapter = FavouritesListAdapter(this, favourites!!, mof!!)

    }

    override fun onResume() {
        super.onResume()
        refreshList()
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        var intent = Intent(FavouritesActivity@this, FavouriteDetailActivity::class.java)
        intent!!.putExtra("FAVOURITE", favourites!![pos])
        startActivity(intent)
    }

    private fun refreshList() {
        var adapter = listFavs!!.adapter as FavouritesListAdapter
        adapter!!.updateFavouritesList(mof!!.allFavs)
    }
}
