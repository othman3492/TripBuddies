import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.othman.tripbuddies.R
import com.othman.tripbuddies.models.Trip
import com.othman.tripbuddies.utils.AdapterEvent
import kotlinx.android.synthetic.main.trip_photos_list_layout.view.*
import org.greenrobot.eventbus.EventBus
import java.lang.IllegalArgumentException


class TripPhotosAdapter(
    val context: Context, val trip: Trip, private val itemClickListener: (String) -> Unit,
    private val addClickListener: (View) -> Unit
) :
    RecyclerView.Adapter<TripPhotosAdapter.BaseTripPhotosViewHolder>() {

    private val photoAdapterId = 0
    private val typeItem = 0
    private val typeAdd = 1


    override fun getItemCount() = trip.photosList.size + 1


    override fun getItemViewType(position: Int): Int {

        return if (position == itemCount - 1) {
            typeAdd
        } else {
            typeItem
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseTripPhotosViewHolder {

        return when (viewType) {

            typeItem -> {

                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.trip_photos_list_layout, parent, false)
                TripPhotosViewHolder(view, context)
            }

            typeAdd -> {

                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recycler_view_add_button, parent, false)
                TripPhotosFooter(view)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }


    // Populate ViewHolder with data depending on the position in the list
    override fun onBindViewHolder(holder: BaseTripPhotosViewHolder, position: Int) {

        when (holder) {

            is TripPhotosViewHolder -> {
                holder.bind(trip.photosList[position], itemClickListener)

                // Configure delete button and get event
                holder.itemView.remove_photo_button.setOnClickListener {

                    EventBus.getDefault().post(AdapterEvent(photoAdapterId, trip.photosList[position]))

                    trip.photosList.removeAt(position)
                    notifyItemRemoved(position)
                }
            }
            is TripPhotosFooter -> holder.bind(addClickListener)
            else -> throw IllegalArgumentException()
        }
    }


    abstract class BaseTripPhotosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}


    class TripPhotosViewHolder(v: View, private var context: Context) : BaseTripPhotosViewHolder(v),
        View.OnClickListener {


        private var view: View = v

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }


        // Assign data to the views
        fun bind(photo: String, clickListener: (String) -> Unit) {

            Glide.with(context).load(photo).into(view.details_photo_image)

            // Set view holder on click
            view.setOnClickListener { clickListener(photo) }
        }
    }


    class TripPhotosFooter(v: View) : BaseTripPhotosViewHolder(v), View.OnClickListener {

        private var view: View = v

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        // Set view onClick
        fun bind(clickListener: (View) -> Unit) {
            view.setOnClickListener { clickListener(view) }
        }

    }
}





