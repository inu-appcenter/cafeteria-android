/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package com.inu.cafeteria.model.json

import com.google.gson.annotations.SerializedName
import java.io.Serializable



/**
 * Represents a single cafeteria.
 */
data class Cafeteria(
    /**
     * The root key for identifying a cafeteria.
     */
    @SerializedName("no") val key: Int,

    /**
     * The name of the cafeteria.
     */
    @SerializedName("name") val name: String,

    /**
     * If -1, it does not support food menu.
     */
    @SerializedName("menu") val supportFoodMenu: Int,

    /**
     * Whether or not the cafeteria supports alarm.
     */
    @SerializedName("alarm") val alarm: Boolean,

    /**
     * The order the server wants the cafeteria be shown to user.
     */
    @SerializedName("order") val order: Int,

    /**
     * Foreground image path. Without base url.
     */
    @SerializedName("img") val imagePath: String,

    /**
     * Background dimmed image path. Without base url.
     */
    @SerializedName("bgimg") val backgroundImagePath: String

) : Serializable

/**
 * Example here.
 *
 * [
 *      {"no":"1",  "menu":1,  "alarm":true, "order":"1" ,"img":"/image/cafe1.png", "bgimg":"/image/cafe1_bg.jpg", "name":"학생식당"},
 *      {"no":"2",  "menu":2,  "alarm":true, "order":"3" ,"img":"/image/cafe2.png", "bgimg":"/image/cafe2_bg.jpg", "name":"카페테리아"},
 *      {"no":"4",  "menu":-1, "alarm":true, "order":"6" ,"img":"/image/cafe4.png", "bgimg":"/image/cafe4_bg.jpg", "name":"소담국밥"},
 *      {"no":"5",  "menu":-1, "alarm":true, "order":"7" ,"img":"/image/cafe5.png", "bgimg":"/image/cafe5_bg.jpg", "name":"카페드림(도서관)"},
 *      {"no":"6",  "menu":-1, "alarm":true, "order":"9" ,"img":"/image/cafe6.png", "bgimg":"/image/cafe6_bg.jpg", "name":"미유카페"},
 *      {"no":"7",  "menu":-1, "alarm":true, "order":"8" ,"img":"/image/cafe7.png", "bgimg":"/image/cafe7_bg.jpg", "name":"카페드림(학식실외)"},
 *      {"no":"8",  "menu":4,  "alarm":false, "order":"2" ,"img":"", "bgimg":"", "name":"제1기숙사"},
 *      {"no":"9",  "menu":3,  "alarm":false, "order":"4" ,"img":"", "bgimg":"", "name":"사범대식당"},
 *      {"no":"10", "menu":5,  "alarm":false, "order":"5" ,"img":"", "bgimg":"", "name":"교직원식당"},
 *      {"no":"11", "menu":0,  "alarm":false, "order":"6" ,"img":"/image/cafe11.png", "bgimg":"", "name":"제2기숙사"}
 * ]
 *
 */