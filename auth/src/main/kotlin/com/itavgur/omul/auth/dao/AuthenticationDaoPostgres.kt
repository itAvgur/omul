package com.itavgur.omul.auth.dao

import com.itavgur.omul.auth.config.DatabasePostgresConfig
import com.itavgur.omul.auth.domain.Subsystem
import com.itavgur.omul.auth.domain.User
import com.itavgur.omul.auth.util.DBUtil.Companion.getBooleanValue
import com.itavgur.omul.auth.util.DBUtil.Companion.getIntValue
import com.itavgur.omul.auth.util.DBUtil.Companion.getStringValue
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.ResultSet


@Primary
@Repository
@ConditionalOnBean(DatabasePostgresConfig::class)
class AuthenticationDaoPostgres(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) : AuthenticationDao {

    companion object {

        const val QUERY_GET_USER_BY_ID =
            """SELECT users.user_id ,users.login, users.linked_id, users.subsystem, users.password, users.enabled
                FROM users WHERE users.user_id = :userId"""

        const val QUERY_GET_USER_BY_LOGIN =
            """SELECT users.user_id ,users.login, users.linked_id, users.subsystem, users.password, users.enabled
                FROM users WHERE users.login = :login"""

        const val QUERY_INSERT_USER =
            """INSERT INTO users (login, linked_id, subsystem, password, enabled)
                VALUES (:login, :linkedId, :subsystem, :password, :enabled)"""
    }

    override fun createUser(user: User): User {
        val map = MapSqlParameterSource(
            mapOf(
                "login" to user.login,
                "linkedId" to user.linkedId,
                "subsystem" to user.subsystem.name,
                "password" to user.password,
                "enabled" to user.enabled
            )
        )
        val generatedKeyHolder = GeneratedKeyHolder()
        namedParameterJdbcTemplate.update(QUERY_INSERT_USER, map, generatedKeyHolder)
        user.userId = generatedKeyHolder.keyList.first()["user_id"] as Int?

        return user
    }

    override fun getUserById(userId: Int): User? {
        val result =
            namedParameterJdbcTemplate.query(
                QUERY_GET_USER_BY_ID,
                MapSqlParameterSource("userId", userId),
                UserRowMapper()
            )
        return result.firstOrNull()
    }

    override fun getUserByLogin(login: String): User? {
        val result =
            namedParameterJdbcTemplate.query(
                QUERY_GET_USER_BY_LOGIN,
                MapSqlParameterSource("login", login),
                UserRowMapper()
            )
        return result.firstOrNull()
    }

    override fun checkAccessBasedOnId(employeeId: Int) {
        TODO("Not yet implemented")
    }

    class UserRowMapper : RowMapper<User> {
        override fun mapRow(rs: ResultSet, rowNum: Int): User {

            return User(
                userId = getIntValue("user_id", rs),
                login = getStringValue("login", rs)!!,
                linkedId = getIntValue("linked_id", rs)!!,
                subsystem = Subsystem.valueOf(getStringValue("subsystem", rs)!!),
                credential = getStringValue("password", rs)!!,
                enabled = getBooleanValue("enabled", rs)!!
            )

        }
    }

}