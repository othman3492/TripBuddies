package com.othman.tripbuddies.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.othman.tripbuddies.BuildConfig
import com.othman.tripbuddies.R
import com.othman.tripbuddies.data.model.City
import kotlinx.android.synthetic.main.city_or_buddies_list_layout.view.*


class ProfileCitiesAdapter(val context: Context, private val clickListener: (City) -> Unit) :
    RecyclerView.Adapter<ProfileCitiesAdapter.ProfileCitiesViewHolder>() {


    private var wishList: List<City> = ArrayList()


    override fun getItemCount() = wishList.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileCitiesViewHolder {

        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.city_or_buddies_list_layout, parent, false)
        return ProfileCitiesViewHolder(v, context)
    }

    // Populate ViewHolder with data depending on the position in the list
    override fun onBindViewHolder(holder: ProfileCitiesViewHolder, position: Int) {

        holder.bind(wishList[position], clickListener)
    }


    fun updateData(list: List<City>) {

        wishList = list
        this.notifyDataSetChanged()
    }




    class ProfileCitiesViewHolder(v: View, private var context: Context) : RecyclerView.ViewHolder(v), View.OnClickListener {

        private var view: View = v
        private val coverImageURL =
            "https://maps.googleapis.com/maps/api/place/photo?maxwidth=1000&maxheight=1000&photoreference="

        init { v.setOnClickListener(this) }

        override fun onClick(v: View?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }


        // Assign data to the views
        fun bind(city: City, clickListener: (City) -> Unit) {

            view.city_or_buddies_list_name.text = city.name
            view.city_or_buddies_list_name.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
            Glide.with(context).load(coverImageURL + city.coverPicture +
                    "&key=" + BuildConfig.google_apikey).into(view.user_search_item_image)

            // Set view holder on click
            view.setOnClickListener { clickListener(city) }
        }


    }
}





