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
import java.lang.IllegalArgumentException


class TripDestinationsAdapter(val context: Context, private val itemClickListener: (City) -> Unit,
                              private val addClickListener: (View) -> Unit) :
    RecyclerView.Adapter<TripDestinationsAdapter.BaseTripDestinationsViewHolder>() {


    private var destinationsList: List<City> = ArrayList()
    private val TYPE_ITEM = 0
    private val TYPE_ADD = 1


    override fun getItemCount() = destinationsList.size + 1


    override fun getItemViewType(position: Int): Int {

        return if (position == itemCount - 1) {
            TYPE_ADD
        } else {
            TYPE_ITEM
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseTripDestinationsViewHolder {

        return when (viewType) {

            TYPE_ITEM -> {

                val view = LayoutInflater.from(parent.context).inflate(R.layout.city_or_buddies_list_layout, parent, false)
                TripDestinationsViewHolder(view, context)
            }

            TYPE_ADD -> {

                val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_add_button, parent, false)
                TripDestinationsFooter(view)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }


    // Populate ViewHolder with data depending on the position in the list
    override fun onBindViewHolder(holder: BaseTripDestinationsViewHolder, position: Int) {

        when (holder) {

            is TripDestinationsViewHolder -> {
                holder.bind(destinationsList[position], itemClickListener)

                // Configure delete button
                holder.itemView.remove_cities_or_buddies_button.visibility = View.VISIBLE
                holder.itemView.remove_cities_or_buddies_button.setOnClickListener {

                    destinationsList.toMutableList().removeAt(position)
                    notifyItemRemoved(position)
                }
            }
            is TripDestinationsFooter -> holder.bind(addClickListener)
            else -> throw IllegalArgumentException()
        }
    }


    fun updateData(list: List<City>) {

        destinationsList = list
        this.notifyDataSetChanged()
    }




    abstract class BaseTripDestinationsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}



    class TripDestinationsViewHolder(v: View, private var context: Context) :
        BaseTripDestinationsViewHolder(v), View.OnClickListener {

        private var view: View = v

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }


        // Assign data to the views
        fun bind(city: City, clickListener: (City) -> Unit) {

            view.city_or_buddies_list_name.text = city.name

            // Display profile picture if not null
            if (city.coverPicture != null) {
                Glide.with(context).load(city.coverPicture).into(view.city_or_buddies_list_image)
            } else {
                Glide.with(context).load(R.drawable.blank_picture)
                    .into(view.city_or_buddies_list_image)
            }

            // Set view holder on click
            view.setOnClickListener { clickListener(city) }
        }
    }


    class TripDestinationsFooter(v: View) : BaseTripDestinationsViewHolder(v), View.OnClickListener {

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





