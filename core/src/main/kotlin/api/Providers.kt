package com.memksim.api

import com.memksim.api.agent.Agent
import com.memksim.api.users.MockUsersRepository
import com.memksim.api.users.UsersRepository
import com.memksim.internal.agent.DeepseekAgent

object SingleInstanceProvider {

    private val usersRepository = MockUsersRepository
    private val agent = DeepseekAgent()

    fun provideUsersRepository(): UsersRepository = usersRepository

    fun provideAgent(): Agent = agent
}
