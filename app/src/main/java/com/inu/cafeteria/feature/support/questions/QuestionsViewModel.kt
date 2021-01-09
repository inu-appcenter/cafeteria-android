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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inu.cafeteria.common.base.BaseViewModel
import com.inu.cafeteria.entities.Question
import com.inu.cafeteria.extension.format
import com.inu.cafeteria.usecase.GetQuestionsAndAnswers
import com.inu.cafeteria.usecase.MarkAnswerRead
import org.koin.core.inject
import timber.log.Timber
import java.util.*

class QuestionsViewModel : BaseViewModel() {

    private val getQuestionsAndAnswers: GetQuestionsAndAnswers by inject()
    private val markAnswerRead: MarkAnswerRead by inject()

    private val _questions = MutableLiveData<List<QuestionView>>()
    val questions: LiveData<List<QuestionView>> = _questions

    private val _loading = MutableLiveData(true)
    val loading: LiveData<Boolean> = _loading

    fun load(invalidateCache: Boolean = false, afterLoadingFinished: () -> Unit = {}) {
        if (handleIfOffline()) {
            Timber.w("Offline! Fetch canceled.")
            return
        }

        getQuestionsAndAnswers(invalidateCache) {
            _loading.value = true

            it.onSuccess(::handleResult).onError(::handleFailure).finally {
                _loading.value = false
                afterLoadingFinished()
            }
        }
    }

    fun reload(afterReloadFinished: () -> Unit) {
        load(true, afterReloadFinished)
    }

    private fun handleResult(result: List<Question>) {
        val questionsViews = result
            .map { question ->
                QuestionView(
                    content = question.content,
                    answer = question.answer?.let { answer ->
                        AnswerView(
                            id = answer.id,
                            title = answer.title,
                            body = answer.body,
                            read = answer.read,
                            date = dateString(answer.createdAt)
                        )
                    },
                    date = dateString(question.createdAt)
                )
            }

        _questions.value = questionsViews
    }

    private fun dateString(timestamp: Long): String {
        return Date(timestamp).format("yyyy/MM/dd HH:mm")
    }

    fun setAnswerRead(answerId: Int) {
        markAnswerRead(answerId) {
            it.onError(::handleFailure)
        }
    }
}