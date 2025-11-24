package com.memksim.api.db

interface Database {
    fun connect(
        url: String,
        user: String,
        password: String,
        driver: String = DEFAULT_DRIVER,
    )
    fun createTables()

    companion object {
        const val DEFAULT_DRIVER = "org.postgresql.Driver"
    }
}