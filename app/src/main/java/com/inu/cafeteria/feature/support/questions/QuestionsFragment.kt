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

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.inu.cafeteria.common.base.BaseFragment
import com.inu.cafeteria.databinding.QuestionsFragmentBinding

class QuestionsFragment : BaseFragment() {

    private val viewModel: QuestionsViewModel by viewModels()

    private val adapter = QuestionsAdapter()

    override fun onNetworkStateChange(available: Boolean) {
        if (available) {
            viewModel.load()
        }
    }

    override fun onCreateView(create: ViewCreator): View {
        return create.createView<QuestionsFragmentBinding> {
            initializeView(this)
            vm = viewModel
        }
    }

    private fun initializeView(binding: QuestionsFragmentBinding) {
        with(binding.questionsRecycler) {
            adapter = this@QuestionsFragment.adapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        with(adapter) {
            onAnswerRead = {
                viewModel.setAnswerRead(it)
            }

            emptyView = binding.emptyView
            loadingView = binding.loadingView
        }

        with(binding.swipeRefresher) {
            setOnRefreshListener {
                viewModel.reload {
                    isRefreshing = false
                }
            }
        }
    }

    companion object {

        @JvmStatic
        @BindingAdapter("questions")
        fun setQuestions(view: RecyclerView, questions: List<QuestionView>?) {
            questions?.let {
                (view.adapter as? QuestionsAdapter)?.data = it
            }
        }

        @JvmStatic
        @BindingAdapter("isQuestionsLoading")
        fun setLoading(view: RecyclerView, isQuestionsLoading: Boolean?) {
            (view.adapter as? QuestionsAdapter)?.isLoading = isQuestionsLoading ?: true
        }
    }

}