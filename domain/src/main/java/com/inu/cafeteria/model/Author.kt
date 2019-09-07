package com.inu.cafeteria.model

data class AuthorGroup(
    val phase: Int,
    val authors: List<Author>
)

data class Author(
    val name: String,
    val part: String
)