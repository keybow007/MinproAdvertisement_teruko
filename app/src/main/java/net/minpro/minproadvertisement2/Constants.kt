package net.minpro.minproadvertisement2

import android.content.Context
import android.widget.Toast

/**
 * Created by keybowNew on 2018/03/09.
 */
enum class IntentKey{
    PLACE,
    SPECIALITY,
    MESSAGE_FORMER,
    MESSAGE_LATTER,
    MESSAGE,
    MESSAGE_SPEED
}

fun makeToast(context: Context, message: String){
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

