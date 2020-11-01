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

package com.inu.cafeteria.util

import android.content.Context
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class Request {

    companion object {

        fun post(context: Context, url: String, rawBody: ByteArray): String {
            val future = RequestFuture.newFuture<String>()
            val queue = Volley.newRequestQueue(context)

            val request = object: StringRequest(Method.POST, url, future, future) {
                override fun getBody(): ByteArray {
                    return rawBody
                }

                override fun getBodyContentType(): String {
                    return "text/plain"
                }
            }

            queue.add(request)

            // Sync
            return future.get()
        }
    }
}