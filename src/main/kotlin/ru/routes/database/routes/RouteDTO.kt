package ru.routes.database.routes

class RouteDTO (
    val id: String,
    val user_id: String,
    val title: String,
    val description: String?,
    val tegs: String,
    val likes: Int?,
    val point1: String,
    val point2: String,
    val public_r: Boolean
)