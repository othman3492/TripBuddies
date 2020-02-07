import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.othman.tripbuddies.R
import com.othman.tripbuddies.models.City
import com.othman.tripbuddies.models.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.city_or_buddies_list_layout.view.*


class CityBuddiesAdapter(val context: Context, val city: City) :
    RecyclerView.Adapter<CityBuddiesAdapter.CityBuddiesViewHolder>() {


    override fun getItemCount() = city.wishList.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityBuddiesViewHolder {

        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.city_or_buddies_list_layout, parent, false)
        return CityBuddiesViewHolder(v)
    }

    // Populate ViewHolder with data depending on the position in the list
    override fun onBindViewHolder(holder: CityBuddiesViewHolder, position: Int) {


        holder.bind(city.wishList[position])
    }




    class CityBuddiesViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        private var view: View = v


        // Assign data to the views
        fun bind(user: User) {

            view.city_or_buddies_list_name.text = user.name

            // Display profile picture if not null
            if (user.urlPicture != null) {
                Picasso.get().load(user.urlPicture).into(view.city_or_buddies_list_image)
            } else {
                Picasso.get().load(R.drawable.blank_picture).into(view.city_or_buddies_list_image)
            }

        }


    }
}





