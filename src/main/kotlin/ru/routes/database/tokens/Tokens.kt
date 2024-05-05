package ru.routes.database.tokens

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction


object Tokens : Table() {
    private val id = Tokens.varchar("id", 25)
    private val login = Tokens.varchar("login", 25)
    private val token = Tokens.varchar("tokens", 50)

    fun insert(tokenDTO: TokenDTO) {
        transaction {
            Tokens.insert {
                it[id] = tokenDTO.rowId
                it[login] = tokenDTO.login
                it[token] = tokenDTO.token
            }
        }
    }

}