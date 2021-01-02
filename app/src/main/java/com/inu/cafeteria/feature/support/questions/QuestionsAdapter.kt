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

import android.view.ViewGroup
import com.inu.cafeteria.R
import com.inu.cafeteria.common.base.BaseBindingAdapter
import com.inu.cafeteria.common.base.BaseBindingViewHolder
import com.inu.cafeteria.common.extension.resetMaxLines
import com.inu.cafeteria.common.extension.setVisible
import com.inu.cafeteria.databinding.QuestionsAnswerItemBinding

class QuestionsAdapter : BaseBindingAdapter<QuestionView, QuestionsAdapter.QuestionsViewHolder>() {

    var onAnswerRead: (Int) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = QuestionsViewHolder(parent)

    override fun onBindViewHolder(holder: QuestionsViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class QuestionsViewHolder(parent: ViewGroup) :
        BaseBindingViewHolder<QuestionsAnswerItemBinding>(parent, R.layout.questions_answer_item) {

        fun bind(position: Int) {
            val item = getItem(position) ?: return

            binding.question = item

            with(binding.root) {
                setOnClickListener {
                    item.answer?.let(::readAnswer)

                    with(binding.questionPart) {
                        content.resetMaxLines()
                    }

                    with(binding.answerPart) {
                        answerBody.resetMaxLines()
                        answerTitle.resetMaxLines()
                    }
                }
            }
        }

        private fun readAnswer(answer: AnswerView) {
            if (!answer.read) {
                onAnswerRead(answer.id)
                answer.read = true

                with(binding.answerPart.newDot) {
                    // We used notifyItemChanged early, but it makes scroll position change.
                    // Therefore we need to change the visibility manually.
                    setVisible(false)
                }
            }
        }
    }
}