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

package com.inu.cafeteria.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inu.cafeteria.GlobalConfig
import com.inu.cafeteria.entities.Question
import com.inu.cafeteria.extension.getOrThrow
import com.inu.cafeteria.retrofit.CafeteriaNetworkService
import com.inu.cafeteria.retrofit.scheme.*
import com.inu.cafeteria.util.Cache

class InteractionRepositoryImpl(
    private val networkService: CafeteriaNetworkService,
    private val globalConfig: GlobalConfig
) : InteractionRepository {

    private val questionsCache = Cache<List<QuestionResult>>()
    private val answerCache = Cache<List<AnswerResult>>()

    private val numberOfUnreadAnswers = MutableLiveData(0)

    override fun ask(content: String) {
        networkService.ask(
            AskParams(
                deviceInfo = globalConfig.deviceInfo,
                version = globalConfig.version,
                content = content
            )
        ).getOrThrow()
    }

    override fun getAllQuestions(): List<Question> {
        val questions = cachedFetch(questionsCache) {
            networkService.getAllQuestions().getOrThrow()
        } ?: return listOf()

        val answers = cachedFetch(answerCache) {
            networkService.getAllAnswers().getOrThrow()
        } ?: return listOf()

        return InteractionResultGatherer(questions, answers).combine()
    }

    override fun markAnswerRead(answerId: Int) {
        networkService.markAnswerRead(answerId).getOrThrow()

        // Need to propagate changes to observers(tab icon, app badge, etc...)
        fetchNumberOfUnreadAnswers()

        // Local copy updated. Need new one.
        answerCache.clear()
    }

    override fun fetchNumberOfUnreadAnswers() {
        numberOfUnreadAnswers.postValue(getNumberOfUnreadAnswers())
    }

    private fun getNumberOfUnreadAnswers(): Int {
        val answers = networkService.getAllAnswers(unreadOnly = true).getOrThrow() ?: return 0

        return answers.size
    }

    override fun getNumberOfUnreadAnswersLiveData(): LiveData<Int> {
        return numberOfUnreadAnswers
    }

    @Synchronized
    private fun <T> cachedFetch(cache: Cache<T>, fetch: () -> T?): T? {
        return (if (cache.isValid) cache.get() else null) ?: fetch()?.also(cache::set)
    }
}