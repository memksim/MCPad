package com.memksim

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.apache5.Apache5
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Request(
    val modelUri: String,
    val tools: List<Tool>,
    val messages: List<Message>
)

@Serializable
data class Message(
    val role: String,
    val text: String
)

@Serializable
data class Tool(
    val function: FunctionDescription
)

@Serializable
data class FunctionDescription(
    val name: String,
    val description: String,
    val parameters: FunctionParameters
)

@Serializable
data class FunctionParameters(
    val type: String, // например: "object"
    val properties: Map<String, FunctionProperty>,
    val required: List<String>
)

@Serializable
data class FunctionProperty(
    val type: String,        // например: "string"
    val description: String  // например: "Название города, например, Москва"
)

class YandexModel(
    val iamToken: String,
    val folderId: String
) {

    suspend fun sendRequest(): Response {
        val client = HttpClient(Apache5) {
            install(ContentNegotiation) {
                json(Json { prettyPrint = true })
            }
            install(io.ktor.client.plugins.logging.Logging) {
                level = io.ktor.client.plugins.logging.LogLevel.ALL
            }
        }

        val response = client.post("https://llm.api.cloud.yandex.net/foundationModels/v1/completion") {
            contentType(ContentType.Application.Json)
            headers.append("Authorization", "Bearer $iamToken")
            //setBody(mock)
        }

        return response.body<Response>()
    }

}

@Serializable
data class Response(
    val result: Result,
)

@Serializable
data class Result(
    val alternatives: List<Alternative>,
    val usage: Usage,
    val modelVersion: String
)

@Serializable
data class Usage(
    val inputTextTokens: String,
    val completionTokens: String,
    val totalTokens: String,
    val completionTokensDetails: CompletionDetails,
)

@Serializable
data class CompletionDetails(
    val reasoningTokens: String
)

@Serializable
data class Alternative(
    val message: ResponseMessage,
    val status: String
)

@Serializable
data class ResponseMessage(
    val role: String,
    val text: String? = null,
    val toolCallList: ToolCallList? = null
)

@Serializable
data class ToolCallList(
    val toolCalls: List<FunctionCall>
)

@Serializable
data class FunctionCall(
    val name: String,
    val arguments: List<String>,
)

