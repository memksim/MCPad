package com.memksim.internal.data.tags

import com.memksim.internal.data.users.UsersTable
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable

private const val DEFAULT_VARCHAR_LENGTH = 12

internal object TagsTable: IntIdTable("tags") {
    val title = varchar("title", DEFAULT_VARCHAR_LENGTH)
    val user  = reference("user_id", UsersTable)
}