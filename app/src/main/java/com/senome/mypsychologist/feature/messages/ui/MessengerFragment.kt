package com.senome.mypsychologist.feature.messages.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.senome.mypsychologist.R
import com.senome.mypsychologist.models.CommonModel
import com.senome.mypsychologist.utilits.*
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.senome.mypsychologist.feature.psihologist.ui.getCommonModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_messenger.*
import kotlinx.android.synthetic.main.item_psychologist_in_dialog.view.*

class MessengerFragment : Fragment(R.layout.fragment_messenger) {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: FirebaseRecyclerAdapter<CommonModel, MessageHolder>

    private val mRefList = REF_DATABASE_ROOT.child(NODE_CORRESPONDENCE).child(USER.id)
    private val mRefUser = REF_DATABASE_ROOT.child(NODE_USERS)
    private val mRefMessage = REF_DATABASE_ROOT.child(NODE_MESSAGES).child(USER.id)

    private var mListItem = listOf<CommonModel>()

    override fun onStart() {
        super.onStart()
        initRecycleView()
    }

    private fun initRecycleView() {

        mRecyclerView = rv_message

        val options = FirebaseRecyclerOptions.Builder<CommonModel>()
            .setQuery(mRefList, CommonModel::class.java)
            .build()

        mAdapter = object : FirebaseRecyclerAdapter<CommonModel, MessageHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_psychologist_in_dialog, parent, false)
                return MessageHolder(view)
            }

            override fun onBindViewHolder(
                holder: MessageHolder,
                position: Int,
                model: CommonModel
            ) {
                mRefList.addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot ->

                    mListItem = dataSnapshot.children.map { it.getCommonModel() }.toList().sortedByDescending { it.timestamp.toString().toLong()}

                    holder.imageUserMessage.isVisible = mListItem[position].isreading != "true"

                    mRefUser.child(mListItem[position].id)
                        .addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot1 ->

                            val newModel = dataSnapshot1.getCommonModel()

                            mRefMessage.child(mListItem[position].id).limitToLast(1)
                                .addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot2 ->

                                    val tempList = dataSnapshot2.children.map { it.getCommonModel() }
                                    newModel.last_message = tempList[0].text

                                    Log.e("*****MY**LOG**1*****", mListItem.toString())

                                    holder.nameUserMessage.text = newModel.name
                                    holder.lastUserMessage.text = newModel.last_message
                                    holder.blockMessage.setOnClickListener {
                                        replaceFragment(PrivateMessagesFragment(newModel, !mListItem[position].isreading.toBoolean()))
                                    }
                                })
                        })
                })



            }
        }

        with(mRecyclerView) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = mAdapter
        }

        mAdapter.startListening()

    }

    class MessageHolder(view: View) : RecyclerView.ViewHolder(view) {
        val blockMessage = view
        val nameUserMessage: TextView = view.tv_message_name
        val lastUserMessage: TextView = view.tv_last_messange
        val imageUserMessage: ImageView = view.iv_message
    }

    override fun onPause() {
        super.onPause()
        activity?.linearLayout?.isVisible = true
    }
    
}