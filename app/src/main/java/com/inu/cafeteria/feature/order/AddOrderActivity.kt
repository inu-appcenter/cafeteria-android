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

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.SparseIntArray
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.util.forEach
import androidx.core.util.set
import com.google.mlkit.common.MlKitException
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.inu.cafeteria.common.base.BaseActivity
import com.inu.cafeteria.databinding.AddOrderActivityBinding
import timber.log.Timber

class AddOrderActivity : BaseActivity() {

    private lateinit var binding: AddOrderActivityBinding
    private val recognizer: TextRecognizer by lazy {
        TextRecognition.getClient()
    }
    private val recognitionHistory = SparseIntArray()
    private var recognitionCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = AddOrderActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT)
                    .show()
                finish()
            }
        }
    }

    @SuppressLint("UnsafeExperimentalUsageError")
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val previewUseCase = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.previewView.createSurfaceProvider())
            }

            val analysisUseCase = ImageAnalysis.Builder().build().also {
                it.setAnalyzer(
                    ContextCompat.getMainExecutor(this),
                    { imageProxy: ImageProxy ->
                        try {
                            processImage(imageProxy)
                        } catch (e: MlKitException) {
                            Toast.makeText(
                                applicationContext,
                                e.localizedMessage,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                )
            }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(this, cameraSelector, previewUseCase)
                cameraProvider.bindToLifecycle(this, cameraSelector, analysisUseCase)
            } catch (exc: Exception) {
                Timber.e("Use case binding failed: $exc")
            }
        }, ContextCompat.getMainExecutor(this))
    }

    @ExperimentalGetImage
    private fun processImage(imageProxy: ImageProxy) {
        recognizer.process(
            InputImage.fromMediaImage(
                imageProxy.image!!,
                imageProxy.imageInfo.rotationDegrees
            )
        )
            .addOnSuccessListener {
                onRecognitionSuccess(it)
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }

    private fun onRecognitionSuccess(text: Text) {
        Timber.v(text.text)

        val num = getNumberIfFound(text) ?: return

        recognitionHistory[num] += 1
        recognitionCount += 1

        binding.recognitionProgress.progress = recognitionCount

        if (recognitionCount < 5) {
            // Wait for next turn. Under 5 is not accurate.
            return
        }


        val mostFrequentNumber = getMostFrequentNumber()

        setResult(1, Intent().apply {
            putExtra("num", mostFrequentNumber)
        })

        finish()
    }

    private fun getMostFrequentNumber(): Int {
        var maxKey = -1
        var maxValue = -1
        recognitionHistory.forEach { key, value ->
            if (value > maxValue) {
                maxKey = key
                maxValue = value
            }
        }

        return maxKey
    }

    private fun getNumberIfFound(text: Text): Int? {
        for (block in text.textBlocks) {
            for (line in block.lines) {
                for (el in line.elements) {
                    val t = el.text

                    return extractNumberAfterColon(t) ?: continue
                }
            }
        }

        return null
    }

    private fun extractNumberAfterColon(text: String): Int? {
        val tester = Regex(": ?([0-9]{4})")

        val numberGroup = tester.find(text)?.groups?.get(1) ?: return null

        return numberGroup.value.toInt()
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}