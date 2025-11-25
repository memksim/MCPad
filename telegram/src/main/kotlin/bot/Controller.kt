package com.memksim.bot

import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import com.github.kotlintelegrambot.entities.keyboard.KeyboardButton
import com.memksim.Strings
import com.memksim.api.agent.Agent
import com.memksim.api.tags.TagsRepository
import com.memksim.api.users.User
import com.memksim.api.users.UsersRepository
import com.memksim.emojis
import com.github.kotlintelegrambot.entities.User as TelegramUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class Controller(
    private val usersRepository: UsersRepository,
    private val tagsRepository: TagsRepository,
    private val agent: Agent,
) {
    private val scope = CoroutineScope(Dispatchers.Default)

    private val _actions = MutableStateFlow<Action?>(null)
    val actions = _actions.asStateFlow()

    fun handleStart(chatId: ChatId.Id, user: TelegramUser?) {
        user?.let { user ->
            scope.launch(Dispatchers.IO) {
                if (checkUser(user.id)) {
                    sendText(chatId, Strings.Greet.ALREADY_KNOWN.format(user.firstName))
                    return@launch
                }

                usersRepository.saveUser(
                    User(
                        telegramId = user.id,
                        firstName = user.firstName,
                        lastName = user.lastName
                    )
                )
                sendText(chatId, Strings.Greet.NEW_USER.format(user.firstName))
            }
        } ?: sendText(chatId, Strings.Error.NO_DATA)
    }

    fun handleRestart(chatId: ChatId.Id, user: TelegramUser?) {
        user?.let { user ->
            scope.launch(Dispatchers.IO) {
                usersRepository.deleteUserByTelegramId(user.id)
                sendText(chatId, Strings.Clear.USER_CLEARED)
            }
        } ?: sendText(chatId, Strings.Error.NO_DATA)
    }

    fun handleTags(chatId: ChatId.Id, user: TelegramUser?) {
        user?.let { user ->
            scope.launch(Dispatchers.IO) {
                usersRepository.getUserByTelegramId(user.id)?.let { record ->
                    record.id?.let { id ->
                        val tags = tagsRepository.getUserTags(id)
                        val action = if (tags.isNotEmpty()) {
                            Action.SendTextMessageWithKeyboard(
                                chatId = chatId,
                                message = Strings.Tags.DATA + tags.joinToString("\n") { it.title },
                                inlineKeyboard = InlineKeyboardMarkup.create(
                                    listOf(
                                        InlineKeyboard.Tags.Edit.toInlineButton(),
                                        InlineKeyboard.Tags.Delete.toInlineButton()
                                    ),
                                    listOf(InlineKeyboard.Tags.Create.toInlineButton()),
                                )
                            )
                        } else {
                            Action.SendTextMessageWithKeyboard(
                                chatId = chatId,
                                message = Strings.Tags.EMPTY,
                                inlineKeyboard = InlineKeyboardMarkup.create(
                                    listOf(InlineKeyboard.Tags.Create.toInlineButton()),
                                )
                            )
                        }
                        _actions.emit(action)
                    } ?: sendText(chatId, Strings.Error.NO_DATA)
                } ?: sendText(chatId, Strings.Error.NEED_AUTHORIZE)
            }
        } ?: sendText(chatId, Strings.Error.NO_DATA)
    }

    fun handleText(chatId: ChatId.Id, user: TelegramUser?, text: String?) {
        user?.let { user ->
            text?.let { text ->
                scope.launch {
                    if (!checkUser(user.id)) {
                        sendText(chatId, Strings.Error.NEED_AUTHORIZE)
                        return@launch
                    }
                    askAgent(chatId, text)
                }
            } ?: {
                sendText(chatId, Strings.Error.MESSAGE_CANNOT_READ)
            }
        } ?: sendText(chatId, Strings.Error.NO_DATA)
    }

    private fun sendText(chatId: ChatId.Id, text: String) {
        scope.launch {
            _actions.emit(Action.SendTextMessage(chatId = chatId, message = text))
        }
    }

    private suspend fun checkUser(telegramId: Long): Boolean {
        return usersRepository.getUserByTelegramId(telegramId) != null
    }

    private suspend fun askAgent(chatId: ChatId.Id, text: String) = runCatching {
        sendText(chatId, Strings.Message.THINK.format(String(Character.toChars(emojis.random()))))
        agent.askAgent(text)
    }.onSuccess { response ->
        sendText(chatId, response)
    }.onFailure {
        delay(1_000)
        sendText(chatId, Strings.Error.MESSAGE_CANNOT_ASK)
    }

}