package com.example.resonance.security

import com.example.resonance.database.entity.UserEntity
import com.example.resonance.database.entity.UserType
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.MessageDigest
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtUtil {

    @Value("\${jwt.secret}")
    private lateinit var secretKeyString: String

    @Value("\${jwt.refresh-secret}")
    private lateinit var refreshSecretKeyString: String

    @Value("\${jwt.expiration}")
    private var accessTokenExpiration: Long = 900000

    @Value("\${jwt.refresh-expiration}")
    private var refreshTokenExpiration: Long = 604800000

    @Value("\${jwt.issuer}")
    private lateinit var issuer: String

    @Value("\${jwt.audience}")
    private lateinit var audience: String

    private val accessSecretKey: SecretKey by lazy {
        Keys.hmacShaKeyFor(secretKeyString.toByteArray())
    }

    private val refreshSecretKey: SecretKey by lazy {
        Keys.hmacShaKeyFor(refreshSecretKeyString.toByteArray())
    }

    fun generateAccessToken(user: UserEntity): String {
        val now = Date()
        val expiryDate = Date(now.time + accessTokenExpiration)

        return Jwts.builder()
            .setSubject(user.email)
            .setIssuer(issuer)
            .setAudience(audience)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .setId(UUID.randomUUID().toString())
            .claim("userId", user.userId.toString())
            .claim("userType", user.userType.name)
            .claim("tokenType", "ACCESS")
            .signWith(accessSecretKey, SignatureAlgorithm.HS512)
            .compact()
    }

    fun generateRefreshToken(user: UserEntity): String {
        val now = Date()
        val expiryDate = Date(now.time + refreshTokenExpiration)

        return Jwts.builder()
            .setSubject(user.userId.toString())
            .setIssuer(issuer)
            .setAudience(audience)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .setId(UUID.randomUUID().toString())
            .claim("tokenType", "REFRESH")
            .signWith(refreshSecretKey, SignatureAlgorithm.HS512)
            .compact()
    }


    fun validateAccessToken(token: String): Boolean {
        return try {
            val claims = extractAccessTokenClaims(token)

            if (claims.issuer != issuer) {
                return false
            }

            if (claims.audience != audience) {
                return false
            }

            val tokenType = claims.get("tokenType", String::class.java)
            if (tokenType != "ACCESS") {
                return false
            }

            if (claims.expiration.before(Date())) {
                return false
            }

            true
        } catch (e: ExpiredJwtException) {
            false
        } catch (e: UnsupportedJwtException) {
            false
        } catch (e: MalformedJwtException) {
            false
        } catch (e: SignatureException) {
            false
        } catch (e: IllegalArgumentException) {
            false
        } catch (e: Exception) {
            false
        }
    }

    fun validateRefreshToken(token: String): Boolean {
        return try {
            val claims = extractRefreshTokenClaims(token)

            if (claims.issuer != issuer || claims.audience != audience) {
                return false
            }

            val tokenType = claims.get("tokenType", String::class.java)
            if (tokenType != "REFRESH") {
                return false
            }

            if (claims.expiration.before(Date())) {
                return false
            }

            true
        } catch (e: Exception) {
            false
        }
    }

    fun extractEmail(token: String): String? {
        return try {
            extractAccessTokenClaims(token).subject
        } catch (e: Exception) {
            null
        }
    }

    fun extractUserId(token: String): UUID? {
        return try {
            val claims = extractAccessTokenClaims(token)
            UUID.fromString(claims.get("userId", String::class.java))
        } catch (e: Exception) {
            null
        }
    }

    fun extractUserIdFromRefreshToken(token: String): UUID? {
        return try {
            val claims = extractRefreshTokenClaims(token)
            UUID.fromString(claims.subject)
        } catch (e: Exception) {
            null
        }
    }

    fun extractExpiration(token: String): Date? {
        return try {
            extractAccessTokenClaims(token).expiration
        } catch (e: Exception) {
            null
        }
    }

    fun extractJti(token: String): String? {
        return try {
            extractAccessTokenClaims(token).id
        } catch (e: Exception) {
            null
        }
    }

    fun hashToken(token: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(token.toByteArray())
        return hash.joinToString("") { "%02x".format(it) }
    }

    private fun extractAccessTokenClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(accessSecretKey)
            .requireIssuer(issuer)
            .requireAudience(audience)
            .build()
            .parseClaimsJws(token)
            .body
    }

    private fun extractRefreshTokenClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(refreshSecretKey)
            .requireIssuer(issuer)
            .requireAudience(audience)
            .build()
            .parseClaimsJws(token)
            .body
    }

    fun getAccessTokenExpirationMillis(): Long = accessTokenExpiration
    fun getRefreshTokenExpirationMillis(): Long = refreshTokenExpiration
}