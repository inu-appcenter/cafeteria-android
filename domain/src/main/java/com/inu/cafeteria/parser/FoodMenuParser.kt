package com.inu.cafeteria.parser

import com.google.gson.JsonElement
import com.inu.cafeteria.extension.tryOrNull
import com.inu.cafeteria.model.FoodMenu
import com.inu.cafeteria.util.Types
import org.jsoup.Jsoup
import timber.log.Timber

class FoodMenuParser : Parser<JsonElement, List<FoodMenu>>() {

    /**
     * We are trying to parse food menus for whole cafeteria.
     * There are some of those not supporting menu information.
     * If the server gives the menus of available cafeteria as json array,
     * like [ {no: 1, data: ...}, {no: 2, data: ...} ], it would be great.
     *
     * The problem is that the json element from the server forms a
     * SINGLE JSON OBJECT with FIXED CAFETERIA NUMBER as a PROPERTY!!!
     * Even more, the menus are written HTML, not plain string. F**K
     *
     * We need something like this:
     *
     * root: Array<FoodMenu>
     *  {
     *      {
     *          cafeteriaNumber: Int      =   0,
     *          corners: Array<Corner>    =   {
     *                                             {
     *                                                 title: String = "A corner"
     *                                                 menu: Array<String> = {
     *                                                     "something",
     *                                                     "delicious"
     *                                                 }
     *                                                 order: Int = 0
     *                                             },
     *                                             {
     *                                                 title: String = "B corner"
     *                                                 menu: Array<String> = {
     *                                                     "good",
     *                                                     "thing"
     *                                                 }
     *                                                 order: Int = 1
     *                                             }
     *                                         }
     *      },
     *  .
     *  .
     *  .
     *  }
     *
     * Just like that!
     * To achieve that, we need to know which cafeteria supports food menu.
     * Two ways:
     *  - get it from field name consists of integer:
     *      Can process by itself, but sucks.
     *  - get list of cafeteria first from repository:
     *      Need another repository but accurate.
     *      Adaptable when cafeteria number changes.
     *
     * We go the former.
     * It sucks but this is a parser
     *
     */
    @SuppressWarnings("Unchecked cast")
    override fun parse(raw: JsonElement, params: Any?): List<FoodMenu>? {
        if (!Types.checkType(params, listOf<Int>())) {
            Timber.w("Params must be List<Int>.")
            return null
        }

        // This is safe.
        // See CafeteriaRepositoryImpl.kt:68.
        val availableCafeteria = params as List<Int>

        val foodMenusByCafeteria = mutableListOf<FoodMenu>()

        return tryOrNull {
            val root = raw.asJsonObject

            availableCafeteria.forEach { cafeteriaNumber ->
                // Parse corners and add them
                // to the foodMenusByCafeteria in every iteration.

                Timber.v("Parsing cafeteria of number $cafeteriaNumber.")

                val corners = mutableListOf<FoodMenu.Corner>()

                root.get(cafeteriaNumber.toString()).asJsonArray.forEach {
                    // Parse a corner and add them to corners in every iteration.

                    val cornerJson = it.asJsonObject

                    val menuInHtml = cornerJson.get("MENU").asString.replace("<br>", "\n")

                    val corner = FoodMenu.Corner(
                        order = cornerJson.get("order").asInt,
                        title = cornerJson.get("TITLE").asString,
                        menu = Jsoup.parse(menuInHtml).text().split("\n")
                    )

                    Timber.v("Parsed corner ${corner.title}.")

                    corners.add(corner)
                }

                foodMenusByCafeteria.add(FoodMenu(cafeteriaNumber, corners))
            }

            // Do sort here. Not there.
            foodMenusByCafeteria.map { original ->
                original.copy(corners = original.corners.sortedBy { it.order })
            }
        }
    }
}