import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.othman.tripbuddies.R
import com.othman.tripbuddies.models.City
import com.othman.tripbuddies.models.Trip
import com.othman.tripbuddies.models.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.trips_list_layout.view.*


class CityTripsAdapter(val context: Context, private val clickListener: (Trip) -> Unit) :
    RecyclerView.Adapter<CityTripsAdapter.CityTripsViewHolder>() {


    private var tripList: List<Trip> = ArrayList()


    override fun getItemCount() = tripList.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityTripsViewHolder {

        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.trips_list_layout, parent, false)
        return CityTripsViewHolder(v, context)
    }

    // Populate ViewHolder with data depending on the position in the list
    override fun onBindViewHolder(holder: CityTripsViewHolder, position: Int) {

        holder.bind(tripList[position], clickListener)
    }


    fun updateData(list: List<Trip>) {

        tripList = list
        this.notifyDataSetChanged()
    }


    class CityTripsViewHolder(v: View, private var context: Context) : RecyclerView.ViewHolder(v), View.OnClickListener {


        private var view: View = v

        init { v.setOnClickListener(this) }

        override fun onClick(v: View?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }


        // Assign data to the views
        fun bind(trip: Trip, clickListener: (Trip) -> Unit) {

            view.trip_list_name.text = trip.name
            view.trip_list_cities_or_user.text = trip.username
            view.trip_list_dates.text = String.format(
                context.resources.getString(
                    R.string.dates_from_to, trip.departDate, trip.returnDate
                )
            )


            // Display first trip photo if image list isn't empty
            if (trip.imagesList.isNotEmpty()) {
                Glide.with(context).load(trip.imagesList[0]).into(view.trip_list_image)
            } else {
                Glide.with(context).load(R.drawable.blank_picture).into(view.trip_list_image)
            }


            // Set number of buddies
            view.trip_list_nb_buddies.text = trip.nbBuddies.toString()

            // Set view holder on click
            view.setOnClickListener { clickListener(trip) }
        }


    }
}





