package com.othman.tripbuddies.views

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.othman.tripbuddies.R
import com.othman.tripbuddies.models.Trip
import com.othman.tripbuddies.utils.FirebaseUserHelper
import com.othman.tripbuddies.utils.Utils
import kotlinx.android.synthetic.main.trip_photos_list_layout.view.*
import org.greenrobot.eventbus.EventBus
import java.lang.IllegalArgumentException


class TripPhotosAdapter(
    val context: Context, val trip: Trip, private val itemClickListener: (String) -> Unit,
    private val addClickListener: (View) -> Unit
) :
    RecyclerView.Adapter<TripPhotosAdapter.BaseTripPhotosViewHolder>() {

    private val photoAdapterId = 0
    private val typeItem = 0
    private val typeAdd = 1


    override fun getItemCount() = trip.photosList.size + 1


    override fun getItemViewType(position: Int): Int {

        return if (position == itemCount - 1) {
            typeAdd
        } else {
            typeItem
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseTripPhotosViewHolder {

        return when (viewType) {

            typeItem -> {

                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.trip_photos_list_layout, parent, false)
                TripPhotosViewHolder(view, context)
            }

            typeAdd -> {

                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recycler_view_add_button, parent, false)
                TripPhotosFooter(view)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }


    // Populate ViewHolder with data depending on the position in the list
    override fun onBindViewHolder(holder: BaseTripPhotosViewHolder, position: Int) {

        when (holder) {

            is TripPhotosViewHolder -> {
                holder.bind(trip.photosList[position], itemClickListener)


                // Configure delete button if user is trip's creator
                if (trip.userId == FirebaseUserHelper.getCurrentUser()!!.uid) {

                    holder.itemView.remove_photo_button.setOnClickListener {

                        EventBus.getDefault()
                            .post(Utils.AdapterEvent(photoAdapterId, trip.photosList[position]))

                        trip.photosList.removeAt(position)
                        notifyItemRemoved(position)
                    }
                } else {

                    holder.itemView.remove_photo_button.visibility = View.GONE
                }
            }

            is TripPhotosFooter ->  {

                // Display add button if user is trip's creator
                if (trip.userId == FirebaseUserHelper.getCurrentUser()!!.uid) {
                    holder.bind(addClickListener)
                } else {
                    holder.itemView.visibility = View.GONE
                }
            }
            else -> throw IllegalArgumentException()
        }
    }




    abstract class BaseTripPhotosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    class TripPhotosViewHolder(v: View, private var context: Context) : BaseTripPhotosViewHolder(v),
        View.OnClickListener {


        private var view: View = v

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }


        // Assign data to the views
        fun bind(photo: String, clickListener: (String) -> Unit) {

            Glide.with(context).load(photo).into(view.details_photo_image)

            // Set view holder on click
            view.setOnClickListener { clickListener(photo) }
        }
    }


    class TripPhotosFooter(v: View) : BaseTripPhotosViewHolder(v), View.OnClickListener {

        private var view: View = v

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        // Set view onClick
        fun bind(clickListener: (View) -> Unit) {
            view.setOnClickListener { clickListener(view) }
        }

    }
}





