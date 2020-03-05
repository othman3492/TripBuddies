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
import kotlinx.android.synthetic.main.trip_buddies_list_layout.view.*
import java.lang.IllegalArgumentException


class TripBuddiesAdapter(val context: Context, private val itemClickListener: (User) -> Unit,
                              private val addClickListener: (View) -> Unit) :
    RecyclerView.Adapter<TripBuddiesAdapter.BaseTripBuddiesViewHolder>() {


    private var buddiesList: List<User> = ArrayList()
    private val TYPE_ITEM = 0
    private val TYPE_ADD = 1


    override fun getItemCount() = buddiesList.size + 1


    override fun getItemViewType(position: Int): Int {

        return if (position == itemCount - 1) {
            TYPE_ADD
        } else {
            TYPE_ITEM
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseTripBuddiesViewHolder {

        return when (viewType) {

            TYPE_ITEM -> {

                val view = LayoutInflater.from(parent.context).inflate(R.layout.trip_buddies_list_layout, parent, false)
                TripBuddiesViewHolder(view, context)
            }

            TYPE_ADD -> {

                val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_add_button, parent, false)
                TripBuddiesFooter(view)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }


    // Populate ViewHolder with data depending on the position in the list
    override fun onBindViewHolder(holder: BaseTripBuddiesViewHolder, position: Int) {

        when (holder) {

            is TripBuddiesViewHolder -> {
                holder.bind(buddiesList[position], itemClickListener)

                // Configure delete button
                holder.itemView.remove_buddy_button.setOnClickListener {

                    buddiesList.toMutableList().removeAt(position)
                    notifyItemRemoved(position)
                }
            }
            is TripBuddiesFooter -> holder.bind(addClickListener)
            else -> throw IllegalArgumentException()
        }
    }


    fun updateData(list: List<User>) {

        buddiesList = list
        this.notifyDataSetChanged()
    }




    abstract class BaseTripBuddiesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}


    class TripBuddiesViewHolder(v: View, private var context: Context) : BaseTripBuddiesViewHolder(v), View.OnClickListener {

        private var view: View = v

        init { v.setOnClickListener(this) }

        override fun onClick(v: View?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }


        // Assign data to the views
        fun bind(user: User, clickListener: (User) -> Unit) {

            view.details_buddy_name.text = user.name

            // Display profile picture if not null
            if (user.urlPicture != null) {
                Glide.with(context).load(user.urlPicture).into(view.details_buddy_image)
            } else {
                Glide.with(context).load(R.drawable.blank_picture).into(view.details_buddy_image)
            }

            // Set view holder on click
            view.setOnClickListener { clickListener(user) }
        }


    }


    class TripBuddiesFooter(v: View) : BaseTripBuddiesViewHolder(v), View.OnClickListener {

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





