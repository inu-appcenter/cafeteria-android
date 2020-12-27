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

package com.inu.cafeteria.feature.order

import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.inu.cafeteria.common.vision.VisionProcessorBase
import timber.log.Timber

class TicketRecognitionProcessor : VisionProcessorBase<Text>() {

    private val textRecognizer: TextRecognizer = TextRecognition.getClient()

    var onOrderRecognized: (OrderTicket) -> Unit = {}

    override fun detectInImage(image: InputImage): Task<Text> {
        return textRecognizer.process(image)
    }

    override fun onSuccess(results: Text) {
        extractTicketContentIfFound(results)?.let {
            onOrderRecognized(it)
        }
    }

    private fun extractTicketContentIfFound(text: Text): OrderTicket? {
        val allTexts = text.text

        return OrderTicket(
            waitingNumber = extractWaitingNumber(allTexts) ?: return null,
            posNumber = extractPosNumber(allTexts) ?: return null
        ).apply {
            Timber.i("Ticket recognized: (waitingNumber: ${waitingNumber}, posNumber: ${posNumber})")
        }
    }

    private fun extractWaitingNumber(text: String): Int? {
        return extractCapturedGroup(": ?([0-9]{4})", text)?.toInt()
    }

    private fun extractPosNumber(text: String): Int? {
        return extractCapturedGroup("POS.* ?: ?([0-9]{2})", text)?.toInt()
    }

    private fun extractCapturedGroup(pattern: String, text: String): String? {
        val tester = Regex(pattern)

        val numberGroup = tester.find(text)?.groups?.get(1) ?: return null

        return numberGroup.value
    }

    override fun onFailure(e: Exception) {
        Timber.w("Text detection failed: ${e.message}")
    }

    override fun stop() {
        super.stop()
        textRecognizer.close()
    }
}
