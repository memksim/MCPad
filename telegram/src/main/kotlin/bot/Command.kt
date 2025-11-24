package com.memksim.bot

import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.handlers.HandleCommand

internal enum class Command(val text: String) {
    START("start"),
    RESTART("restart"),
}

internal fun Dispatcher.handleCommand(command: Command, handleCommand: HandleCommand) = command(command.text, handleCommand)