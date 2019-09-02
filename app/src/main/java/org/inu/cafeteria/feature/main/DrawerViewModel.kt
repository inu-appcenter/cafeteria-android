package org.inu.cafeteria.feature.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.inu.cafeteria.common.base.BaseViewModel
import org.inu.cafeteria.model.BarcodeState

class DrawerViewModel : BaseViewModel() {
    val barcodeState = MutableLiveData<BarcodeState>().apply {
        value = BarcodeState(isAvailable = false, isLoading = false, isNetworkDown = false)
    }

    val studentId = MutableLiveData<String>().apply {
        value = "STUDENT ID"
    }
}