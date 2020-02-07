import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.othman.tripbuddies.R
import com.othman.tripbuddies.models.City
import com.othman.tripbuddies.models.User
import kotlinx.android.synthetic.main.city_or_buddies_list_layout.view.*


class ProfileCitiesAdapter(val context: Context, val user: User) :
    RecyclerView.Adapter<ProfileCitiesAdapter.ProfileCitiesViewHolder>() {


    override fun getItemCount() = user.wishList.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileCitiesViewHolder {

        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.city_or_buddies_list_layout, parent, false)
        return ProfileCitiesViewHolder(v)
    }

    // Populate ViewHolder with data depending on the position in the list
    override fun onBindViewHolder(holder: ProfileCitiesViewHolder, position: Int) {

        holder.bind(user.wishList[position])
    }




    class ProfileCitiesViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        private var view: View = v


        // Assign data to the views
        fun bind(city: City) {

            view.city_or_buddies_list_name.text = city.name

            // Display city profile from Google

        }


    }
}





