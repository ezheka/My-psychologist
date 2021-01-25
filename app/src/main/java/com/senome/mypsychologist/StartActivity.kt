package com.senome.mypsychologist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.senome.mypsychologist.feature.registration.RegisterActivity
import com.senome.mypsychologist.models.User
import com.senome.mypsychologist.utilits.*
import kotlinx.android.synthetic.main.activity_main.*

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        AUTH = FirebaseAuth.getInstance()
        REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
        USER = User()
        UID = AUTH.currentUser?.uid.toString()
        initUser()

    }

    private fun initUser() {
        REF_DATABASE_ROOT.child(NODE_USERS).child(UID)
            .addListenerForSingleValueEvent(AppValueEventListener {
                USER = it.getValue(User::class.java) ?: User()
                if (AUTH.currentUser != null) {
                    replaceActivity(MainActivity())
                } else {
                    replaceActivity(RegisterActivity())
                }
            })
    }
}
