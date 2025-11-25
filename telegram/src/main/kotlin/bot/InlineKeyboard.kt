package com.memksim.bot

import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import com.memksim.Strings

internal sealed class InlineKeyboard(val title: String, val command: String) {
    sealed class Tags(title: String, command: String): InlineKeyboard(title, command) {
        data object Create : Tags(Strings.Keyboard.CREATE, CallbackQuery.CREATE_TAG.text)
        data object Edit : Tags(Strings.Keyboard.EDIT, CallbackQuery.EDIT_TAG.text)
        data object Delete : Tags(Strings.Keyboard.DELETE, CallbackQuery.DELETE_TAG.text)
    }
}

internal fun InlineKeyboard.toInlineButton(): InlineKeyboardButton.CallbackData =
    InlineKeyboardButton.CallbackData(text = title, callbackData = command)