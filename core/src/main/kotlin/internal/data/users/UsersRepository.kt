package com.memksim.internal.data.users

import com.memksim.api.users.User
import com.memksim.api.users.UsersRepository
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction

internal class UsersRepositoryImpl : UsersRepository {
    override suspend fun getUserByTelegramId(telegramId: Long): User? =
        suspendTransaction {
            val record = UserEntity.find { UsersTable.telegramId eq telegramId }.firstOrNull()
            return@suspendTransaction record?.let {
                User(
                    id = it.id.value,
                    telegramId = it.telegramId,
                    name = it.name,
                )
            }
        }

    override suspend fun deleteUserByTelegramId(telegramId: Long): Unit =
        suspendTransaction {
            val record = UserEntity.find { UsersTable.telegramId eq telegramId }.firstOrNull()
            record?.delete()
        }

    override suspend fun saveUser(user: User): Unit =
        suspendTransaction {
            UserEntity.new {
                name = user.name
                telegramId = user.telegramId ?: UserEntity.DEFAULT_TELEGRAM_ID
            }
        }
}