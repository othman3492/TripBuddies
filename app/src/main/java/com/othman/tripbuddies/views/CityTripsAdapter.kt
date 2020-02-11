import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.othman.tripbuddies.R
import com.othman.tripbuddies.models.City
import com.othman.tripbuddies.models.Trip
import com.othman.tripbuddies.models.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.trips_list_layout.view.*


class CityTripsAdapter(val context: Context, private val city: City) :
    RecyclerView.Adapter<CityTripsAdapter.CityTripsViewHolder>() {


    override fun getItemCount() = city.lastTrips.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityTripsViewHolder {

        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.trips_list_layout, parent, false)
        return CityTripsViewHolder(v, context)
    }

    // Populate ViewHolder with data depending on the position in the list
    override fun onBindViewHolder(holder: CityTripsViewHolder, position: Int) {

        holder.bind(city.lastTrips[position])
    }


    class CityTripsViewHolder(v: View, private var context: Context) :
        RecyclerView.ViewHolder(v) {

        private var view: View = v


        // Assign data to the views
        fun bind(trip: Trip) {

            view.trip_list_name.text = trip.name
            view.trip_list_cities_or_user.text = trip.username
            view.trip_list_dates.text = String.format(
                context.resources.getString(
                    R.string.dates_from_to, trip.departDate, trip.returnDate
                )
            )


            // Display first trip photo if image list isn't empty
            if (trip.imageList.size > 0) {
                Picasso.get().load(trip.imageList[0]).into(view.trip_list_image)
            } else {
                Picasso.get().load(R.drawable.blank_picture).into(view.trip_list_image)
            }


            // Set number of buddies
            view.trip_list_nb_buddies.text = trip.buddiesList.toString()
        }


    }
}





