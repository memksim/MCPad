package com.memksim.data

import com.memksim.user.User

class UserStorage {
    private val users: MutableList<User> = mutableListOf()

    fun getUser(id: Long): User? {
        return users.find { it.id == id }
    }

    fun addUser(user: User) {
        users.add(user)
    }
}