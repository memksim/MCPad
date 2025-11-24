package com.memksim.internal.data

import com.memksim.api.db.Database
import com.memksim.internal.data.users.UsersTable
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.Database as ExposedDb

internal class DatabaseImpl() : Database {
    override fun connect(url: String, user: String, password: String, driver: String) {
        ExposedDb.connect(
            url = url,
            user = user,
            password = password,
            driver = driver
        )
    }

    override fun createTables() {
        transaction {
            SchemaUtils.create(UsersTable)
        }
    }

}
