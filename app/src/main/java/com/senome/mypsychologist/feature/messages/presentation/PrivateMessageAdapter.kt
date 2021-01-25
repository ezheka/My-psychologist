package com.senome.mypsychologist.feature.messages.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.senome.mypsychologist.R
import com.senome.mypsychologist.models.CommonModel
import com.senome.mypsychologist.utilits.USER
import com.senome.mypsychologist.utilits.asTime
import kotlinx.android.synthetic.main.item_message.view.*

class PrivateMessageAdapter : RecyclerView.Adapter<PrivateMessageAdapter.PrivateMessageHolder>() {

    var mListMessagesCache = emptyList<CommonModel>()

    class PrivateMessageHolder(view: View) : RecyclerView.ViewHolder(view) {

        val blockUserMessage: ConstraintLayout = view.cl_user_messange
        val chatUserMessage: TextView = view.tv_UserMessageText
        val blockInterlocutorMessage: ConstraintLayout = view.cl_interlocutor_messange
        val chatInterlocutorMessage: TextView = view.tv_NamePsihologistMessageText
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrivateMessageHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return PrivateMessageHolder(view)
    }

    override fun onBindViewHolder(holder: PrivateMessageHolder, position: Int) {
        if(mListMessagesCache[position].from == USER.id){
            holder.blockUserMessage.isVisible = true
            holder.blockInterlocutorMessage.isVisible = false
            holder.chatUserMessage.text = mListMessagesCache[position].text
            holder.chatInterlocutorMessage.text =mListMessagesCache[position].timestamp.toString().asTime()
        }
        else{
            holder.blockUserMessage.isVisible = false
            holder.blockInterlocutorMessage.isVisible = true
            holder.chatInterlocutorMessage.text = mListMessagesCache[position].text
            holder.chatUserMessage.text =mListMessagesCache[position].timestamp.toString().asTime()
        }
    }

    override fun getItemCount(): Int = mListMessagesCache.size

    fun setList(list: List<CommonModel>){
        mListMessagesCache = list
        notifyDataSetChanged()
    }
}