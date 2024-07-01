package ru.routes.features.register

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.routes.cache.InMemoryCache
import ru.routes.cache.TokenCache


fun Application.configureRegisterRouting() {
    routing {
        post("/register") {
            val registerController = RegisterController(call)
            registerController.registerNewUser()


        }
    }
}


