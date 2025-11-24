package com.memksim.internal.data.users

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

internal class UserEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object: IntEntityClass<UserEntity>(UsersTable) {
        const val DEFAULT_TELEGRAM_ID = -1L
    }

    var telegramId by UsersTable.telegramId
    var name by UsersTable.name
}