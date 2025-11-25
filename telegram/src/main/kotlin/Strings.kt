package com.memksim

object Strings {
    object Greet {
        const val ALREADY_KNOWN = "Здравствуйте! Рад снова вас видеть, %s"
        const val NEW_USER = "Здравствуйте! Приятно познакомиться, %s"
    }

    object Clear {
        const val USER_CLEARED = "Пользовательские данные удалены. \nОтправьте команду /start для начала работы."
    }

    object Message {
        const val THINK = "%s думаю..."
    }

    object Command {
        const val START = "Сохранить информацию о пользователе"
        const val CLEAR = "Очистить информацию о пользователе"
    }

    object Error {
        const val NO_DATA = "Не удалось получить данные о пользователе. \nПовторите попытку позже."
        const val NEED_AUTHORIZE = "Не удалось получить данные о пользователе. \nОтправьте команду /start для начала работы."
        const val MESSAGE_CANNOT_READ = "Не могу прочитать сообщение пользователя. \nПовторите попытку позже."
    }
}