import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.othman.tripbuddies.R
import com.othman.tripbuddies.models.User
import kotlinx.android.synthetic.main.city_or_buddies_list_layout.view.*
import kotlinx.android.synthetic.main.city_or_buddies_list_layout.view.user_search_item_image
import kotlinx.android.synthetic.main.user_search_item_layout.view.*


class UserSearchAdapter(val context: Context, private val clickListener: (User) -> Unit) :
    RecyclerView.Adapter<UserSearchAdapter.UserSearchViewHolder>() {


    private var buddiesList: List<User> = ArrayList()


    override fun getItemCount() = buddiesList.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserSearchViewHolder {

        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.user_search_item_layout, parent, false)
        return UserSearchViewHolder(v, context)
    }

    // Populate ViewHolder with data depending on the position in the list
    override fun onBindViewHolder(holder: UserSearchViewHolder, position: Int) {


        holder.bind(buddiesList[position], clickListener)
    }


    fun updateData(list: List<User>) {

        buddiesList = list
        this.notifyDataSetChanged()
    }




    class UserSearchViewHolder(v: View, private var context: Context) : RecyclerView.ViewHolder(v), View.OnClickListener {

        private var view: View = v

        init { v.setOnClickListener(this) }

        override fun onClick(v: View?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }


        // Assign data to the views
        fun bind(user: User, clickListener: (User) -> Unit) {

            view.user_search_item_name.text = user.name

            // Display profile picture if not null
            if (user.urlPicture != null) {
                Glide.with(context).load(user.urlPicture)
                    .apply(RequestOptions.circleCropTransform())
                    .into(view.user_search_item_image)

            } else {
                Glide.with(context)
                    .load(R.drawable.blank_picture)
                    .into(view.user_search_item_image)
            }

            // Set view holder on click
            view.setOnClickListener { clickListener(user) }

        }


    }
}




