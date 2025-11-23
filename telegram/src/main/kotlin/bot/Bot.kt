package com.memksim.bot

import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.message
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.extensions.filters.Filter
import com.github.kotlintelegrambot.logging.LogLevel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal class Bot(
    private val token: String,
    private val controller: Controller
) {

    private val scope = CoroutineScope(Dispatchers.Default)

    private val bot: com.github.kotlintelegrambot.Bot = bot {
        this@bot.token = this@Bot.token
        logLevel = LogLevel.All(LogLevel.Network.Basic)

        dispatch {
            message(Filter.Text) {
                controller.handleText(ChatId.fromId(message.chat.id), message.text)
            }
            handleCommand(Command.START) {
                controller.handleStart(ChatId.fromId(message.chat.id), message.from)
            }
        }
    }

    fun start() {
        bot.startPolling()
        subscribeMessages()
    }

    private fun subscribeMessages() {
        scope.launch {
            controller.messages.collect { (chatId, text) ->
                bot.sendMessage(chatId, text)
            }
        }
    }

}