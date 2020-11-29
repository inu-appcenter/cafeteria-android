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

package com.inu.cafeteria.feature.support.questions

import com.inu.cafeteria.common.base.BaseViewModel
import com.inu.cafeteria.entities.Question
import com.inu.cafeteria.usecase.GetQuestionsAndAnswers
import org.koin.core.inject
import timber.log.Timber

class QuestionsViewModel : BaseViewModel() {

    private val getQuestionsAndAnswers: GetQuestionsAndAnswers by inject()

    fun load() {
        getQuestionsAndAnswers(Unit) {
            it.onSuccess(::handleResult).onError(::handleFailure)
        }
    }

    private fun handleResult(result: List<Question>) {
        Timber.i(result.joinToString { it.toString() })
    }
}