/**
 * This file is part of INU Cafeteria.
 *
 * Copyright (C) 2020 INU Global App Center <potados99@gmail.com>
 *
 * INU Cafeteria is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * INU Cafeteria is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.inu.cafeteria.common.base

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.inu.cafeteria.R
import com.inu.cafeteria.exception.NetworkException
import com.inu.cafeteria.exception.NullBodyException
import com.inu.cafeteria.exception.ResponseFailException
import org.koin.core.KoinComponent
import org.koin.core.inject

abstract class BaseViewModel : ViewModel(), KoinComponent {

    protected val mContext: Context by inject()

    protected open fun handleFailure(e: Exception) {
        val errorMessage = when (e) {
            is NetworkException -> {
                mContext.getString(R.string.fail_server)
            }
            is ResponseFailException -> {
                mContext.getString(R.string.fail_response)
            }
            is NullBodyException -> {
                mContext.getString(R.string.fail_response_body_null)
            }
            else -> {
                e.message
            }
        }

        Toast.makeText(mContext, errorMessage, Toast.LENGTH_LONG).show()
    }

    protected open fun handleFailure(@StringRes message: Int, vararg args: Any?) {
        Toast.makeText(mContext, mContext.getString(message, *args), Toast.LENGTH_SHORT).show()
    }
}