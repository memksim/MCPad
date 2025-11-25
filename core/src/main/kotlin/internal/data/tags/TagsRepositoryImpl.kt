package com.memksim.internal.data.tags

import com.memksim.api.tags.Tag
import com.memksim.api.tags.TagsRepository
import com.memksim.internal.data.users.UsersTable
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import org.jetbrains.exposed.v1.jdbc.update

internal class TagsRepositoryImpl : TagsRepository {
    override suspend fun getUserTags(userId: Int): List<Tag> =
        suspendTransaction {
            return@suspendTransaction TagsTable
                .selectAll()
                .where { TagsTable.user eq userId }
                .map {
                Tag(
                    id = it[TagsTable.id].value,
                    userId = it[TagsTable.user].value,
                    title = it[TagsTable.title],
                )
            }
        }

    override suspend fun createTag(tag: Tag): Unit =
        suspendTransaction {
            if (tag.userId == null) throw IllegalArgumentException("User ID is required")
            TagsTable.insert {
                it[TagsTable.title] = tag.title
                it[TagsTable.user] = tag.userId
            } get TagsTable.id
        }

    override suspend fun updateTag(tag: Tag): Unit =
        suspendTransaction {
            TagsTable.update({ TagsTable.id.eq(tag.id) }) {
                it[TagsTable.title] = tag.title
            }
        }

    override suspend fun deleteTag(tag: Tag): Unit =
        suspendTransaction {
            TagsTable.deleteWhere { TagsTable.id.eq(tag.id) }
        }

}