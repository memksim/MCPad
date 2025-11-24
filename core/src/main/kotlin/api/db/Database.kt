package com.memksim.api.db

interface Database {
    fun connect(
        url: String,
        user: String,
        password: String,
        driver: String? = null,
    )
    fun createTables()
}