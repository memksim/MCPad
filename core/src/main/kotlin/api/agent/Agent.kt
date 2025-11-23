package com.memksim.api.agent

interface Agent {
    suspend fun askAgent(text: String): String
}