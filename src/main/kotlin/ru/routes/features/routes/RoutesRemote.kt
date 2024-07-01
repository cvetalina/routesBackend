package ru.routes.features.routes

import kotlinx.serialization.Serializable

@Serializable
data class RoutesReceiveRemote(
    val point1: String,
    val point2: String,
    val title: String,
    val user_id: String,
    val tegs: String,
    val description: String,
    val public_r: Boolean
)

@Serializable
data class RoutesResponseRemote (
    val id: String,
    val point1: String,
    val point2: String,
    val title: String,
    val user_id: String,
    val tegs: String,
    val description: String,
    val public_r: Boolean,
    val likes: Int
)

data class RoutesEditRequest(
    val id: String,
    val point1: String,
    val point2: String,
    val title: String,
    val user_id: String,
    val tegs: String,
    val description: String,
    val public_r: Boolean,
    val likes: Int
)