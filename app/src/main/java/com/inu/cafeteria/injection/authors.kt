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

package com.inu.cafeteria.injection

import com.inu.cafeteria.model.Author
import com.inu.cafeteria.model.AuthorGroup

val thisAppAuthors = listOf (
    AuthorGroup(
        phase = 1,
        authors = listOf(
            Author(
                name = "신재문",
                part = "Server"
            ),
            Author(
                name = "김선일",
                part = "iOS"
            ),
            Author(
                name = "권순재",
                part = "Android"
            ),
            Author(
                name = "이가윤",
                part = "Android"
            ),
            Author(
                name = "김진웅",
                part = "Design"
            )
        )
    ),
    AuthorGroup(
        phase = 2,
        authors = listOf(
            Author(
                name = "손민재",
                part = "Server"
            ),
            Author(
                name = "김선일",
                part = "iOS"
            ),
            Author(
                name = "권순재",
                part = "Android"
            ),
            Author(
                name = "이가윤",
                part = "Android"
            ),
            Author(
                name = "남효신",
                part = "Design"
            )
        )
    ),
    AuthorGroup(
        phase = 3,
        authors = listOf(
            Author(
                name = "손원희",
                part = "iOS"
            ),
            Author(
                name = "송병준",
                part = "Android"
            ),
            Author(
                name = "이예린",
                part = "Design"
            ),
            Author(
                name = "남효신",
                part = "Design"
            )
        )
    )
)

/**
 * Author:
 *
 * 인천대학교 컴퓨터공학부 17학번 송병준
 *
 * Github: https://github.com/potados99
 * Email: potados99@gmail.com
 *
 * Copyright 2018-2019 INU Appcenter.
 */

val ME = Author(
    name = "송병준",
    part = "Android"
)