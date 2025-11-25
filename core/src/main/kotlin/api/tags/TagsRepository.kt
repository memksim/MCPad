package com.memksim.api.tags

interface TagsRepository {
    suspend fun getUserTags(userId: Int): List<Tag>
    suspend fun createTag(tag: Tag)
    suspend fun updateTag(tag: Tag)
    suspend fun deleteTag(tag: Tag)
}