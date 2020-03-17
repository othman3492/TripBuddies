import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.othman.tripbuddies.R
import com.othman.tripbuddies.models.Trip
import com.othman.tripbuddies.models.User
import com.othman.tripbuddies.utils.AdapterEvent
import com.othman.tripbuddies.utils.FirebaseUserHelper
import kotlinx.android.synthetic.main.trip_buddies_list_layout.view.*
import kotlinx.android.synthetic.main.trip_photos_list_layout.view.*
import org.greenrobot.eventbus.EventBus
import java.lang.IllegalArgumentException


class TripBuddiesAdapter(val context: Context, val trip: Trip, private val itemClickListener: (User) -> Unit,
                         private val addClickListener: (View) -> Unit) :
    RecyclerView.Adapter<TripBuddiesAdapter.BaseTripBuddiesViewHolder>() {


    private var buddiesList: List<User> = ArrayList()
    private val buddiesAdapterId = 1
    private val typeItem = 0
    private val typeAdd = 1


    override fun getItemCount() = buddiesList.size + 1


    override fun getItemViewType(position: Int): Int {

        return if (position == itemCount - 1) {
            typeAdd
        } else {
            typeItem
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseTripBuddiesViewHolder {

        return when (viewType) {

            typeItem -> {

                val view = LayoutInflater.from(parent.context).inflate(R.layout.trip_buddies_list_layout, parent, false)
                TripBuddiesViewHolder(view, context)
            }

            typeAdd -> {

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

                // Configure delete button if user is trip's creator
                if (trip.userId == FirebaseUserHelper.getCurrentUser()!!.uid &&
                    !(buddiesList[position].userId == FirebaseUserHelper.getCurrentUser()!!.uid)) {

                    holder.itemView.remove_buddy_button.setOnClickListener {

                        EventBus.getDefault()
                            .post(AdapterEvent(buddiesAdapterId, buddiesList[position].userId))

                        buddiesList.toMutableList().removeAt(position)
                        notifyItemRemoved(position)
                    }
                } else {

                    holder.itemView.remove_buddy_button.visibility = View.GONE
                }
            }

            is TripBuddiesFooter -> {

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





