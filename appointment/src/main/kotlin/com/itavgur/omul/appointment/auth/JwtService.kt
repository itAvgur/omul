package com.itavgur.omul.appointment.auth

import com.itavgur.omul.appointment.exception.InvalidRequestException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.util.*
import java.util.function.Function
import javax.crypto.SecretKey

@Service
class JwtService {

    companion object {
        val SECURITY_SUBSYSTEM = Subsystem.CUSTOMER
    }

    @Value("\${security.signing.key:''}")
    private val jwtSigningKey: String? = null

    @Value("\${security.enabled:true}")
    private val securityEnabled: Boolean = true

    fun validateIdWithJwt(id: Int) {

        if (securityEnabled) {

            val user = SecurityContextHolder.getContext().authentication.principal as User
            if (id != user.linkedId) {
                throw ForbiddenException("access to target data is forbidden", httpCode = HttpStatus.FORBIDDEN)
            }
        }

    }

    fun getCurrentJwt(): String? {
        return if (securityEnabled) {
            (SecurityContextHolder.getContext().authentication.principal as User).jwt
        } else {
            null
        }
    }

    fun extractUserName(token: String): String {
        return extractClaim(token) { obj: Claims -> obj.subject }
    }

    fun extractSubsystem(token: String): Subsystem {
        return Subsystem.valueOf(extractClaim(token) { obj: Claims -> obj["subsystem"] as String })
    }

    fun extractLinkedId(token: String): Int {
        return extractClaim(token) { obj: Claims -> obj["linkedId"] as Int }
    }

    private fun <T> extractClaim(token: String, claimsResolvers: Function<Claims, T>): T {
        val claims: Claims = extractAllClaims(token)
        return claimsResolvers.apply(claims)
    }

    private fun extractAllClaims(token: String): Claims {
        try {
            return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).payload
        } catch (ex: JwtException) {
            throw InvalidRequestException("jwt invalid")
        }
    }

    fun getSigningKey(): SecretKey {
        val keyBytes = Decoders.BASE64.decode(jwtSigningKey)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    fun isTokenValid(token: String?, user: User): Boolean {
        val userName = extractUserName(token!!)
        return userName == user.username && !isTokenExpired(token)
    }

    private fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }

    private fun extractExpiration(token: String): Date {
        return extractClaim(token) { obj: Claims -> obj.expiration }
    }

}