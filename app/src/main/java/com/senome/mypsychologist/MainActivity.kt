package com.senome.mypsychologist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import com.senome.mypsychologist.feature.messages.ui.MessengerFragment
import com.senome.mypsychologist.feature.profile.ui.ProfileFragment
import com.senome.mypsychologist.feature.psihologist.ui.ListPsychologistFragment
import com.senome.mypsychologist.feature.registration.RegisterActivity
import com.senome.mypsychologist.models.User
import com.senome.mypsychologist.utilits.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //private var isActivate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv_headerText.text = getString(R.string.title_messenger)


        replaceFragment(MessengerFragment())
        linearLayout.isVisible = true

        Log.e("****MY**LOG**UID****", UID)

        buttonsClick()
    }



    private fun buttonsClick() {
        btn_Messanger.setOnClickListener {
            if (tv_headerText.text != getString(R.string.title_messenger)) {
                tv_headerText.text = getString(R.string.title_messenger)
                replaceFragment(MessengerFragment())
                linearLayout.isVisible = true
            }
        }
        btn_Psihologists.setOnClickListener {
            if (tv_headerText.text != getString(R.string.title_psihologist)) {
                tv_headerText.text = getString(R.string.title_psihologist)
                replaceFragment(ListPsychologistFragment())
                linearLayout.isVisible = true
            }
        }
        btn_Profile.setOnClickListener {
            if (tv_headerText.text != getString(R.string.title_profile)) {
                tv_headerText.text = getString(R.string.title_profile)
                replaceFragment(ProfileFragment())
                linearLayout.isVisible = true
            }
        }
    }
}