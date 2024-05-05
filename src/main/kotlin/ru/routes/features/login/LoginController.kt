package ru.routes.features.login

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.routes.cache.InMemoryCache
import ru.routes.cache.TokenCache
import ru.routes.database.tokens.TokenDTO
import ru.routes.database.tokens.Tokens
import ru.routes.database.users.Users
import java.util.*

class LoginController(private val call: ApplicationCall) {
    suspend fun performLogin(){
        val receive = call.receive<LoginReceiveRemote>()
        val userDTO = Users.fetchUser(receive.login)

        if (userDTO == null) {
            call.respond(HttpStatusCode.BadRequest, "Пользователь не найден")
        } else {
            if (userDTO.password == receive.password) {
                // генерируем токен
                val token = UUID.randomUUID().toString()
                Tokens.insert(
                    TokenDTO(
                        rowId = UUID.randomUUID().toString(),
                        login = receive.login,
                        token = token
                    )
                )
                call.respond(LoginResponseRemote(token = token))
            } else {
                call.respond(HttpStatusCode.BadRequest, "Неверный пароль")
            }
        }
    }
}