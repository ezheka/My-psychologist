package com.senome.mypsychologist.feature.registration.ui

import android.util.Log
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.senome.mypsychologist.R
import com.senome.mypsychologist.feature.registration.RegisterActivity
import com.senome.mypsychologist.utilits.replaceFragment
import com.senome.mypsychologist.utilits.showToast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.senome.mypsychologist.MainActivity
import com.senome.mypsychologist.utilits.AUTH
import com.senome.mypsychologist.utilits.replaceActivity
import kotlinx.android.synthetic.main.fragment_enter_phone_number.*
import java.util.concurrent.TimeUnit

class EnterPhoneNumberFragment : Fragment(R.layout.fragment_enter_phone_number) {

    private lateinit var mPhoneNumber:String
    private lateinit var mCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onStart() {
        super.onStart()

        mCallback = object: PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                AUTH.signInWithCredential(credential).addOnCompleteListener{
                    if(it.isSuccessful){
                        showToast("Добро пожаловать!")

                        (activity as RegisterActivity).replaceActivity(MainActivity())
                    } else showToast(it.exception?.message.toString())
                }
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                showToast(p0.message.toString())
            }

            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                replaceFragment(EnterCodeFragment(mPhoneNumber, id))
            }

        }

        btn_register.setOnClickListener { sendCode() }
    }

    private fun sendCode() {
        if(et_NumberPhone.text.toString().isBlank()){
            showToast("Введите номер телефона")
        }
        if((et_NumberPhone.text.dropLast(10).toString()=="+7") || (et_NumberPhone.text.dropLast(10).toString()=="+1")){
            containerBarMain.isVisible=true
            authUser()
        }
        else{
            showToast("Номер должен начинаться на +7")
            Log.e("*******", et_NumberPhone.text.dropLast(10).toString())
        }
    }

    private fun authUser() {
        mPhoneNumber = et_NumberPhone.text.toString()
        PhoneAuthProvider.verifyPhoneNumber(
            PhoneAuthOptions
                .newBuilder(FirebaseAuth.getInstance())
                .setActivity(activity as RegisterActivity)
                .setPhoneNumber(mPhoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setCallbacks(mCallback)
                .build()
        )
    }
}