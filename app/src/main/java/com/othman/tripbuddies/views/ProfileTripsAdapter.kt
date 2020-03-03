import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.othman.tripbuddies.R
import com.othman.tripbuddies.models.Trip
import kotlinx.android.synthetic.main.trips_list_layout.view.*


class ProfileTripsAdapter(val context: Context, private val clickListener: (Trip) -> Unit) :
    RecyclerView.Adapter<ProfileTripsAdapter.ProfileTripsViewHolder>() {


    private var tripList: List<Trip> = ArrayList()


    override fun getItemCount() = tripList.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileTripsViewHolder {

        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.trips_list_layout, parent, false)
        return ProfileTripsViewHolder(v, context)
    }

    // Populate ViewHolder with data depending on the position in the list
    override fun onBindViewHolder(holder: ProfileTripsViewHolder, position: Int) {

        holder.bind(tripList[position], clickListener)
    }

    
    fun updateData(list: List<Trip>) {

        tripList = list
        this.notifyDataSetChanged()
    }


    class ProfileTripsViewHolder(v: View, private var context: Context) : RecyclerView.ViewHolder(v), View.OnClickListener {

        private var view: View = v

        init { v.setOnClickListener(this) }

        override fun onClick(v: View?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        // Assign data to the views
        fun bind(trip: Trip, clickListener: (Trip) -> Unit) {

            view.trip_list_name.text = trip.name
            view.trip_list_name.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
            view.trip_list_dates.text = String.format(
                context.resources.getString(
                    R.string.dates_from_to, trip.departDate, trip.returnDate
                )
            )


            // Set destination(s) text view depending on quantities
            /*view.trip_list_cities_or_user.text = context.resources.getQuantityString(
                R.plurals.destination_and_others,
                trip.destination.size, trip.destination[0].name, trip.destination.size
            )*/


            // Display first trip photo if image list isn't empty
            if (trip.photosList.isNotEmpty()) {
                Glide.with(context).load(trip.photosList[0]).into(view.trip_list_image)
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





