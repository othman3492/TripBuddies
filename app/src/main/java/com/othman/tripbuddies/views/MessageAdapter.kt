package com.othman.tripbuddies.views

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.othman.tripbuddies.R
import com.othman.tripbuddies.models.Message
import kotlinx.android.synthetic.main.my_message_layout.view.*
import kotlinx.android.synthetic.main.my_message_layout.view.my_message_timestamp
import kotlinx.android.synthetic.main.other_message_layout.view.*
import java.lang.IllegalArgumentException


class MessageAdapter(private val context: Context, val userId: String) :
    RecyclerView.Adapter<MessageAdapter.BaseMessageViewHolder<*>>() {


    private var messagesList: List<Message> = ArrayList()
    private val myMessageId = 0
    private val otherMessageId = 1


    override fun getItemCount() = messagesList.size

    override fun getItemViewType(position: Int): Int {

        return if (messagesList[position].userId == userId) {
            myMessageId
        } else {
            otherMessageId
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseMessageViewHolder<*> {

        return when (viewType) {

            myMessageId -> {

                val view = LayoutInflater.from(parent.context).inflate(R.layout.my_message_layout, parent, false)
                MyMessageViewHolder(view)
            }

            otherMessageId -> {

                val view = LayoutInflater.from(parent.context).inflate(R.layout.other_message_layout, parent, false)
                OtherMessageViewHolder(view, context)
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
            view.my_message_timestamp.text = message.timestamp
        }
    }


    class OtherMessageViewHolder(v: View, val context: Context): BaseMessageViewHolder<Message>(v) {

        private var view: View = v

        // Assign data to views
        override fun bind(message: Message) {

            view.other_message_content.text = message.content
            view.other_message_timestamp.text = message.timestamp
            view.other_name.text = message.username

            Glide.with(context)
                .load(message.urlProfile)
                .apply(RequestOptions.circleCropTransform())
                .into(view.other_profile_picture)




        }
    }
}





