package org.inu.cafeteria.repository

import android.app.Activity
import android.content.Context
import androidx.core.content.edit

class StudentInfoRepositoryImpl(
    context: Context
) : StudentInfoRepository() {

    private val pref = context.getSharedPreferences(
        PREFERENCE_STUDENT_INFO,
        Activity.MODE_PRIVATE
    )

    override fun getStudentId(): String? {
        return pref.getString(KEY_ID, EMPTY)
    }
    override fun setStudentId(id: String?) {
        pref.edit {
            putString(KEY_ID, id)
        }
    }

    override fun getBarcode(): String? {
        return pref.getString(KEY_BARCODE, EMPTY)
    }
    override fun setBarcode(barcode: String?) {
        pref.edit {
            putString(KEY_BARCODE, barcode)
        }
    }

    override fun getLoginToken(): String? {
        return pref.getString(KEY_TOKEN, EMPTY)

    }
    override fun setLoginToken(token: String?) {
        pref.edit {
            putString(KEY_TOKEN, token)
        }
    }

    companion object {
        private const val PREFERENCE_STUDENT_INFO = "studentInfo"

        private const val KEY_ID = "studentId"
        private const val KEY_BARCODE = "barcode"
        private const val KEY_TOKEN = "token"

        private const val EMPTY = ""
    }
}
