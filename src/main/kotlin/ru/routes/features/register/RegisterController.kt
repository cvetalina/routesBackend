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
import ru.routes.features.login.LoginResponseRemote
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
        println("dto -> $userDTO")

        if (userDTO != null) {
            call.respond(HttpStatusCode.Conflict, "user already exists")
        } else {
            // создаем токен
            val token = UUID.randomUUID().toString()
            try {
                //вставляем юзера в бд
                Users.insert(
                    UserDTO(
                        login = registerReceiveRemote.login,
                        password = registerReceiveRemote.password,
                        username = registerReceiveRemote.username,
                        email = registerReceiveRemote.email,
                        place = registerReceiveRemote.place
                    )
                )
            } catch (e: ExposedSQLException) {
                call.respond(HttpStatusCode.Conflict, "data base error ")
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
            call.respond(LoginResponseRemote(
                token = token,
                username = registerReceiveRemote.username,
                place = registerReceiveRemote.place,
                login = registerReceiveRemote.login,
                email = registerReceiveRemote.email
            )
            )
        }

    }
}