import android.content.Context
import android.net.Uri
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
import kotlinx.android.synthetic.main.trip_photos_list_layout.view.*
import java.lang.IllegalArgumentException


class TripPhotosAdapter(val context: Context, private val itemClickListener: (String) -> Unit,
                         private val addClickListener: (View) -> Unit) :
    RecyclerView.Adapter<TripPhotosAdapter.BaseTripPhotosViewHolder>() {


    private var photosList: List<String> = ArrayList()
    private val TYPE_ITEM = 0
    private val TYPE_ADD = 1


    override fun getItemCount() = photosList.size + 1


    override fun getItemViewType(position: Int): Int {

        return if (position == itemCount - 1) {
            TYPE_ADD
        } else {
            TYPE_ITEM
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseTripPhotosViewHolder {

        return when (viewType) {

            TYPE_ITEM -> {

                val view = LayoutInflater.from(parent.context).inflate(R.layout.trip_photos_list_layout, parent, false)
                TripPhotosViewHolder(view, context)
            }

            TYPE_ADD -> {

                val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_add_button, parent, false)
                TripPhotosFooter(view)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }


    // Populate ViewHolder with data depending on the position in the list
    override fun onBindViewHolder(holder: BaseTripPhotosViewHolder, position: Int) {

        when (holder) {

            is TripPhotosViewHolder -> holder.bind(photosList[position], itemClickListener)
            is TripPhotosFooter -> holder.bind(addClickListener)
            else -> throw IllegalArgumentException()
        }
    }


    fun updateData(list: List<String>) {

        photosList = list
        this.notifyDataSetChanged()
    }




    abstract class BaseTripPhotosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}



    class TripPhotosViewHolder(v: View, private var context: Context) : BaseTripPhotosViewHolder(v), View.OnClickListener {


        private var view: View = v

        init { v.setOnClickListener(this) }

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
        fun bind(clickListener: (View) -> Unit) { view.setOnClickListener { clickListener(view) }}

    }
}





