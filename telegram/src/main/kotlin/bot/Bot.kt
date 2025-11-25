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
            handleCommand(Command.CLEAR) {
                controller.handleRestart(ChatId.fromId(message.chat.id), message.from)
            }
        }
    }

    fun start() {
        bot.startPolling()
        subscribeMessages()
        scope.launch {
            bot.setMyCommands(listOf(
                Command.CLEAR.toBotCommand()
            ))
        }
    }

    private fun subscribeMessages() {
        scope.launch {
            controller.actions.collect { action ->
                when (action) {
                    is Action.SendTextMessage -> {
                        bot.sendMessage(action.chatId, action.message)
                    }
                    else -> {}
                }
            }
        }
    }

}