package com.memksim.api.users

data class User(
    val id: Int? = null,
    val telegramId: Long? = null,
    val firstName: String,
    val lastName: String? = null,
)