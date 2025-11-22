package com.memksim

import com.memksim.bot.Bot
import com.memksim.bot.Controller
import com.memksim.data.UserStorage
import io.github.cdimascio.dotenv.dotenv

fun main() {
    val dotenv = dotenv()

    val bot = Bot(
        token = dotenv["TELEGRAM_TOKEN"],
        controller = Controller(UserStorage())
    )
    bot.start()
}