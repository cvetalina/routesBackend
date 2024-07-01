package ru.routes.features.routes
import ru.routes.features.routes.RoutesController

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRoutesRouting() {
    routing {
        get("/routes") {
            val routesController = RoutesController(call)
            routesController.fetchAllRoutes()
        }
        //маршруты пользователя
        get("/routes/my"){
            val routesController = RoutesController(call)
            routesController.fetchRouteByUser()
        }
        post("/routes/create") {
            val routesController = RoutesController(call)
            routesController.createRoute()
        }
        post("/routes/delete") {
            val routesController = RoutesController(call)
            routesController.deleteRoute()
        }
        post("/routes/edit") {
            val routesController = RoutesController(call)
            routesController.editRoute()
        }
    }
}


