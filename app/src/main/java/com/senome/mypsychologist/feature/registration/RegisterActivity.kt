package com.senome.mypsychologist.feature.registration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.senome.mypsychologist.R
import com.senome.mypsychologist.feature.registration.ui.EnterPhoneNumberFragment
import com.senome.mypsychologist.utilits.replaceFragment

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        title = "Авторизация"
        replaceFragment(EnterPhoneNumberFragment(), false)
    }
}