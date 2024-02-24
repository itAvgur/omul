package com.itavgur.omul.auth.dao

import com.itavgur.omul.auth.domain.User

interface AuthenticationDao {

    fun createUser(user: User): User

    fun getUserById(userId: Int): User?

    fun getUserByLogin(login: String): User?

    fun checkAccessBasedOnId(employeeId: Int)

}