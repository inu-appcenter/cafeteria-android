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

import com.inu.cafeteria.entities.Answer
import com.inu.cafeteria.entities.Question
import com.inu.cafeteria.retrofit.scheme.AnswerResult
import com.inu.cafeteria.retrofit.scheme.QuestionResult

class InteractionResultGatherer(
    private val questions: List<QuestionResult>,
    private val answers: List<AnswerResult>
) {

    fun combine(): List<Question> {
        return questions.map {
            Question(
                id = it.id,
                content = it.content,
                createdAt = it.createdAt,
                answer = answerForThatQuestion(it.id)
            )
        }
    }

    private fun answerForThatQuestion(questionId: Int): Answer? {
        val answerFound = answers.find { it.questionId == questionId } ?: return null

        return Answer(
            id = answerFound.id,
            title = answerFound.title,
            body = answerFound.body,
            read = answerFound.read,
            createdAt = answerFound.createdAt
        )
    }
}