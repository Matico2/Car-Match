package com.example.carmatch.utils

import android.app.Activity
import android.widget.Toast

fun Activity.showMenssage(menssage: String){
    Toast.makeText(
        applicationContext,
        menssage,
        Toast.LENGTH_SHORT
    ).show()
}