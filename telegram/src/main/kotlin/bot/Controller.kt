package com.memksim.bot

import com.github.kotlintelegrambot.entities.ChatId
import com.memksim.Strings
import com.memksim.api.agent.Agent
import com.memksim.api.users.User
import com.memksim.api.users.UsersRepository
import com.github.kotlintelegrambot.entities.User as TelegramUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

internal class Controller(
    private val repository: UsersRepository,
    private val agent: Agent,
) {
    private val scope = CoroutineScope(Dispatchers.Default)

    private val _messages = MutableSharedFlow<Pair<ChatId.Id, String>>()
    val messages = _messages.asSharedFlow()

    fun handleStart(chatId: ChatId.Id, user: TelegramUser?) {
        user?.let { user ->
            scope.launch(Dispatchers.IO) {
                repository.getUserByTelegramId(user.id)?.let {
                    sendText(chatId, Strings.Greet.ALREADY_KNOWN.format(user.firstName))
                    return@launch
                }
                repository.saveUser(User(telegramId = user.id, name = "${user.firstName} ${user.lastName}"))
                sendText(chatId, Strings.Greet.NEW_USER.format(user.firstName))
            }
        } ?: {
            sendText(chatId, Strings.Greet.NO_DATA)
        }
    }

    fun handleRestart(chatId: ChatId.Id, user: TelegramUser?) {
        user?.let { user ->
            scope.launch(Dispatchers.IO) {
                repository.deleteUserByTelegramId(user.id)
                sendText(chatId, Strings.Restart.USER_CLEARED)
            }
        } ?: {
            sendText(chatId, Strings.Restart.NO_DATA)
        }
    }

    fun handleText(chatId: ChatId.Id, text: String?) {
        text?.let { text ->
            scope.launch {
                val answer = async { agent.askAgent(text) }
                sendText(chatId, answer.await())
            }
        } ?: {
            sendText(chatId, Strings.Message.CANNOT_READ)
        }
    }

    private fun sendText(chatId: ChatId.Id, text: String) {
        scope.launch {
            _messages.emit(chatId to text)
        }
    }

}