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
import kotlinx.android.synthetic.main.city_or_buddies_list_layout.view.*


class TripBuddiesAdapter(val context: Context, private val clickListener: (User) -> Unit) :
    RecyclerView.Adapter<TripBuddiesAdapter.TripBuddiesViewHolder>() {


    private var buddiesList: List<User> = ArrayList()


    override fun getItemCount() = buddiesList.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripBuddiesViewHolder {

        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.city_or_buddies_list_layout, parent, false)
        return TripBuddiesViewHolder(v, context)
    }

    // Populate ViewHolder with data depending on the position in the list
    override fun onBindViewHolder(holder: TripBuddiesViewHolder, position: Int) {

        holder.bind(buddiesList[position], clickListener)
    }


    fun updateData(list: List<User>) {

        buddiesList = list
        this.notifyDataSetChanged()
    }




    class TripBuddiesViewHolder(v: View, private var context: Context) : RecyclerView.ViewHolder(v), View.OnClickListener {

        private var view: View = v

        init { v.setOnClickListener(this) }

        override fun onClick(v: View?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }


        // Assign data to the views
        fun bind(user: User, clickListener: (User) -> Unit) {

            view.city_or_buddies_list_name.text = user.name

            // Display profile picture if not null
            if (user.urlPicture != null) {
                Glide.with(context).load(user.urlPicture).into(view.city_or_buddies_list_image)
            } else {
                Glide.with(context).load(R.drawable.blank_picture).into(view.city_or_buddies_list_image)
            }

            // Set view holder on click
            view.setOnClickListener { clickListener(user) }
        }


    }
}





