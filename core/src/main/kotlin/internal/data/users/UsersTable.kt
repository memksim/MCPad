package com.memksim.internal.data.users

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable

private const val DEFAULT_VARCHAR_LENGTH = 128

internal object UsersTable: IntIdTable("users") {
    val telegramId = long("telegram_id").nullable()
    val firstName  = varchar("first_name", DEFAULT_VARCHAR_LENGTH)
    val lastName   = varchar("last_name", DEFAULT_VARCHAR_LENGTH).nullable()
}