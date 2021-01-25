package com.senome.mypsychologist.feature.registration.ui

import android.util.Log
import androidx.fragment.app.Fragment
import com.senome.mypsychologist.MainActivity
import com.senome.mypsychologist.R
import com.senome.mypsychologist.feature.registration.RegisterActivity
import com.senome.mypsychologist.utilits.*
import com.google.firebase.auth.PhoneAuthProvider
import com.senome.mypsychologist.StartActivity
import com.senome.mypsychologist.feature.psihologist.ui.getCommonModel
import kotlinx.android.synthetic.main.fragment_enter_code.*

class EnterCodeFragment(private val phoneNumber: String, val id: String) : Fragment(R.layout.fragment_enter_code) {

    private val mRefList = REF_DATABASE_ROOT.child(NODE_USERS)

    override fun onStart() {
        super.onStart()

        (activity as RegisterActivity).title = phoneNumber

        btn_register2.setOnClickListener{
            if(et_Code.text.toString().length == 6){
                enterCode(et_Code.text.toString())
            }
        }
    }

    private fun enterCode(code: String){

        val credential = PhoneAuthProvider.getCredential(id, code)
        AUTH.signInWithCredential(credential).addOnCompleteListener{
            if(it.isSuccessful){

                val uid = AUTH.currentUser?.uid.toString()
                Log.e("**START**REGISTRATION**", uid)

                mRefList.child(uid).addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot ->

                    val mListItem = dataSnapshot.getCommonModel()
                    Log.e("*****MY**LOG**3*****", mListItem.name)

                    val dateMap = mutableMapOf<String, Any>()
                    dateMap[CHILD_ID] = uid
                    dateMap[CHILD_PHONE] = phoneNumber

                    if (mListItem.name!=""){
                        dateMap[CHILD_USER_NAME] = mListItem.name
                    }
                    else{
                        dateMap[CHILD_USER_NAME] = "user_${phoneNumber.substring(1)}"
                    }


                    dateMap[CHILD_USER_NAME_START] = "user_${phoneNumber.substring(1)}"

                    REF_DATABASE_ROOT.child(NODE_USERS).child(uid).updateChildren(dateMap)
                        .addOnCompleteListener { task->
                            Log.e("**FINISH*REGISTRATION**", uid)
                            if(task.isSuccessful){
                                showToast("Привет!")
                                (activity as RegisterActivity).replaceActivity(StartActivity())
                            } else showToast(task.exception?.message.toString())
                        }


                })

            } else showToast(it.exception?.message.toString())
        }
    }
}