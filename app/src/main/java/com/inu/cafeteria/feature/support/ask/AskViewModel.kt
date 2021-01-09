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

package com.inu.cafeteria.feature.support.ask

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inu.cafeteria.R
import com.inu.cafeteria.common.base.BaseViewModel
import com.inu.cafeteria.common.extension.onChanged
import com.inu.cafeteria.db.SharedPreferenceWrapper
import com.inu.cafeteria.usecase.Ask
import com.inu.cafeteria.util.SingleLiveEvent
import org.koin.core.inject
import timber.log.Timber

class AskViewModel : BaseViewModel() {

    private val db: SharedPreferenceWrapper by inject()
    private val ask: Ask by inject()

    private val _contentValid = MutableLiveData(false)
    val contentValid: LiveData<Boolean> = _contentValid

    val content = ObservableField<String>().apply {
        onChanged {
            _contentValid.value = validateContent()
            storeCurrentText()
        }
    }

    val navigateUpEvent = SingleLiveEvent<Unit>()

    fun load() {
        restorePreviousText()
    }

    private fun restorePreviousText() {
        val storedText = db.getString(KEY_STORED_ASK_TEXT)?.takeIf { it.isNotBlank() } ?: return

        content.set(storedText)
    }

    fun submit() {
        if (handleIfOffline()) {
            Timber.w("Offline! Submit canceled.")
            return
        }

        if (!validateContent()) {
            Timber.w("Content is not valid to submit!!")
            return
        }

        val question = content.get()
        if (question == null) {
            Timber.w("Question is null after validation!!")
            return
        }

        ask(question) {
            it.onSuccess { onSubmitSuccess() }.onError(::handleFailure)
        }
    }

    private fun validateContent(): Boolean {
        val currentContent = content.get()

        if (currentContent.isNullOrBlank()) {
            return false
        }

        val allowedMaxLength = context.resources.getInteger(R.integer.question_max_length)
        if (currentContent.length > allowedMaxLength) {
            return false
        }

        return true
    }

    private fun onSubmitSuccess() {
        clearStoredText()
        notify(R.string.notify_ask_succeeded)
        navigateUpEvent.call()
    }

    private fun storeCurrentText() {
        val currentText = content.get() ?: return

        db.putString(KEY_STORED_ASK_TEXT, currentText);
    }

    private fun clearStoredText() {
        db.putString(KEY_STORED_ASK_TEXT, null);
    }

    companion object {
        private const val KEY_STORED_ASK_TEXT = "key_stored_ask_text"
    }
}