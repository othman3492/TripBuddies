import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.othman.tripbuddies.R
import com.othman.tripbuddies.models.Trip
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.trip_photos_list_layout.view.*


class TripPhotoAdapter(val context: Context, val trip: Trip) :
    RecyclerView.Adapter<TripPhotoAdapter.TripPhotoViewHolder>() {

    private var photoList: List<String> = ArrayList()


    override fun getItemCount() = photoList.size + 1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripPhotoViewHolder {

        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.trip_photos_list_layout, parent, false)
        return TripPhotoViewHolder(v)
    }

    // Populate ViewHolder with data depending on the position in the list
    override fun onBindViewHolder(holder: TripPhotoViewHolder, position: Int) {

        holder.bind(Uri.parse(photoList[position]))
    }




    class TripPhotoViewHolder(v: View) : RecyclerView.ViewHolder(v) {


        private var view: View = v


        // Assign data to the views
        fun bind(photo: Uri) {

            Picasso.get().load(photo).into(view.details_photo_image)
        }


    }
}





