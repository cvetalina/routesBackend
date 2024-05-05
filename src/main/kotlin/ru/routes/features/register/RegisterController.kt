package ru.routes.features.register

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import ru.routes.database.tokens.TokenDTO
import ru.routes.database.tokens.Tokens
import ru.routes.database.users.UserDTO
import ru.routes.database.users.Users
import ru.routes.utils.isValidEmail
import java.util.*

class RegisterController(private val call: ApplicationCall) {
    suspend fun registerNewUser() {
        val registerReceiveRemote = call.receive<RegisterReceiveRemote>()
        if (!registerReceiveRemote.email.isValidEmail()) {
            call.respond(HttpStatusCode.BadRequest, "Email is not valid")
        }
        //            проверяем есть ли такой пользователь
        // идем напрямую в базу
        val userDTO = Users.fetchUser(registerReceiveRemote.login)

        if (userDTO != null) {
            call.respond(HttpStatusCode.Conflict, "Такой пользователь уже существует")
        } else {
            // создаем токен
            val token = UUID.randomUUID().toString()
            try {
                //вставляем юзера в бд
                Users.insert(
                    UserDTO(
                        login = registerReceiveRemote.login,
                        password = registerReceiveRemote.password,
                        email = registerReceiveRemote.email,
                        username = "",
                        place = registerReceiveRemote.place
                    )
                )
            } catch (e: ExposedSQLException) {
                call.respond(HttpStatusCode.Conflict, "Такой пользователь уже существует")
            }

            // вставляем запись что у нас есть такой токен
            // берем таблицу токенс
            Tokens.insert(
                TokenDTO(
                    rowId = UUID.randomUUID().toString(),
                    login = registerReceiveRemote.login,
                    token = token
                )
            )
            call.respond(RegisterResponseRemote(token = token))
        }

    }
}