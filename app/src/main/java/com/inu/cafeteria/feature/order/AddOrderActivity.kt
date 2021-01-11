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
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.mlkit.common.MlKitException
import com.inu.cafeteria.R
import com.inu.cafeteria.common.base.BaseActivity
import com.inu.cafeteria.common.extension.fadeInAndAnimateMargin
import com.inu.cafeteria.common.extension.hideKeyboard
import com.inu.cafeteria.common.extension.observe
import com.inu.cafeteria.common.extension.requestFocusWithKeyboard
import com.inu.cafeteria.databinding.AddOrderActivityBinding
import timber.log.Timber

/**
 * We use Activity directly.
 */
class AddOrderActivity : BaseActivity() {

    private lateinit var binding: AddOrderActivityBinding
    private val viewModel: AddOrderViewModel by viewModels()

    private var cameraRunning: Boolean = false

    private var cameraProvider: ProcessCameraProvider? = null // Reusable
    private var cameraControl: CameraControl? = null
    private var ticketProcessor: TicketRecognitionProcessor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = AddOrderActivityBinding.inflate(layoutInflater).apply {
            setContentView(root)
            initializeView(this)

            lifecycleOwner = this@AddOrderActivity
            vm = viewModel
        }

        startCameraWhenReady()
    }

    private fun initializeView(binding: AddOrderActivityBinding) {
        with(binding.manualPart.cafeteriaSelectionRecycler) {
            adapter = CafeteriaSelectionAdapter().apply {
                onClickRootLayout = viewModel::handleManualCafeteriaSelection
            }
        }

        with(viewModel) {
            observe(cameraViewVisible) {
                it?.takeIf { it } ?: return@observe

                startCamera()

                with(binding.cameraPart.corners) {
                    fadeInAndAnimateMargin(
                        R.dimen.camera_frame_margin,
                        500
                    )
                }
            }

            observe(manualViewVisible) {
                it?.takeIf { it } ?: return@observe

                stopCamera()

                viewModel.fetchCafeteriaSelectionOptions()

                with(binding.manualPart.waitingNumberInput) {
                    post {
                        // Calling requestFocusWithKeyboard right away does not work.
                        requestFocusWithKeyboard()
                    }
                }
            }

            observe(waitingNumberInputDone) {
                it?.takeIf { it } ?: return@observe

                with(binding.manualPart.waitingNumberInput) {
                    hideKeyboard()
                }
            }

            observe(closeEvent) {
                finish()
            }

            observe(toggleFlashEvent) {
                it ?: return@observe

                cameraControl?.enableTorch(it)
            }

            observe(orderSuccessfullyAddedEvent) {
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        startCamera()
    }

    override fun onPause() {
        super.onPause()
        stopCamera()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopCamera()
    }

    // -----------------Code for camera-------------------------------------------------------------
    private fun startCameraWhenReady() {
        observe(viewModel.getProcessorCameraProvider()) {
            cameraProvider = it
            startCamera()
        }
    }

    private fun startCamera() {
        if (cameraRunning) {
            return
        }

        if (!allPermissionsGranted()) {
            return
        }

        try {
            bindAllCameraUseCases(cameraProvider ?: return)
            cameraRunning = true
        } catch (e: Exception) {
            Timber.e("Use case binding failed: ${e.message}")
        }
    }

    private fun bindAllCameraUseCases(cameraProvider: ProcessCameraProvider) {
        cameraProvider.unbindAll()

        bindPreviewUseCase(cameraProvider)
        bindAnalysisUseCase(cameraProvider)

        obtainCameraControl(cameraProvider)
    }

    private fun bindPreviewUseCase(cameraProvider: ProcessCameraProvider) {
        val previewUseCase = Preview.Builder().build().apply {
            setSurfaceProvider(binding.cameraPart.previewView.surfaceProvider)
        }

        cameraProvider.bindToLifecycle(this, getCameraSelector(cameraProvider), previewUseCase)
    }

    @SuppressLint("UnsafeExperimentalUsageError")
    private fun bindAnalysisUseCase(cameraProvider: ProcessCameraProvider) {
        ticketProcessor = TicketRecognitionProcessor().apply {
            onOrderRecognized = viewModel::handleOrderInput
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

    private fun obtainCameraControl(cameraProvider: ProcessCameraProvider) {
        cameraControl = cameraProvider.bindToLifecycle(this, getCameraSelector(cameraProvider)).cameraControl
    }

    private fun stopCamera() {
        cameraProvider?.unbindAll()
        cameraControl?.enableTorch(false)
        ticketProcessor?.stop()

        cameraRunning = false
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
        fun callingIntent(context: Context) = Intent(context, AddOrderActivity::class.java)

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

        @JvmStatic
        @BindingAdapter("cafeteriaToChoose")
        fun setCafeteriaToChoose(view: RecyclerView, cafeteriaToChoose: List<CafeteriaSelectionView>?) {
            cafeteriaToChoose ?: return

            (view.adapter as? CafeteriaSelectionAdapter)?.items = cafeteriaToChoose
        }
    }
}