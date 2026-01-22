package com.example.resonance.security

import com.example.resonance.database.entity.UserEntity
import com.example.resonance.database.entity.UserType
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtUtil {

    @Value("\${jwt.secret}")
    private lateinit var secretKeyString: String

    private val secretKey: SecretKey by lazy {
        Keys.hmacShaKeyFor(secretKeyString.toByteArray())
    }

    @Value("\${jwt.expiration}")
    private var expiration: Long = 86400000

    fun generateToken(user: UserEntity): String {
        return Jwts.builder()
            .claim("userId", user.userId.toString())
            .claim("userType", user.userType.name)
            .setSubject(user.email)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + expiration))
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()
    }

    fun extractUsername(token: String): String {
        return extractClaim(token, Claims::getSubject)
    }

//    fun extractUserId(token: String): UUID {
//        val userId = extractClaim(token) { it["userId"] as String }
//        return UUID.fromString(userId)
//    }
//
//    fun extractUserType(token: String): UserType {
//        val userType = extractClaim(token) { it["userType"] as String }
//        return UserType.valueOf(userType)
//    }

    fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T {
        val claims = extractAllClaims(token)
        return claimsResolver(claims)
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body
    }

    fun validateToken(token: String): Boolean {
        return try {
            extractAllClaims(token)
            true
        } catch (e: Exception) {
            false
        }
    }
}