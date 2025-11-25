import com.memksim.api.users.User
import com.memksim.internal.data.users.UsersRepositoryImpl
import com.memksim.internal.data.users.UsersTable
import kotlinx.coroutines.test.runTest
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class UsersRepositoryTest {

    private val repository = UsersRepositoryImpl()

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
                SchemaUtils.create(UsersTable)
            }
        }
    }

    @BeforeEach
    fun cleanTables() {
        transaction {
            SchemaUtils.drop(UsersTable)
            SchemaUtils.create(UsersTable)
        }
    }

    @Test
    fun `createUser and getUserByTelegramId`() = runTest {
        val tgId = 123456L

        repository.saveUser(
            User(
                telegramId = tgId,
                firstName = "Max",
                lastName = "Test"
            )
        )
        val user = repository.getUserByTelegramId(tgId)

        assertNotNull(user)
        assertEquals(tgId, user.telegramId)
        assertEquals("Max", user.firstName)
        assertEquals("Test", user.lastName)
    }

    @Test
    fun `getUserByTelegramId returns null when user not found`() = runTest {
        val nonExistentTgId = 999999L
        val user = repository.getUserByTelegramId(nonExistentTgId)

        assertNull(user)
    }

    @Test
    fun `saveUser with null lastName`() = runTest {
        val tgId = 123457L

        repository.saveUser(
            User(
                telegramId = tgId,
                firstName = "Alex",
                lastName = null
            )
        )
        val user = repository.getUserByTelegramId(tgId)

        assertNotNull(user)
        assertEquals(tgId, user.telegramId)
        assertEquals("Alex", user.firstName)
        assertNull(user.lastName)
    }

    @Test
    fun `deleteUserByTelegramId`() = runTest {
        val tgId = 123458L

        // First create a user
        repository.saveUser(
            User(
                telegramId = tgId,
                firstName = "Sarah",
                lastName = "Delete"
            )
        )

        // Verify user exists
        var user = repository.getUserByTelegramId(tgId)
        assertNotNull(user)

        // Delete the user
        repository.deleteUserByTelegramId(tgId)

        // Verify user is deleted
        user = repository.getUserByTelegramId(tgId)
        assertNull(user)
    }

    @Test
    fun `deleteUserByTelegramId when user not found should not throw`() = runTest {
        val nonExistentTgId = 888888L

        // Should not throw exception even if user doesn't exist
        repository.deleteUserByTelegramId(nonExistentTgId)
    }
}
