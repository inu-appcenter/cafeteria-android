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
 * Copyright 2018-2019 the original author or authors.
 */
val ME = Author(
    name = "송병준",
    part = "Android"
)