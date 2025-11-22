package com.memksim.bot

import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.User as TelegramUser
import com.memksim.data.UserStorage
import com.memksim.user.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class Controller(
    private val storage: UserStorage,
) {
    private val scope = CoroutineScope(Dispatchers.Default)

    private val _messages = MutableSharedFlow<Pair<ChatId.Id, String>>()
    val messages = _messages.asSharedFlow()

    fun handleStart(chatId: ChatId.Id, user: TelegramUser?) {
        user?.let { user ->
            storage.getUser(user.id)?.let {
                sendText(chatId, "Привет! Рад снова тебя видеть, ${user.firstName}")
                return@handleStart
            }
            storage.addUser(User(
                id = user.id,
                name = "${user.firstName} ${user.lastName}",
            ))
            sendText(chatId, "Привет! Приятно познакомиться, ${user.firstName}")
        } ?: {
            sendText(chatId, "Привет! Не могу получить информацию о тебе. Повтори попытку позже.")
        }
    }

    private fun sendText(chatId: ChatId.Id, text: String) {
        scope.launch {
            _messages.emit(chatId to text)
        }
    }

}