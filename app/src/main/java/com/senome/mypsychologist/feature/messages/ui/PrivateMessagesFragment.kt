package com.senome.mypsychologist.feature.messages.ui

import android.util.Log
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.senome.mypsychologist.R
import com.senome.mypsychologist.feature.messages.presentation.PrivateMessageAdapter
import com.senome.mypsychologist.models.CommonModel
import com.senome.mypsychologist.utilits.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ServerValue
import com.senome.mypsychologist.feature.psihologist.ui.getCommonModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_private_messages.*

class PrivateMessagesFragment(
    private val contact: CommonModel,
    private val isUpdate: Boolean
) :
    Fragment(R.layout.fragment_private_messages) {

    private lateinit var mRefMessages: DatabaseReference
    private lateinit var mAdapter: PrivateMessageAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mMessageListener: AppValueEventListener
    private var mListMessages = emptyList<CommonModel>()


    private val mRefList =
        REF_DATABASE_ROOT.child(NODE_CORRESPONDENCE).child(USER.id).child(contact.id)

    override fun onStart() {
        super.onStart()
        activity?.linearLayout?.isVisible = false

        tv_headerNameUser.text = contact.name
        btn_enter_SMS.setOnClickListener {
            if (et_SMS.text.isBlank()) {
                showToast("Введите сообщение!")
            } else {
                enterSMS(et_SMS.text.toString(), contact.id, TYPE_TEXT) {
                    et_SMS.setText("")
                }
                enterCorrespondence(et_SMS.text.toString(), contact.id)
            }
        }

        btn_exit_private_messages.setOnClickListener {
            replaceFragment(MessengerFragment())
            activity?.tv_headerText?.text = getString(R.string.title_messenger)
        }

        initRecycleView()

        if (isUpdate) UpdateCorrespondence()
    }

    private fun initRecycleView() {
        mRecyclerView = rv_private_message
        mAdapter = PrivateMessageAdapter()
        mRefMessages = REF_DATABASE_ROOT
            .child(NODE_MESSAGES)
            .child(USER.id)
            .child(contact.id)

        with(mRecyclerView) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = mAdapter
        }
        mMessageListener = AppValueEventListener { dataSnapshot ->
            mListMessages =
                dataSnapshot.children.sortedBy { CHILD_TIME }.map { it.getCommonModel() }
            mAdapter.setList(mListMessages)
            mRecyclerView.smoothScrollToPosition(mAdapter.itemCount)
        }

        mRefMessages.addValueEventListener(mMessageListener)
    }

    private fun enterSMS(
        message: String,
        receivingUserID: String,
        typeMessage: String,
        function: () -> Unit
    ) {

        val refDialogUser = "$NODE_MESSAGES/${USER.id}/$receivingUserID"
        val refDialogReceivingUser = "$NODE_MESSAGES/$receivingUserID/${USER.id}"

        val messageKey = REF_DATABASE_ROOT.child(refDialogUser).push().key

        val mapMessage = hashMapOf<String, Any>()
        mapMessage[CHILD_FROM] = USER.id
        mapMessage[CHILD_TYPE] = typeMessage
        mapMessage[CHILD_TEXT] = message.Cleaning()
        mapMessage[CHILD_TIME] = ServerValue.TIMESTAMP

        val mapDialog = hashMapOf<String, Any>()
        mapDialog["$refDialogUser/$messageKey"] = mapMessage
        mapDialog["$refDialogReceivingUser/$messageKey"] = mapMessage

        REF_DATABASE_ROOT
            .updateChildren(mapDialog)
            .addOnSuccessListener { function() }
            .addOnFailureListener {
                showToast(it.message.toString())
            }
    }

    private fun enterCorrespondence(message: String, receivingUserID: String) {
        val refUser = "$NODE_CORRESPONDENCE/${USER.id}/$receivingUserID"
        val refReceivingUser = "$NODE_CORRESPONDENCE/$receivingUserID/${USER.id}"

        val mapUser = hashMapOf<String, Any>()
        val mapReceivingUser = hashMapOf<String, Any>()

        mapUser[CHILD_ID] = receivingUserID
        mapUser[CHILD_TIME] = ServerValue.TIMESTAMP
        mapUser[CHILD_IS_READING] = "true"

        mapReceivingUser[CHILD_ID] = USER.id
        mapReceivingUser[CHILD_TIME] = ServerValue.TIMESTAMP
        mapReceivingUser[CHILD_IS_READING] = "false"

        val commonMap = hashMapOf<String, Any>()
        commonMap[refUser] = mapUser
        commonMap[refReceivingUser] = mapReceivingUser

        REF_DATABASE_ROOT
            .updateChildren(commonMap)
            .addOnFailureListener {
                showToast(it.message.toString())
            }
    }

    private fun UpdateCorrespondence() {
        val refUser = "$NODE_CORRESPONDENCE/${USER.id}/${contact.id}"

        mRefList.addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot ->

            val mListItem = dataSnapshot.getCommonModel()
            Log.e("*****MY**LOG**2*****", mListItem.timestamp.toString())

            val mapUser = hashMapOf<String, Any>()

            mapUser[CHILD_ID] = contact.id
            mapUser[CHILD_TIME] = mListItem.timestamp.toString()
            mapUser[CHILD_IS_READING] = "true"


            val commonMap = hashMapOf<String, Any>()
            commonMap[refUser] = mapUser

            REF_DATABASE_ROOT
                .updateChildren(commonMap)
                .addOnFailureListener {
                    showToast(it.message.toString())
                }
        })
    }

    override fun onPause() {
        super.onPause()
        activity?.linearLayout?.isVisible = true
        mRefMessages.removeEventListener(mMessageListener)
    }

}