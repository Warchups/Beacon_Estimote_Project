package com.gnommostudios.alertru.beacon_estimote_project.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v7.app.ActionBar
import android.support.v7.widget.CardView
import android.support.v7.widget.Toolbar
import android.util.DisplayMetrics
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.estimote.coresdk.recognition.packets.Beacon
import com.gnommostudios.alertru.beacon_estimote_project.API.MyOperationalFilm
import com.gnommostudios.alertru.beacon_estimote_project.R
import com.gnommostudios.alertru.beacon_estimote_project.pojo.Favourite
import com.gnommostudios.alertru.beacon_estimote_project.pojo.Film
import com.gnommostudios.alertru.beacon_estimote_project.pojo.MyBeacon

class FavouriteDetailActivity : AppCompatActivity(), AppBarLayout.OnOffsetChangedListener, View.OnClickListener {

    private var favourite: Favourite? = null
    private var film: Film? = null
    private var myBeacon: MyBeacon? = null

    private var collapsingToolbarLayout: CollapsingToolbarLayout? = null
    private var coordinator: CoordinatorLayout? = null
    private var appBar: AppBarLayout? = null
    private var toolbar: Toolbar? = null
    private var actionBar: ActionBar? = null

    private var backButton: ImageView? = null
    private var favButton: ImageView? = null

    private var imageDetail: ImageView? = null
    private var coverDetailFixed: ImageView? = null
    private var coverDetailFloat: ImageView? = null

    private var containerCover: CardView? = null
    private var finalCover: CardView? = null

    private var titleDetail: TextView? = null
    private var uuidDetail: TextView? = null
    private var majorDetail: TextView? = null
    private var minorDetail: TextView? = null
    private var macDetail: TextView? = null

    private var previousPosition = 0

    private var width: Int = 0
    private var height: Int = 0

    private var mof: MyOperationalFilm? = null

    private var isFavourite: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite_detail)

        mof = MyOperationalFilm.getInstance(this)

        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        width = metrics.widthPixels // ancho absoluto en pixels
        height = metrics.heightPixels // alto absoluto en pixels

        isFavourite =
                if (intent.extras.getBoolean("IS_FAV")) {
                    favourite = intent.extras.getSerializable("FAVOURITE") as Favourite
                    film = mof!!.searchFilm(favourite!!.mac!!)
                    true
                } else {
                    myBeacon = intent.extras.getSerializable("MY_BEACON") as MyBeacon
                    film = mof!!.searchFilm(myBeacon!!.mac!!)
                    favourite = Favourite(film!!.id, myBeacon!!.uuid, myBeacon!!.major, myBeacon!!.minor, myBeacon!!.mac)
                    false
                }

        collapsingToolbarLayout = findViewById(R.id.collapsingToolbar)
        collapsingToolbarLayout!!.isTitleEnabled = false

        coordinator = findViewById(R.id.coordinator)
        appBar = findViewById(R.id.appbar)
        appBar!!.addOnOffsetChangedListener(this)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        actionBar = supportActionBar

        backButton = findViewById(R.id.back_button)
        backButton!!.setOnClickListener(this)

        favButton = findViewById(R.id.fav_button)
        favButton!!.setOnClickListener(this)

        imageDetail = findViewById(R.id.image_detail)
        coverDetailFixed = findViewById(R.id.cover_detail_2)
        coverDetailFloat = findViewById(R.id.cover_detail_1)

        containerCover = findViewById(R.id.container_cover)
        finalCover = findViewById(R.id.final_cover)
        finalCover!!.visibility = View.INVISIBLE

        titleDetail = findViewById(R.id.title_detail)
        uuidDetail = findViewById(R.id.uuid_detail)
        majorDetail = findViewById(R.id.major_detail)
        minorDetail = findViewById(R.id.minor_detail)
        macDetail = findViewById(R.id.mac_detail)

        if (isFavourite) {
            favButton!!.setImageDrawable(resources.getDrawable(R.drawable.baseline_star_selected_24))
            val imageResource = resources.getIdentifier(film!!.image, null, packageName)

            Glide.with(this).load(imageResource).into(imageDetail)
            Glide.with(this).load(imageResource).into(coverDetailFloat)
            Glide.with(this).load(imageResource).into(coverDetailFixed)

            titleDetail!!.text = film!!.title
            uuidDetail!!.text = favourite!!.uuid
            majorDetail!!.text = "Major: ${favourite!!.major}"
            minorDetail!!.text = "Minor: ${favourite!!.minor}"
            macDetail!!.text = favourite!!.mac
        } else {
            favButton!!.setImageDrawable(resources.getDrawable(R.drawable.baseline_star_unselected_24))
            val imageResource = resources.getIdentifier(myBeacon!!.image, null, packageName)

            Glide.with(this).load(imageResource).into(imageDetail)
            Glide.with(this).load(imageResource).into(coverDetailFloat)
            Glide.with(this).load(imageResource).into(coverDetailFixed)

            titleDetail!!.text = myBeacon!!.title
            uuidDetail!!.text = myBeacon!!.uuid
            majorDetail!!.text = "Major: ${myBeacon!!.major}"
            minorDetail!!.text = "Minor: ${myBeacon!!.minor}"
            macDetail!!.text = myBeacon!!.mac
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.fav_button -> {
                isFavourite = mof!!.isFavourite(film!!)

                if (!isFavourite) {
                    mof!!.addFav(favourite!!)
                    favButton!!.setImageDrawable(resources.getDrawable(R.drawable.baseline_star_selected_24))
                } else {
                    mof!!.deleteFav(mof!!.searchFav(film!!.id)!!)
                    favButton!!.setImageDrawable(resources.getDrawable(R.drawable.baseline_star_unselected_24))
                }
            }
            R.id.back_button -> onBackPressed()
        }
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        if (verticalOffset == 0) {
            //La imagen que esta dentro del NestedScrollView la pongo invisible
            finalCover!!.visibility = View.INVISIBLE
            //Y la que esta en el CoordinatorLayout en visible
            containerCover!!.visibility = View.VISIBLE

        } else {
            if (verticalOffset == -810) {
                //La imagen que esta dentro del NestedScrollView la pongo en visible
                finalCover!!.visibility = View.VISIBLE
                //Y la que esta en el CoordinatorLayout en invisible
                containerCover!!.visibility = View.INVISIBLE
            } else {

                //Si la pantalla es mas grande de 800x480**
                if (width > 480) {
                    if (verticalOffset < -130 && verticalOffset > -135 || verticalOffset < -135) {
                        //Si el desplazamiento esta entre -130 y -135 o se pasa de -135 por si vamos muy deprisa pongo:
                        //La imagen del NestedScrollView en visible
                        finalCover!!.visibility = View.VISIBLE
                        //Y la que esta en el CoordinatorLayout en invisible
                        containerCover!!.visibility = View.INVISIBLE
                    }
                } else {
                    if (verticalOffset < -115 && verticalOffset > -120 || verticalOffset < -125) {
                        //Si el desplazamiento esta entre -115 y -120 o se pasa de -125 por si vamos muy deprisa pongo:
                        //La imagen del NestedScrollView en visible
                        finalCover!!.visibility = View.VISIBLE
                        //Y la que esta en el CoordinatorLayout en invisible
                        containerCover!!.visibility = View.INVISIBLE
                    }
                }

                if (previousPosition < verticalOffset) {
                    //Si estoy bajando
                    //Si la pantalla es mas grande de 800x480**
                    if (width > 480) {
                        if (verticalOffset < -125 && verticalOffset > -130 || verticalOffset > -125) {
                            //Si el desplazamiento esta entre -125 y -130 o se pasa de -125 por si vamos muy deprisa pongo:
                            //La imagen del NestedScrollView en invisible
                            finalCover!!.visibility = View.INVISIBLE
                            //Y la que esta en el CoordinatorLayout en invisible
                            containerCover!!.visibility = View.VISIBLE
                        }
                    } else {
                        if (verticalOffset < -110 && verticalOffset > -115 || verticalOffset > -110) {
                            //Si el desplazamiento esta entre -110 y -115 o se pasa de -110 por si vamos muy deprisa pongo:
                            //La imagen del NestedScrollView en invisible
                            finalCover!!.visibility = View.INVISIBLE
                            //Y la que esta en el CoordinatorLayout en visible
                            containerCover!!.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }

        //Me guardo la posicion en la que estoy ahora para despues mirar si estoy subiendo o bajando
        previousPosition = verticalOffset
    }
}
