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

package com.inu.cafeteria.common.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inu.cafeteria.R
import com.inu.cafeteria.entities.OnboardingHint
import com.inu.cafeteria.repository.OnboardingHintRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * Emits event indicating that we need to display a hint to user.
 */
class OnboardingHintEventEmitter(private val hint: OnboardingHint) : KoinComponent {

    private val onboardingHintRepository: OnboardingHintRepository by inject()

    private val _event = MutableLiveData<OnboardingHintView>(null)
    val event: LiveData<OnboardingHintView> = _event

    fun emitIfAvailable() {
        // A layout which is combined with the hint is shown to the user.
        onboardingHintRepository.markExposed(hint)

        val shouldWeShowHint = onboardingHintRepository.hintHasNotBeenShown(hint)
                && onboardingHintRepository.getExposureCount(hint) >= hint.minimumPreExposure

        if (shouldWeShowHint) {
            emitEvent()
        }
    }

    private fun emitEvent() {
        val hintView = when (hint) {
            OnboardingHint.SortingCafeteria -> OnboardingHintView(R.string.hint_sorting_cafeteria)
            OnboardingHint.ToggleBrightness -> OnboardingHintView(R.string.hint_toggle_brightness)
        }

        _event.postValue(hintView)
    }

    fun markHintAccepted() {
        onboardingHintRepository.markHintShown(hint)
    }
}