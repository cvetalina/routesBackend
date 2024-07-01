package ru.routes.features.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import ru.routes.database.routes.RouteDTO
import ru.routes.database.routes.Routes

import java.util.*

class RoutesController(private val call: ApplicationCall) {

    suspend fun createRoute() {
        val receive = call.receive<RoutesReceiveRemote>()
        Routes.insert(
            RouteDTO(
                id = UUID.randomUUID().toString(),
                user_id = receive.user_id,
                title = receive.title,
                description = receive.description,
                tegs = receive.tegs,
                point1 = receive.point1,
                point2 = receive.point2,
                public_r = receive.public_r,
                likes = 0
            )
        )
        call.respond(HttpStatusCode.Created, message = "Маршрут создан")
    }

    suspend fun deleteRoute() {
        //удаляет по ссылке
        val id = call.request.queryParameters["id"]
        if (id != null) {
            Routes.deleteRoute(id)
        }
        call.respond(HttpStatusCode.OK, message = "Маршрут удален")
    }

    suspend fun fetchAllRoutes() {
        val routes = Routes.fetchAllRoutes()
        call.respond(routes)
    }

    suspend fun fetchRouteByUser() {
        val id = call.request.queryParameters["user_id"]
        if (id != null) {
            val routes = Routes.fetchRouteByUser(id)
            if (routes != null) {
                call.respond(
                    RoutesResponseRemote(
                        routes.id,
                        routes.point1,
                        routes.point2,
                        routes.title,
                        routes.user_id,
                        routes.tegs,
                        routes.description ?: "",
                        routes.public_r,
                        routes.likes ?: 0
                    )
                )
            } else {
                call.respond(HttpStatusCode.NoContent, "Ничего не найдено")
            }
        } else {
            call.respond(HttpStatusCode.BadRequest, "Неверный параметр")
        }
    }

    suspend fun editRoute() {
        val receive = call.receive<RoutesEditRequest>()
        val route = Routes.editRoute(
            RouteDTO(
                id = receive.id,
                user_id = receive.user_id,
                title = receive.title,
                description = receive.description,
                tegs = receive.tegs,
                point1 = receive.point1,
                point2 = receive.point2,
                public_r = receive.public_r,
                likes = receive.likes
            ), receive.id
        )
        if (route != null) {
            call.respond(route)
        } else {
            call.respond(HttpStatusCode.NotFound, "Маршрут не найден")
        }

    }

}