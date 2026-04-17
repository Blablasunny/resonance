package com.example.resonance.service.implement

import com.example.resonance.database.dao.RefreshTokenDao
import com.example.resonance.database.dao.TokenBlacklistDao
import com.example.resonance.database.entity.RefreshTokenEntity
import com.example.resonance.database.entity.TokenBlacklistEntity
import com.example.resonance.security.JwtUtil
import com.example.resonance.service.TokenService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
class TokenServiceImpl(
    private val refreshTokenDao: RefreshTokenDao,
    private val tokenBlacklistDao: TokenBlacklistDao,
    private val jwtUtil: JwtUtil
): TokenService {

    @Transactional
    override fun saveRefreshToken(
        token: String,
        userId: UUID,
        expiresAt: LocalDateTime,
        ipAddress: String?,
        userAgent: String?
    ): RefreshTokenEntity {
        val existingTokens = refreshTokenDao.findAllByUserId(userId)
        if (existingTokens.size >= 5) {
            existingTokens.minByOrNull { it.createdAt }?.let {
                refreshTokenDao.delete(it)
            }
        }

        val refreshTokenEntity = RefreshTokenEntity(
            token = token,
            userId = userId,
            expiresAt = expiresAt,
            ipAddress = ipAddress,
            userAgent = userAgent
        )
        return refreshTokenDao.save(refreshTokenEntity)
    }

    override fun validateRefreshToken(token: String): RefreshTokenEntity? {
        if (!jwtUtil.validateRefreshToken(token)) {
            return null
        }

        val refreshToken = refreshTokenDao.findByToken(token) ?: return null

        if (refreshToken.revoked) {
            return null
        }

        if (refreshToken.expiresAt.isBefore(LocalDateTime.now())) {
            return null
        }

        return refreshToken
    }

    @Transactional
    override fun revokeRefreshToken(token: String) {
        refreshTokenDao.findByToken(token)?.let { refreshToken ->
            refreshToken.revoked = true
            refreshToken.revokedAt = LocalDateTime.now()
            refreshTokenDao.save(refreshToken)
        }
    }

    @Transactional
    override fun revokeAllUserRefreshTokens(userId: UUID) {
        refreshTokenDao.revokeAllByUserId(userId)
    }

    @Transactional
    override fun blacklistToken(token: String, userId: UUID, reason: String) {
        val tokenHash = jwtUtil.hashToken(token)
        val expiresAt = jwtUtil.extractExpiration(token)?.let {
            LocalDateTime.ofInstant(it.toInstant(), java.time.ZoneId.systemDefault())
        } ?: LocalDateTime.now().plusHours(1)

        val blacklistEntry = TokenBlacklistEntity(
            tokenHash = tokenHash,
            userId = userId,
            expiresAt = expiresAt,
            reason = reason
        )
        tokenBlacklistDao.save(blacklistEntry)
    }

    override fun isTokenBlacklisted(token: String): Boolean {
        val tokenHash = jwtUtil.hashToken(token)
        return tokenBlacklistDao.existsByTokenHash(tokenHash)
    }

    @Scheduled(cron = "0 0 3 * * ?")
    @Transactional
    override fun cleanupExpiredTokens() {
        val now = LocalDateTime.now()
        refreshTokenDao.deleteExpiredTokens(now)
        tokenBlacklistDao.deleteExpiredTokens(now)
    }

    override fun extractIpAddress(request: HttpServletRequest): String? {
        return request.getHeader("X-Forwarded-For")
            ?: request.getHeader("X-Real-IP")
            ?: request.remoteAddr
    }

    override fun extractUserAgent(request: HttpServletRequest): String? {
        return request.getHeader("User-Agent")?.take(500)
    }
}
