package com.itavgur.omul.auth.service

import com.itavgur.omul.auth.domain.User
import com.itavgur.omul.auth.exception.InvalidRequestException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*
import java.util.function.Function
import javax.crypto.SecretKey

@Service
class JwtService {

    @Value("\${token.signing.key}")
    private val jwtSigningKey: String? = null

    @Cacheable("jwt", key = "{#newUser.login}")
    fun generateJwt(newUser: User): String {

        val extraClaims = mutableMapOf<String, Objects>()

        return Jwts.builder().claims(extraClaims)
            .subject(newUser.username)
            .issuedAt(Date(System.currentTimeMillis()))
            .claims().add("linkedId", newUser.linkedId)
            .and().claims().add("subsystem", newUser.subsystem)
            .and().expiration(Date(System.currentTimeMillis() + 1000 * 60 * 24))
            .signWith(getSigningKey()).compact()
    }

    @CacheEvict("jwt", key = "{#user.login}")
    fun evictTokenForUser(user: User) {
        //nothing here
    }

    fun extractUserName(token: String): String {
        return extractClaim(token) { obj: Claims -> obj.subject }
    }

    fun extractSubsystem(token: String): String {
        return extractClaim(token) { obj: Claims -> obj["subsystem"] as String }
    }

    fun extractLinkedId(token: String): String {
        return extractClaim(token) { obj: Claims -> obj["linkedId"] as String }
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

    fun isTokenValid(token: String?, userDetails: UserDetails): Boolean {
        val userName = extractUserName(token!!)
        return userName == userDetails.username && !isTokenExpired(token)
    }

    private fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }

    private fun extractExpiration(token: String): Date {
        return extractClaim(token) { obj: Claims -> obj.expiration }
    }

}