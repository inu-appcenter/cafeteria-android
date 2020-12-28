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
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.google.mlkit.common.MlKitException
import com.inu.cafeteria.R
import com.inu.cafeteria.common.base.BaseActivity
import com.inu.cafeteria.common.extension.fadeInAndAnimateMargin
import com.inu.cafeteria.common.extension.observe
import com.inu.cafeteria.databinding.AddOrderActivityBinding
import timber.log.Timber

/**
 * We use Activity directly.
 */
class AddOrderActivity : BaseActivity() {

    private lateinit var binding: AddOrderActivityBinding
    private val viewModel: AddOrderViewModel by viewModels()

    private var ticketProcessor: TicketRecognitionProcessor? = null
    private var cameraProvider: ProcessCameraProvider? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = AddOrderActivityBinding.inflate(layoutInflater).apply {
            setContentView(root)
            initializeView(this)
        }

        startCameraWhenReady()
    }

    private fun initializeView(binding: AddOrderActivityBinding) {
        with(binding.closeButton) {
            setOnClickListener {
                finish()
            }
        }

        with(binding.corners) {
            fadeInAndAnimateMargin(
                R.dimen.camera_frame_margin,
                500
            )
        }
    }

    override fun onResume() {
        super.onResume()
        startCamera()
    }

    override fun onPause() {
        super.onPause()
        ticketProcessor?.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        ticketProcessor?.stop()
    }

    private fun onOrderRecognized(ticket: OrderTicket) {
        Toast.makeText(this, "대기번호: ${ticket.waitingNumber}, POS번호: ${ticket.posNumber}", Toast.LENGTH_SHORT).show()
    }

    // -----------------Code for camera-------------------------------------------------------------
    private fun startCameraWhenReady() {
        observe(viewModel.getProcessorCameraProvider()) {
            cameraProvider = it
            startCamera()
        }
    }

    private fun startCamera() {
        if (!allPermissionsGranted()) {
            return
        }

        try {
            bindAllCameraUseCases(cameraProvider ?: return)
        } catch (e: Exception) {
            Timber.e("Use case binding failed: ${e.message}")
        }
    }

    private fun bindAllCameraUseCases(cameraProvider: ProcessCameraProvider) {
        cameraProvider.unbindAll()

        bindPreviewUseCase(cameraProvider)
        bindAnalysisUseCase(cameraProvider)
    }

    @SuppressLint("RestrictedApi")
    private fun bindPreviewUseCase(cameraProvider: ProcessCameraProvider) {
        val previewUseCase = Preview.Builder().build().apply {
            setSurfaceProvider(binding.previewView.createSurfaceProvider())
        }

        cameraProvider.bindToLifecycle(this, getCameraSelector(cameraProvider), previewUseCase)
    }

    @SuppressLint("UnsafeExperimentalUsageError")
    private fun bindAnalysisUseCase(cameraProvider: ProcessCameraProvider) {
        ticketProcessor = TicketRecognitionProcessor().apply {
            onOrderRecognized = this@AddOrderActivity::onOrderRecognized
        }

        val analyzer = { imageProxy: ImageProxy ->
            try {
                ticketProcessor?.processImageProxy(imageProxy) ?: Unit // To ease compiler warning
            } catch (e: MlKitException) {
                Timber.e("Exception while processing image proxy: ${e.message}")
            }
        }

        val analysisUseCase = ImageAnalysis.Builder().build().apply {
            setAnalyzer(ContextCompat.getMainExecutor(this@AddOrderActivity), analyzer)
        }

        cameraProvider.bindToLifecycle(this, getCameraSelector(cameraProvider), analysisUseCase)
    }

    private fun getCameraSelector(cameraProvider: ProcessCameraProvider): CameraSelector {
        return when {
            cameraProvider.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA) -> {
                CameraSelector.DEFAULT_BACK_CAMERA
            }
            cameraProvider.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA) -> {
                CameraSelector.DEFAULT_FRONT_CAMERA
            }
            else -> {
                CameraSelector.DEFAULT_BACK_CAMERA
            }
        }
    }

    // -----------------Code for permissions--------------------------------------------------------
    override val requiredPermissions: Array<String>
        get() = REQUIRED_PERMISSIONS

    override fun onAllPermissionsGranted() {
        startCamera()
    }

    override fun onPermissionNotGranted() {
        finish()
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}