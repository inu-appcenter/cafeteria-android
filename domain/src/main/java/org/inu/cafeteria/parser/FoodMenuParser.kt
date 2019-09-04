package org.inu.cafeteria.parser

import com.google.gson.Gson
import com.google.gson.JsonElement
import org.inu.cafeteria.extension.tryOrNull
import org.inu.cafeteria.model.FoodMenu
import org.inu.cafeteria.model.json.Cafeteria
import org.inu.cafeteria.util.Types
import timber.log.Timber

class FoodMenuParser(
    private val
) : Parser<List<FoodMenu>>() {

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
     * We need somthing like this:
     *
     * root: Array<FoodMenu>
     *  {
     *    cafeteriaNumber: Int      =   0,
     *    corners: Array<Corner>    =   {
     *                                      {
     *                                          title: String = "A corner"
     *                                          menu: Array<String> = {
     *                                              "something",
     *                                              "delicious"
     *                                          }
     *                                          order: Int = 0
     *                                      },
     *                                      {
     *                                          title: String = "B corner"
     *                                          menu: Array<String> = {
     *                                              "good",
     *                                              "thing"
     *                                          }
     *                                          order: Int = 1
     *                                      }
     *                                  }
     *  ,
     *    cafeteriaNumber: Int      =   1,
     *    corners: Array<Corner>    =   {
     *                                      {
     *                                          title: String = "A corner"
     *                                          menu: Array<String> = {
     *                                              "something",
     *                                              "delicious"
     *                                          }
     *                                          order: Int = 0
     *                                      },
     *                                      {
     *                                          title: String = "B corner"
     *                                          menu: Array<String> = {
     *                                              "good",
     *                                              "thing"
     *                                          }
     *                                          order: Int = 1
     *                                      }
     *                                  }
     *
     * Just like that!
     */
    override fun parse(json: JsonElement): List<FoodMenu>? {
        return tryOrNull {

            val foodMenuByCafeteria = mutableListOf<FoodMenu>()

            with (json.asJsonObject) {
                get()
            }







            return@tryOrNull foodMenuByCafeteria
        }
    }
}