package com.memksim.api.users

interface UsersRepository {
    suspend fun getUserByTelegramId(telegramId: Long): User?
    suspend fun saveUser(user: User)
}

internal object MockUsersRepository : UsersRepository {
    override suspend fun getUserByTelegramId(telegramId: Long): User? = null
    override suspend fun saveUser(user: User) = Unit
}