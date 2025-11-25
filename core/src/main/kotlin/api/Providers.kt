package com.memksim.api

import com.memksim.api.agent.Agent
import com.memksim.api.db.Database
import com.memksim.api.tags.TagsRepository
import com.memksim.api.users.UsersRepository
import com.memksim.internal.agent.DeepseekAgent
import com.memksim.internal.data.PostgresqlDatabase
import com.memksim.internal.data.tags.TagsRepositoryImpl
import com.memksim.internal.data.users.UsersRepositoryImpl

object SingleInstanceProvider {

    private val database = PostgresqlDatabase()
    private val usersRepository = UsersRepositoryImpl()
    private val tagsRepository = TagsRepositoryImpl()
    private val agent = DeepseekAgent()

    fun provideDatabase(): Database = database

    fun provideUsersRepository(): UsersRepository = usersRepository

    fun provideTagsRepository(): TagsRepository = tagsRepository

    fun provideAgent(): Agent = agent
}
