package org.inu.cafeteria.model

import com.google.gson.annotations.SerializedName

data class Foods(
    val cafeteriaNumber: Int,    // Root key
    val meals: List<Meal>
) {
    data class Meal(
        val order: Int,
        val title: String,
        val menu: String
    )

    /**
     *
    {
    "1":[],
    "2":[],
    "8":[],
    "9":[],
    "10":[],
    "11":[
    {"MENU":"타워함박스테이크 쫄면<br/>마카로니콘샐러드<br/>우동국물<br/>배추김치<br/>백미밥","TITLE":"점심","order":0},
    {"MENU":"셀프비빔밥&계란후라이 감자채햄볶음<br/>브로콜리두부무침<br/>유채된장국<br/>배추김치","TITLE":"저녁","order":1}
    ],
    "stdDate":"20190904",
    "weekDay":"수"
    }

     */

}