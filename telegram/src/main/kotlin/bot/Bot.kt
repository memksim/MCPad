package com.memksim.bot

import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.callbackQuery
import com.github.kotlintelegrambot.dispatcher.message
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
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
                controller.handleText(ChatId.fromId(message.chat.id), message.from, message.text)
            }
            handleCommand(Command.START) {
                controller.handleStart(ChatId.fromId(message.chat.id), message.from)
            }
            handleCommand(Command.CLEAR) {
                controller.handleRestart(ChatId.fromId(message.chat.id), message.from)
            }
            handleCommand(Command.TAGS) {
                controller.handleTags(ChatId.fromId(message.chat.id), message.from)
            }
            callbackQuery(CallbackQuery.CREATE_TAG.text) {
                callbackQuery.message?.chat?.id?.let { id ->
                    bot.sendMessage(ChatId.fromId(id), "/create_tag")
                }
            }
            callbackQuery(CallbackQuery.EDIT_TAG.text) {
                callbackQuery.message?.chat?.id?.let { id ->
                    bot.sendMessage(ChatId.fromId(id), "/edit_tag")
                }
            }
            callbackQuery(CallbackQuery.DELETE_TAG.text) {
                callbackQuery.message?.chat?.id?.let { id ->
                    bot.sendMessage(ChatId.fromId(id), "/delete_tag")
                }
            }
        }
    }

    fun start() {
        bot.startPolling()
        subscribeMessages()
        scope.launch {
            bot.setMyCommands(
                listOf(
                    Command.TAGS.toBotCommand(),
                    Command.CLEAR.toBotCommand()
                )
            )
        }
    }

    private fun subscribeMessages() {
        scope.launch {
            controller.actions.collect { action ->
                when (action) {
                    is Action.SendTextMessage -> {
                        bot.sendMessage(
                            chatId = action.chatId,
                            text = action.message
                        )
                    }

                    is Action.SendTextMessageWithKeyboard -> {
                        bot.sendMessage(
                            chatId = action.chatId,
                            text = action.message,
                            replyMarkup = action.inlineKeyboard
                        )
                    }

                    else -> {}
                }
            }
        }
    }

}