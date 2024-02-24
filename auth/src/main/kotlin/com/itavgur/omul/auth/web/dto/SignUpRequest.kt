package com.itavgur.omul.auth.web.dto

import com.itavgur.omul.auth.domain.Subsystem
import com.itavgur.omul.auth.domain.User
import org.springframework.security.crypto.password.PasswordEncoder

data class SignUpRequest(

    val login: String,
    val linkedId: Int,
    val role: Subsystem,
    val password: String
) {
    fun toUser(passwordEncoder: PasswordEncoder): User {

        return User(
            login = login,
            linkedId = linkedId,
            subsystem = role,
            credential = passwordEncoder.encode(password)
        )
    }

}