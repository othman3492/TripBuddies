import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.othman.tripbuddies.R
import com.othman.tripbuddies.models.Trip
import com.othman.tripbuddies.models.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.trip_photos_list_layout.view.*


class TripPhotoAdapter(val context: Context, private val clickListener: (String) -> Unit) :
    RecyclerView.Adapter<TripPhotoAdapter.TripPhotoViewHolder>() {


    private var photoList: List<String> = ArrayList()


    override fun getItemCount() = photoList.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripPhotoViewHolder {

        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.trip_photos_list_layout, parent, false)
        return TripPhotoViewHolder(v, context)
    }

    // Populate ViewHolder with data depending on the position in the list
    override fun onBindViewHolder(holder: TripPhotoViewHolder, position: Int) {

        holder.bind(photoList[position], clickListener)
    }




    class TripPhotoViewHolder(v: View, private var context: Context) : RecyclerView.ViewHolder(v), View.OnClickListener {


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
}





