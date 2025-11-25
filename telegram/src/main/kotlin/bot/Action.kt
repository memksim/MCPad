package com.memksim.bot

import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup

internal sealed class Action {
    data class SendTextMessage(val chatId: ChatId, val message: String) : Action()
    data class SendTextMessageWithKeyboard(val chatId: ChatId, val message: String, val inlineKeyboard: InlineKeyboardMarkup) : Action()
}