package com.example.resonance.service

import com.example.resonance.database.entity.RefreshTokenEntity
import jakarta.servlet.http.HttpServletRequest
import java.time.LocalDateTime
import java.util.UUID

interface TokenService {
    fun saveRefreshToken(
        token: String,
        userId: UUID,
        expiresAt: LocalDateTime,
        ipAddress: String? = null,
        userAgent: String? = null
    ): RefreshTokenEntity
    fun validateRefreshToken(token: String): RefreshTokenEntity?
    fun revokeRefreshToken(token: String)
    fun revokeAllUserRefreshTokens(userId: UUID)
    fun blacklistToken(token: String, userId: UUID, reason: String = "USER_LOGOUT")
    fun isTokenBlacklisted(token: String): Boolean
    fun cleanupExpiredTokens()
    fun extractIpAddress(request: HttpServletRequest): String?
    fun extractUserAgent(request: HttpServletRequest): String?
}