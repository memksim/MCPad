package com.memksim.internal.agent

import ai.koog.prompt.llm.LLMCapability
import ai.koog.prompt.llm.LLMProvider
import ai.koog.prompt.llm.LLModel

internal object Deepseek {
    val DEEPSEEK_R1_LATEST: LLModel = LLModel(
        provider = LLMProvider.Ollama,
        id = "deepseek-r1:latest",
        capabilities = listOf(
            LLMCapability.Temperature,
            LLMCapability.Schema.JSON.Standard,
            LLMCapability.Tools
        ),
        contextLength = 131_072
    )
}