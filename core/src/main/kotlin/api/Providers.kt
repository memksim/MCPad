package com.memksim.api

import com.memksim.api.agent.Agent
import com.memksim.api.db.Database
import com.memksim.api.users.UsersRepository
import com.memksim.internal.agent.DeepseekAgent
import com.memksim.internal.data.DatabaseImpl
import com.memksim.internal.data.users.UsersRepositoryImpl

object SingleInstanceProvider {

    private val database = DatabaseImpl()
    private val usersRepository = UsersRepositoryImpl()
    private val agent = DeepseekAgent()

    fun provideDatabase(): Database = database

    fun provideUsersRepository(): UsersRepository = usersRepository

    fun provideAgent(): Agent = agent
}
