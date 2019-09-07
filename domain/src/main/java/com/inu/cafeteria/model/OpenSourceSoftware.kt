package com.inu.cafeteria.model

data class OpenSourceSoftware(
    val name: String,
    val contact: String = "",
    val copyright: String,
    val devWebSite: String = "",
    val licenseName: String,
    val licenseReference: String,
    val sourceCodeReference: String
)