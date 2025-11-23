package com.memksim

import com.memksim.api.SingleInstanceProvider
import com.memksim.bot.Bot
import com.memksim.bot.Controller
import io.github.cdimascio.dotenv.dotenv


fun main() {
    val dotenv = dotenv()

    val bot = Bot(
        token = dotenv["TELEGRAM_TOKEN"],
        controller = Controller(
            repository = SingleInstanceProvider.provideUsersRepository(),
            agent = SingleInstanceProvider.provideAgent()
        )
    )
    bot.start()
}