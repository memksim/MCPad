package com.memksim.internal.agent

import ai.koog.agents.core.agent.AIAgent
import ai.koog.prompt.executor.llms.all.simpleOllamaAIExecutor
import com.memksim.api.agent.Agent

internal class DeepseekAgent: Agent {
    private val agent = AIAgent(
        llmModel = Deepseek.DEEPSEEK_R1_LATEST,
        systemPrompt = "Ты персональный ассистент пользователя. Отвечай всегда на русском языке.",
        promptExecutor = simpleOllamaAIExecutor()
    )

    override suspend fun askAgent(text: String): String {
        val result = agent.run(text)
        return result
    }
}