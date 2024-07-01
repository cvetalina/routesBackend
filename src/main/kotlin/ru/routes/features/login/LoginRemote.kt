package ru.routes.features.login

import kotlinx.serialization.Serializable

@Serializable
data class LoginReceiveRemote(
    val login: String,
    val password: String
)

@Serializable
data class LoginResponseRemote (
    val token: String,
    val username: String,
    val place: String,
    val login: String,
    val email: String
)
