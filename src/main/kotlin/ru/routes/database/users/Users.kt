package ru.routes.database.users

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.lang.Exception


object Users : Table() {
    private val login = Users.varchar("login", 50)
    private val password = Users.varchar("password", 50)
    private val username = Users.varchar("username", 50)
    private val email = Users.varchar("email", 50)
    private val place = Users.varchar("place", 50)

    fun insert(userDTO: UserDTO) {
        transaction {
            Users.insert {
                it[login] = userDTO.login
                it[password] = userDTO.password
                it[username] = userDTO.username
//                it[email] = userDTO.email ?: ""
                it[email] = userDTO.email
                it[place] = userDTO.place
            }
        }
    }

    fun fetchUser(id: String): UserDTO? {
        return try {
            transaction {
                val userModel = Users.select { Users.login.eq(id) }.single()
                UserDTO(
                    login = userModel[Users.login],
                    password = userModel[password],
                    username = userModel[username],
                    email = userModel[email],
                    place = userModel[place]
                )
            }
        } catch (e: Exception) {
            null
        }
    }
}