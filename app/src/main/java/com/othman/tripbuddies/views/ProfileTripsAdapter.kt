import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.othman.tripbuddies.R
import com.othman.tripbuddies.models.Trip
import com.othman.tripbuddies.models.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.trips_list_layout.view.*


class ProfileTripsAdapter(val context: Context, val user: User) :
    RecyclerView.Adapter<ProfileTripsAdapter.ProfileTripsViewHolder>() {


    override fun getItemCount() = user.tripList.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileTripsViewHolder {

        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.trips_list_layout, parent, false)
        return ProfileTripsViewHolder(v, context)
    }

    // Populate ViewHolder with data depending on the position in the list
    override fun onBindViewHolder(holder: ProfileTripsViewHolder, position: Int) {

        holder.bind(user.tripList[position])
    }


    class ProfileTripsViewHolder(v: View, private var context: Context) : RecyclerView.ViewHolder(v) {

        private var view: View = v


        // Assign data to the views
        fun bind(trip: Trip) {

            view.trip_list_name.text = trip.name
            view.trip_list_dates.text = String.format(
                context.resources.getString(
                    R.string.dates_from_to, trip.departDate, trip.returnDate
                )
            )


            // Set destination(s) text view depending on quantities
            view.trip_list_cities_or_user.text = context.resources.getQuantityString(
                R.plurals.destination_and_others,
                trip.destination.size, trip.destination[0].name, trip.destination.size
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





