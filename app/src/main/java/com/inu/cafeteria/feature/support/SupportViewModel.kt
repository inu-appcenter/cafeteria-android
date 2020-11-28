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

package com.inu.cafeteria.feature.support

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inu.cafeteria.R
import com.inu.cafeteria.common.base.BaseViewModel

class SupportViewModel : BaseViewModel() {

    private val availableSupportOptionsForThoseLoggedIn = listOf(
        SupportOption(R.string.title_authors, R.id.support_dest),
        SupportOption(R.string.title_feedback, R.id.support_dest)
    )

    private val availableSupportOptionsForThoseNotLoggedIn = listOf(
        SupportOption(R.string.title_authors, R.id.support_dest)
    )

    private val _supportOptions = MutableLiveData<List<SupportOption>>(availableSupportOptionsForThoseLoggedIn)
    val supportOptions: LiveData<List<SupportOption>> = _supportOptions


}