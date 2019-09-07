package com.inu.cafeteria.repository

import com.inu.cafeteria.model.scheme.ActivateBarcodeParams
import com.inu.cafeteria.model.scheme.ActivateBarcodeResult

abstract class StudentInfoRepository : Repository() {

    abstract fun invalidate()

    abstract fun getStudentId(): String?
    abstract fun setStudentId(id: String?)

    abstract fun getBarcode(): String?
    abstract fun setBarcode(barcode: String?)

    abstract fun getLoginToken(): String?
    abstract fun setLoginToken(token: String?)

    abstract fun activateBarcode(params: ActivateBarcodeParams, callback: DataCallback<ActivateBarcodeResult>)
}