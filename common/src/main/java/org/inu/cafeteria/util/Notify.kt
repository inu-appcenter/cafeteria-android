package org.inu.cafeteria.util

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

/**
 * Toast의 wrapper입니다.
 * 객체를 만들어서 써도 되고, 그러지 않아도 됩니다.
 */
class Notify(private val context: Context?) {

    fun short(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    fun short(@StringRes message: Int, vararg formatArgs: Any?) = short(context?.getString(message, *formatArgs))

    fun long(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
    fun long(@StringRes message: Int) = long(context?.getString(message))

    companion object {
        fun short(context: Context?, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
        fun long(context: Context?, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }
}