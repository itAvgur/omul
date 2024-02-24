package com.itavgur.omul.auth.dao

import com.itavgur.omul.auth.config.DatabaseMockConfig
import com.itavgur.omul.auth.domain.User
import com.itavgur.omul.auth.exception.DatabaseConstraintException
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.stereotype.Repository
import java.util.concurrent.atomic.AtomicInteger

@Repository
@ConditionalOnBean(DatabaseMockConfig::class)
class AuthenticationDaoMock : AuthenticationDao {

    companion object {
        const val INITIAL_SQL_TABLE_SEQUENCE_VALUE = 0
    }

    private val sequenceCounter: AtomicInteger = AtomicInteger(INITIAL_SQL_TABLE_SEQUENCE_VALUE)

    private val sqlTable: MutableSet<User> = mutableSetOf()

    override fun createUser(user: User): User {
        checkConstraints(user)

        val newUser = user.clone()
        newUser.userId = sequenceCounter.getAndIncrement()
        sqlTable.add(newUser)
        return newUser
    }

    override fun getUserById(userId: Int): User? = sqlTable.firstOrNull { it.userId == userId }

    override fun getUserByLogin(login: String): User? = sqlTable.firstOrNull { it.login == login }

    override fun checkAccessBasedOnId(employeeId: Int) {
        TODO("Not yet implemented")
    }

    @Throws(DatabaseConstraintException::class)
    private fun checkConstraints(user: User) {
        sqlTable.firstOrNull { e -> user.login == e.login }
            ?.let {
                throw DatabaseConstraintException(
                    "user with login ${user.login} already exists"
                )
            }
    }

}