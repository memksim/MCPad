package com.memksim.bot

import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.handlers.HandleCommand

enum class Command(val text: String) {
    START("start"),
}

fun Dispatcher.handleCommand(command: Command, handleCommand: HandleCommand) = command(command.text, handleCommand)