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

package com.inu.cafeteria.common

import com.inu.cafeteria.entities.OnboardingHint
import com.inu.cafeteria.repository.OnboardingHintRepository
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*
import kotlin.concurrent.timer

class OnboardingHintEventEmitter(
    private val hint: OnboardingHint,
    private val tickInterval: Long = 1000,
    private val onEvent: (OnboardingHint) -> Unit
) : KoinComponent {

    private val onboardingHintRepository: OnboardingHintRepository by inject()

    private var timer: Timer? = when (onboardingHintRepository.doWeHaveToShowHint(hint)) {
        true -> timer(period = tickInterval) {
            if (onboardingHintRepository.doWeHaveToShowHint(hint)) {
                onEvent(hint)
            }
        }
        else -> null
    }

    fun markHintAccepted() {
        onboardingHintRepository.markHintShown(hint)
        destroy()
    }

    fun destroy() {
        timer?.cancel()
        timer = null
    }
}