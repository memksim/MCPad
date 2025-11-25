package com.memksim.internal.data.users

import com.memksim.api.users.User
import com.memksim.api.users.UsersRepository
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction

internal class UsersRepositoryImpl : UsersRepository {
    override suspend fun getUserByTelegramId(telegramId: Long): User? =
        suspendTransaction {
            val record = UsersTable
                .selectAll()
                .where { UsersTable.telegramId eq telegramId }
                .singleOrNull()

            return@suspendTransaction record?.let {
                User(
                    id = it[UsersTable.id].value,
                    telegramId = it[UsersTable.telegramId],
                    firstName = it[UsersTable.firstName],
                    lastName = it[UsersTable.lastName],
                )
            }
        }

    override suspend fun deleteUserByTelegramId(telegramId: Long): Unit =
        suspendTransaction {
            UsersTable.deleteWhere { UsersTable.telegramId eq telegramId }
        }

    override suspend fun saveUser(user: User): Unit =
        suspendTransaction {
            UsersTable.insert {
                it[telegramId] = user.telegramId
                it[firstName] = user.firstName
                it[lastName] = user.lastName
            }
        }
}