package com.gnommostudios.alertru.beacon_estimote_project.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.gnommostudios.alertru.beacon_estimote_project.API.MyOperationalFilm
import com.gnommostudios.alertru.beacon_estimote_project.R
import com.gnommostudios.alertru.beacon_estimote_project.pojo.Favourite
import com.gnommostudios.alertru.beacon_estimote_project.pojo.Film
import kotlinx.android.synthetic.main.element_list_favourite.view.*

class FavouritesListAdapter(private val context: Activity, private val elements: ArrayList<Favourite>, private val mof: MyOperationalFilm) : ArrayAdapter<Favourite>(context, R.layout.element_list_favourite, elements) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val convertView: View?
        val inflater = context.layoutInflater
        convertView = inflater.inflate(R.layout.element_list_favourite, null)
        val element = elements[position]
        val film = mof.searchFilm(element.mac!!)

        val image = convertView.findViewById<ImageView>(R.id.image_fav)
        val title = convertView.findViewById<TextView>(R.id.title_fav)
        val uuid = convertView.findViewById<TextView>(R.id.uuid_fav)
        val major = convertView.findViewById<TextView>(R.id.major_fav)
        val minor = convertView.findViewById<TextView>(R.id.minor_fav)
        val mac = convertView.findViewById<TextView>(R.id.mac_fav)

        val imageResource = context.resources.getIdentifier(film!!.image, null, context.packageName)
        Glide.with(context).load(imageResource).into(image)

        title.text = film.title
        uuid.text = element.uuid
        major.text = "Major: ${element.major}"
        minor.text = "Minor: ${element.minor}"
        mac.text = "${element.mac}"

        return convertView
    }

    fun updateFavouritesList(favourites: List<Favourite>) {
        elements.clear()
        elements.addAll(favourites)
        notifyDataSetChanged()
    }
}
