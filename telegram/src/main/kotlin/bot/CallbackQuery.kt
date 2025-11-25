package com.memksim.bot

internal enum class CallbackQuery(val text: String) {
    CREATE_TAG("create_tag"),
    EDIT_TAG("edit_tag"),
    DELETE_TAG("delete_tag"),
}