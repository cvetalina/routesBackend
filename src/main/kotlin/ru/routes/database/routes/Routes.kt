package ru.routes.database.routes

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import ru.routes.features.routes.RoutesResponseRemote
import java.lang.Exception


object Routes : Table() {
    private val id = Routes.varchar("id", 50)
    private val user_id = Routes.varchar("user_id", 50)
    private val title = Routes.varchar("title", 100)
    private val description = Routes.text("description")
    private val tegs = Routes.varchar("tegs", 1000)
    private val likes = Routes.integer("likes")
    private val point1 = Routes.varchar("point1", 100)
    private val point2 = Routes.varchar("point2", 100)
    private val public_r = Routes.bool("public")

    fun insert(routeDTO: RouteDTO) {
        transaction {
            Routes.insert {
                it[id] = routeDTO.id
                it[user_id] = routeDTO.user_id
                it[title] = routeDTO.title
                it[description] = routeDTO.description ?: ""
                it[tegs] = routeDTO.tegs
                it[point1] = routeDTO.point1
                it[point2] = routeDTO.point2
                it[public_r] = routeDTO.public_r
            }
        }
    }

    fun fetchRouteById(id: String): RouteDTO? {
        return try {
            transaction {
                val route = Routes.select { Routes.id.eq(id)}.single()
                RouteDTO(
                    id = route[Routes.id],
                    user_id = route[user_id],
                    title = route[title],
                    description = route[description],
                    tegs = route[tegs],
                    point1 = route[point1],
                    point2 = route[point2],
                    public_r = route[public_r],
                    likes = route[likes]
                )
            }
        } catch (e: Exception) {
            null
        }
    }

    fun fetchRouteByUser(user_id: String): RouteDTO? {
        return try {
            transaction {
                val route = Routes.select { Routes.user_id.eq(user_id)}.single()
                RouteDTO(
                    id = route[Routes.id],
                    user_id = route[Routes.user_id],
                    title = route[title],
                    description = route[description],
                    tegs = route[tegs],
                    point1 = route[point1],
                    point2 = route[point2],
                    public_r = route[public_r],
                    likes = route[likes]
                )
            }
        } catch (e: Exception) {
            null
        }
    }

    fun deleteRoute(id: String): Boolean {
        return transaction {
            Routes.deleteWhere { Routes.id.eq(id) }
        } > 0
    }

    fun fetchAllRoutes(): List<RoutesResponseRemote> {
        return try {
            transaction {
                Routes.select{ Routes.public_r.eq(true)}
                    .map {
                        RoutesResponseRemote(
                            id = it[Routes.id],
                            user_id = it[user_id],
                            title = it[title],
                            description = it[description],
                            tegs = it[tegs],
                            point1 = it[point1],
                            point2 = it[point2],
                            public_r = it[public_r],
                            likes = it[likes]
                        )
                    }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun editRoute(routeDTO: RouteDTO, id: String): RouteDTO? {
        transaction {
            Routes.update({ Routes.id.eq(id)}){
                it[user_id] = routeDTO.user_id
                it[title] = routeDTO.title
                it[description] = routeDTO.description ?: ""
                it[tegs] = routeDTO.tegs
                it[point1] = routeDTO.point1
                it[point2] = routeDTO.point2
                it[public_r] = routeDTO.public_r
            }
        }
        return fetchRouteById(id)
    }
}