package com.memksim

object Strings {
    object Greet {
        const val ALREADY_KNOWN = "Здравствуйте! Рад снова вас видеть, %s"
        const val NEW_USER = "Здравствуйте! Приятно познакомиться, %s"
        const val NO_DATA = "Здравствуйте! Не удалось получить данные о пользователе. Повторите попытку позже."
    }

    object Message {
        const val CANNOT_READ = "Не могу прочитать сообщение пользователя. Повторите попытку позже."
    }

    object Clear {
        const val USER_CLEARED = "Пользовательские данные удалены. Отправьте команду /start для начала работы."
        const val NO_DATA = "Не удалось получить данные о пользователе. Повторите попытку позже."
    }

    object Command {
        const val START = "Сохранить информацию о пользователе"
        const val CLEAR = "Очистить информацию о пользователе"
    }
}