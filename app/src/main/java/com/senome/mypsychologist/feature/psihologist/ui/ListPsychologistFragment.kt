package com.senome.mypsychologist.feature.psihologist.ui

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.senome.mypsychologist.R
import com.senome.mypsychologist.feature.messages.ui.PrivateMessagesFragment
import com.senome.mypsychologist.models.CommonModel
import com.senome.mypsychologist.utilits.*
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_list_psychologist.*
import kotlinx.android.synthetic.main.item_psichologist.view.*

class ListPsychologistFragment : Fragment(R.layout.fragment_list_psychologist) {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: FirebaseRecyclerAdapter<CommonModel, PsychologistHolder>
    private lateinit var mRefPsychologist: DatabaseReference
    private lateinit var mRefUsers: DatabaseReference

    override fun onStart() {
        super.onStart()
        initRecycleView()
    }

    private fun initRecycleView() {

        mRecyclerView = psich_recycler_view
        mRefPsychologist = REF_DATABASE_ROOT.child(NODE_PSYCHOLOGIST)

        val options = FirebaseRecyclerOptions.Builder<CommonModel>()
            .setQuery(mRefPsychologist, CommonModel::class.java)
            .build()

        mAdapter = object : FirebaseRecyclerAdapter<CommonModel, PsychologistHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PsychologistHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_psichologist, parent, false)
                return PsychologistHolder(view)
            }

            override fun onBindViewHolder(
                holder: PsychologistHolder,
                position: Int,
                model: CommonModel
            ) {

                mRefUsers = REF_DATABASE_ROOT.child(NODE_USERS).child(model.id)
                mRefUsers.addValueEventListener(AppValueEventListener{

                    val contact = it.getCommonModel()

                    holder.name.text = contact.name
                    holder.description.text = model.description

                    holder.containerView.setOnClickListener {
                        replaceFragment(PrivateMessagesFragment(
                            contact,
                            false,
                        ))
                        activity?.tv_headerText?.text = getString(R.string.title_psihologist)+"  "
                    }
                })
            }
        }

        with(mRecyclerView) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = mAdapter
        }
        mAdapter.startListening()
    }

    class PsychologistHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.tv_namePsychologist
        val description: TextView = view.tv_descriptionPsychologist
        val containerView = view
    }

    override fun onPause() {
        super.onPause()
        mAdapter.stopListening()
    }
}

fun DataSnapshot.getCommonModel(): CommonModel =
    this.getValue(CommonModel::class.java) ?: CommonModel()