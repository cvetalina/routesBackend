package ru.routes

import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import org.jetbrains.exposed.sql.Database
import ru.routes.features.login.configureLoginRouting
import ru.routes.features.register.configureRegisterRouting
import ru.routes.features.routes.configureRoutesRouting
import ru.routes.plugins.*

fun main() {
    Database.connect(url = "jdbc:postgresql://localhost:5432/routes", driver = "org.postgresql.Driver",
        user = "postgres", password = "root")
    embeddedServer(CIO, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureRouting()
    configureLoginRouting()
    configureRegisterRouting()
    configureRoutesRouting()
}
