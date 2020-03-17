import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.othman.tripbuddies.BuildConfig
import com.othman.tripbuddies.R
import com.othman.tripbuddies.models.City
import com.othman.tripbuddies.models.Trip
import com.othman.tripbuddies.utils.AdapterEvent
import com.othman.tripbuddies.utils.FirebaseUserHelper
import kotlinx.android.synthetic.main.trip_cities_list_layout.view.*
import kotlinx.android.synthetic.main.trip_photos_list_layout.view.*
import org.greenrobot.eventbus.EventBus
import java.lang.IllegalArgumentException
import java.util.*


class TripDestinationsAdapter(val context: Context, val trip: Trip, private val itemClickListener: (City) -> Unit,
                              private val addClickListener: (View) -> Unit) :
    RecyclerView.Adapter<TripDestinationsAdapter.BaseTripDestinationsViewHolder>() {


    private var destinationsList: List<City> = ArrayList()
    private val destinationsAdapterId = 2
    private val typeItem = 0
    private val typeAdd = 1



    override fun getItemCount() = destinationsList.size + 1


    override fun getItemViewType(position: Int): Int {

        return if (position == itemCount - 1) {
            typeAdd
        } else {
            typeItem
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseTripDestinationsViewHolder {

        return when (viewType) {

            typeItem -> {

                val view = LayoutInflater.from(parent.context).inflate(R.layout.trip_cities_list_layout, parent, false)
                TripDestinationsViewHolder(view, context)
            }

            typeAdd -> {

                val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_add_button, parent, false)
                TripDestinationsFooter(view)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }


    // Populate ViewHolder with data depending on the position in the list
    override fun onBindViewHolder(holder: BaseTripDestinationsViewHolder, position: Int) {

        when (holder) {

            is TripDestinationsViewHolder -> {

                holder.bind(destinationsList[position], itemClickListener)

                // Configure delete button if user is trip's creator
                if (trip.userId == FirebaseUserHelper.getCurrentUser()!!.uid) {

                    holder.itemView.remove_city_button.setOnClickListener {

                        EventBus.getDefault()
                            .post(AdapterEvent(destinationsAdapterId, destinationsList[position].cityId))

                        destinationsList.toMutableList().removeAt(position)
                        notifyItemRemoved(position)
                    }
                } else {

                    holder.itemView.remove_city_button.visibility = View.GONE
                }
            }

            is TripDestinationsFooter -> {

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


    fun updateData(list: List<City>) {

        destinationsList = list
        this.notifyDataSetChanged()
    }




    abstract class BaseTripDestinationsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}



    class TripDestinationsViewHolder(v: View, private var context: Context) :
        BaseTripDestinationsViewHolder(v), View.OnClickListener {

        private var view: View = v
        val COVER_IMAGE_URL =
            "https://maps.googleapis.com/maps/api/place/photo?maxwidth=1000&maxheight=1000&photoreference="


        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }


        // Assign data to the views
        fun bind(city: City, clickListener: (City) -> Unit) {

            view.details_city_name.text = city.name

            val path = COVER_IMAGE_URL + city.coverPicture + "&key=" + BuildConfig.google_apikey


            // Display profile picture if not null
            if (city.coverPicture != null) {
                Glide.with(context).load(path).into(view.details_city_image)
            } else {
                Glide.with(context).load(R.drawable.blank_picture)
                    .into(view.details_city_image)
            }

            // Set view holder on click
            view.setOnClickListener { clickListener(city) }
        }
    }


    class TripDestinationsFooter(v: View) : BaseTripDestinationsViewHolder(v), View.OnClickListener {

        private var view: View = v

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        // Set view onClick
        fun bind(clickListener: (View) -> Unit) { view.setOnClickListener { clickListener(view) }}

    }
}





