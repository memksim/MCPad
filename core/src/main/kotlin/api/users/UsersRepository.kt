package com.memksim.api.users

interface UsersRepository {
    suspend fun getUserByTelegramId(telegramId: Long): User?
    suspend fun deleteUserByTelegramId(telegramId: Long)
    suspend fun saveUser(user: User)
}