import com.memksim.api.tags.Tag
import com.memksim.internal.data.tags.TagsRepositoryImpl
import com.memksim.internal.data.tags.TagsTable
import com.memksim.internal.data.users.UsersTable
import kotlinx.coroutines.test.runTest
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class TagsRepositoryTest {

    private val repository = TagsRepositoryImpl()

    companion object {
        @JvmStatic
        @BeforeAll
        fun setupDb() {
            Database.connect(
                url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;",
                driver = "org.h2.Driver",
                user = "",
                password = ""
            )

            transaction {
                SchemaUtils.create(UsersTable, TagsTable)
            }
        }
    }

    @BeforeEach
    fun cleanTables() {
        transaction {
            SchemaUtils.drop(TagsTable, UsersTable)
            SchemaUtils.create(UsersTable, TagsTable)
        }
    }

    private fun createTestUser(userId: Int = 1) {
        transaction {
            UsersTable.insert {
                it[telegramId] = 12345L + userId
                it[firstName] = "TestUser$userId"
                it[lastName] = "Test"
            }
        }
    }

    @Test
    fun `getUserTags returns empty list when user has no tags`() = runTest {
        val userId = 1
        createTestUser(userId)

        val tags = repository.getUserTags(userId)

        assertEquals(emptyList(), tags)
    }

    @Test
    fun `getUserTags returns user's tags`() = runTest {
        val userId = 1
        createTestUser(userId)

        // Create tags for user
        repository.createTag(Tag(userId = userId, title = "Work"))
        repository.createTag(Tag(userId = userId, title = "Personal"))

        val tags = repository.getUserTags(userId)

        assertEquals(2, tags.size)
        assertTrue(tags.any { it.title == "Work" })
        assertTrue(tags.any { it.title == "Personal" })
        tags.forEach { assertEquals(userId, it.userId) }
    }

    @Test
    fun `getUserTags returns only user's own tags`() = runTest {
        val userId1 = 1
        val userId2 = 2
        createTestUser(userId1)
        createTestUser(userId2)

        // Create tags for different users
        repository.createTag(Tag(userId = userId1, title = "User1Tag"))
        repository.createTag(Tag(userId = userId2, title = "User2Tag"))

        val user1Tags = repository.getUserTags(userId1)
        val user2Tags = repository.getUserTags(userId2)

        assertEquals(1, user1Tags.size)
        assertEquals("User1Tag", user1Tags.first().title)

        assertEquals(1, user2Tags.size)
        assertEquals("User2Tag", user2Tags.first().title)
    }

    @Test
    fun `createTag successfully creates new tag`() = runTest {
        val userId = 1
        createTestUser(userId)

        val newTag = Tag(userId = userId, title = "Important")
        repository.createTag(newTag)

        val tags = repository.getUserTags(userId)
        assertEquals(1, tags.size)
        assertEquals("Important", tags.first().title)
        assertEquals(userId, tags.first().userId)
        assertNotNull(tags.first().id)
    }

    @Test
    fun `createTag throws exception when userId is null`() = runTest {
        val tagWithoutUser = Tag(userId = null, title = "Invalid")

        assertFailsWith<IllegalArgumentException>("User ID is required") {
            repository.createTag(tagWithoutUser)
        }
    }

    @Test
    fun `updateTag successfully updates existing tag`() = runTest {
        val userId = 1
        createTestUser(userId)

        // Create a tag first
        repository.createTag(Tag(userId = userId, title = "OldTitle"))
        val tags = repository.getUserTags(userId)
        val tagToUpdate = tags.first()

        // Update the tag
        val updatedTag = tagToUpdate.copy(title = "NewTitle")
        repository.updateTag(updatedTag)

        // Verify update
        val updatedTags = repository.getUserTags(userId)
        assertEquals(1, updatedTags.size)
        assertEquals("NewTitle", updatedTags.first().title)
        assertEquals(tagToUpdate.id, updatedTags.first().id)
    }

    @Test
    fun `updateTag with non-existent id does nothing`() = runTest {
        val userId = 1
        createTestUser(userId)

        // Try to update a tag that doesn't exist
        val nonExistentTag = Tag(id = 999, userId = userId, title = "DoesNotExist")
        repository.updateTag(nonExistentTag) // Should not throw

        // Verify no changes
        val tags = repository.getUserTags(userId)
        assertEquals(emptyList(), tags)
    }

    @Test
    fun `deleteTag successfully deletes existing tag`() = runTest {
        val userId = 1
        createTestUser(userId)

        // Create tags
        repository.createTag(Tag(userId = userId, title = "ToDelete"))
        repository.createTag(Tag(userId = userId, title = "ToKeep"))

        var tags = repository.getUserTags(userId)
        assertEquals(2, tags.size)

        // Delete one tag
        val tagToDelete = tags.find { it.title == "ToDelete" }!!
        repository.deleteTag(tagToDelete)

        // Verify deletion
        tags = repository.getUserTags(userId)
        assertEquals(1, tags.size)
        assertEquals("ToKeep", tags.first().title)
    }

    @Test
    fun `deleteTag with non-existent id does nothing`() = runTest {
        val userId = 1
        createTestUser(userId)

        // Create a tag
        repository.createTag(Tag(userId = userId, title = "TestTag"))
        val initialTags = repository.getUserTags(userId)
        assertEquals(1, initialTags.size)

        // Try to delete a tag that doesn't exist
        val nonExistentTag = Tag(id = 999, userId = userId, title = "DoesNotExist")
        repository.deleteTag(nonExistentTag) // Should not throw

        // Verify no changes
        val finalTags = repository.getUserTags(userId)
        assertEquals(1, finalTags.size)
        assertEquals(initialTags.first().title, finalTags.first().title)
    }

    @Test
    fun `complete CRUD flow`() = runTest {
        val userId = 1
        createTestUser(userId)

        // Create
        repository.createTag(Tag(userId = userId, title = "Initial"))
        var tags = repository.getUserTags(userId)
        assertEquals(1, tags.size)

        // Read
        val createdTag = tags.first()
        assertEquals("Initial", createdTag.title)

        // Update
        val updatedTag = createdTag.copy(title = "Updated")
        repository.updateTag(updatedTag)
        tags = repository.getUserTags(userId)
        assertEquals("Updated", tags.first().title)

        // Delete
        repository.deleteTag(tags.first())
        tags = repository.getUserTags(userId)
        assertEquals(emptyList(), tags)
    }
}