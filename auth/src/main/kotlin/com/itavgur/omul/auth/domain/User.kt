package com.itavgur.omul.auth.domain

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class User(
    var userId: Int? = null,
    val login: String,
    val linkedId: Int,
    val subsystem: Subsystem,
    val credential: String? = null,
    val enabled: Boolean? = true
) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {

        return mutableListOf(SimpleGrantedAuthority(subsystem.name))
    }

    override fun getPassword(): String? {
        return credential
    }

    override fun getUsername(): String {
        return login
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return enabled ?: return false
    }

    fun clone(): User {
        return User(
            userId = userId,
            login = login,
            linkedId = linkedId,
            credential = credential,
            subsystem = subsystem,
            enabled = enabled
        )
    }
}
