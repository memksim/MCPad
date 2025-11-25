package com.memksim.bot

import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.handlers.HandleCommand
import com.github.kotlintelegrambot.entities.BotCommand
import com.memksim.Strings

internal enum class Command(val text: String, val description: String) {
    START("start", Strings.Command.START),
    CLEAR("clear", Strings.Command.CLEAR),
}

internal fun Dispatcher.handleCommand(command: Command, handleCommand: HandleCommand) = command(command.text, handleCommand)

internal fun Command.toBotCommand() = BotCommand(
    command = text,
    description = description
)