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

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.inu.cafeteria.R
import com.inu.cafeteria.common.base.BaseAdapter
import com.inu.cafeteria.common.base.BaseViewHolder
import com.inu.cafeteria.common.extension.setVisible
import com.inu.cafeteria.databinding.QuestionsAnswerItemBinding
import kotlinx.android.synthetic.main.answer_item.view.*

class QuestionsAdapter : BaseAdapter<QuestionView, QuestionsAdapter.QuestionsViewHolder>() {

    var onAnswerRead: (Int) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: QuestionsAnswerItemBinding = DataBindingUtil
            .inflate(inflater, R.layout.questions_answer_item, parent, false)

        return QuestionsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuestionsViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class QuestionsViewHolder(
        private val binding: QuestionsAnswerItemBinding
    ) : BaseViewHolder(binding.root) {

        fun bind(position: Int) {
            val item = getItem(position) ?: return

            binding.question = item

            with(binding.root) {
                setOnClickListener {
                    item.answer?.let {
                        readAnswer(it, position)
                    }
                }
            }
        }

        private fun readAnswer(answer: AnswerView, position: Int) {
            if (!answer.read) {
                onAnswerRead(answer.id)
                answer.read = true

                with(itemView.new_dot) {
                    // We used notifyItemChanged early, but it makes scroll position change.
                    // Therefore we need to change the visibility manually.
                    setVisible(false)
                }
            }
        }
    }
}