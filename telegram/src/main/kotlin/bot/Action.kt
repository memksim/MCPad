package com.memksim.bot

import com.github.kotlintelegrambot.entities.ChatId

internal sealed class Action {
    data class SendTextMessage(val chatId: ChatId, val message: String) : Action()
}