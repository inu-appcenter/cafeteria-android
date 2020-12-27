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

package com.inu.cafeteria.common.vision

import java.util.concurrent.Executor
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Inspired by Google.
 * Part of this file came from com.google.mlkit.vision.demo.
 */

/**
 * Wraps an existing executor to provide a {@link #shutdown} method that allows subsequent
 * cancellation of submitted runnables.
 */
class ScopedExecutor(
    private val executor: Executor
) : Executor {

    private val shutdown = AtomicBoolean()

    override fun execute(command: Runnable) {
        // Return early if this object has been shut down.
        if (shutdown.get()) {
            return
        }

        // Check again in case it has been shut down in the mean time.
        executor.execute {
            if (!shutdown.get()) {
                command.run()
            }
        }
    }

    /**
     * After this method is called, no runnables that have been submitted or are subsequently
     * submitted will start to execute, turning this executor into a no-op.
     *
     * Runnables that have already started to execute will continue.
     */
    fun shutdown() {
        shutdown.set(true)
    }
}