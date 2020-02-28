import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.libraries.places.api.model.Place
import com.othman.tripbuddies.BuildConfig
import com.othman.tripbuddies.R
import com.othman.tripbuddies.models.City
import com.othman.tripbuddies.models.Message
import com.othman.tripbuddies.models.Trip
import com.othman.tripbuddies.models.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.city_or_buddies_list_layout.view.*
import kotlinx.android.synthetic.main.my_message_layout.view.*
import kotlinx.android.synthetic.main.my_message_layout.view.my_message_timestamp
import kotlinx.android.synthetic.main.other_message_layout.view.*
import java.lang.IllegalArgumentException


class MessageAdapter(private var context: Context) :
    RecyclerView.Adapter<MessageAdapter.BaseMessageViewHolder<*>>() {


    private var messagesList: List<Message> = ArrayList()
    private val MY_MESSAGE = 0
    private val OTHER_MESSAGE = 1


    override fun getItemCount() = messagesList.size

    override fun getItemViewType(position: Int): Int = messagesList[position].type


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseMessageViewHolder<*> {

        return when (viewType) {

            MY_MESSAGE -> {

                val view = LayoutInflater.from(parent.context).inflate(R.layout.my_message_layout, parent, false)
                MyMessageViewHolder(view)
            }

            OTHER_MESSAGE -> {

                val view = LayoutInflater.from(parent.context).inflate(R.layout.other_message_layout, parent, false)
                MyMessageViewHolder(view)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    // Populate ViewHolder with data depending on the position in the list
    override fun onBindViewHolder(holder: BaseMessageViewHolder<*>, position: Int) {

        when (holder) {

            is MyMessageViewHolder -> holder.bind(messagesList[position])
            is OtherMessageViewHolder -> holder.bind(messagesList[position])
            else -> throw IllegalArgumentException()
        }
    }


    fun updateData(list: List<Message>) {

        messagesList = list
        this.notifyDataSetChanged()
    }




    abstract class BaseMessageViewHolder<in T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

        abstract fun bind(message: T)
    }


    class MyMessageViewHolder(v: View): BaseMessageViewHolder<Message>(v) {

        private var view: View = v

        // Assign data to views
        override fun bind(message: Message) {

            view.my_message_content.text = message.content
            view.my_message_timestamp.text = message.timestamp.toString()
        }
    }


    class OtherMessageViewHolder(v: View): BaseMessageViewHolder<Message>(v) {

        private var view: View = v

        // Assign data to views
        override fun bind(message: Message) {

            view.other_message_content.text = message.content

        }
    }
}





