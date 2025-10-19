package com.memksim

import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.entities.ChatId
import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

fun main() {
    val model = YandexModel(
        iamToken = dotenv()["YANDEX_IAM_TOKEN"],
        folderId = dotenv()["FOLDER_ID"],
    )

    bot {
        token = dotenv()["TELEGRAM_TOKEN"]
        dispatch {
            command("start") {
                scope.launch {
                    val r = model.sendRequest()
                    val result = bot.sendMessage(chatId = ChatId.fromId(message.chat.id), text = r.result.alternatives[0].message.text ?: "Не удалось выполнить запрос.")
                    result.fold(
                        ifSuccess = { m ->
                            println("message ${m.text} with id ${m.messageId} was successfully sended")
                        },
                        ifError = { e ->
                            println("error while send message: $e")
                        }
                    )
                }
            }
            command("tools") {
                val result = bot.sendMessage(chatId = ChatId.fromId(message.chat.id), text = "Command /tools was handled")
                result.fold(
                    ifSuccess = { m ->
                        println("message ${m.text} with id ${m.messageId} was successfully sended")
                    },
                    ifError = { e ->
                        println("error while send message: $e")
                    }
                )
            }
        }
    }.startPolling()
}