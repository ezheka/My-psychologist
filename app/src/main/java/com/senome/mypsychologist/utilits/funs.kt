package com.senome.mypsychologist.utilits

import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.senome.mypsychologist.R
import java.text.SimpleDateFormat
import java.util.*

fun Fragment.showToast(message: String) {
    Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.replaceActivity(activity: AppCompatActivity) {
    val intent = Intent(this, activity::class.java)
    startActivity(intent)
    this.finish()
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, addStack: Boolean = true) {
    if (addStack) {
        supportFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(
                    R.id.dataContainer,
                    fragment
                ).commit()
    } else {
        supportFragmentManager.beginTransaction()
                .replace(
                    R.id.dataContainer,
                    fragment
                ).commit()
    }
}

fun Fragment.replaceFragment(fragment: Fragment) {
    this.fragmentManager?.beginTransaction()
            ?.addToBackStack(null)
            ?.replace(
                R.id.dataContainer,
                fragment
            )?.commit()
}

fun String.asTime(): String {
    val time = Date(this.toLong())
    val timeFormat = SimpleDateFormat("HH:MM", Locale.getDefault())
    return timeFormat.format(time)
}

fun  String.Cleaning(): String{
    /* Чистка от пробелов */
    val cleaning_1 = " $this ".replace("^ +| +$|( )+".toRegex(), " ")
    val cleaning_2 = cleaning_1.removeSurrounding(" ")

    /* Чистка от \n */
    val cleaning_3 = "\n$cleaning_2\n".replace("^\n+|\n+$|(\n)+".toRegex(), "\n\n")
    val cleaning_4 = cleaning_3.removeSurrounding("\n\n")

    val cleaning_5 = cleaning_4.replace("\n ", "\n")

    return cleaning_5
}