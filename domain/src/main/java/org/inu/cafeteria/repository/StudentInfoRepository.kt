package org.inu.cafeteria.repository

abstract class StudentInfoRepository : Repository() {

    abstract fun getStudentId(): String?
    abstract fun setStudentId(id: String?)

    abstract fun getBarcode(): String?
    abstract fun setBarcode(barcode: String?)

    abstract fun getLoginToken(): String?
    abstract fun setLoginToken(token: String?)
}