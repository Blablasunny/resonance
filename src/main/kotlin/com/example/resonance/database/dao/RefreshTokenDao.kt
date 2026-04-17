package com.example.resonance.database.dao

import com.example.resonance.database.entity.RefreshTokenEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

@Repository
interface RefreshTokenDao : JpaRepository<RefreshTokenEntity, UUID> {
    
    fun findByToken(token: String): RefreshTokenEntity?
    
    fun findAllByUserId(userId: UUID): List<RefreshTokenEntity>
    
    @Modifying
    @Query("UPDATE RefreshTokenEntity r SET r.revoked = true, r.revokedAt = :revokedAt WHERE r.userId = :userId AND r.revoked = false")
    fun revokeAllByUserId(userId: UUID, revokedAt: LocalDateTime = LocalDateTime.now())
    
    @Modifying
    @Query("DELETE FROM RefreshTokenEntity r WHERE r.expiresAt < :now")
    fun deleteExpiredTokens(now: LocalDateTime = LocalDateTime.now())
    
    @Modifying
    @Query("DELETE FROM RefreshTokenEntity r WHERE r.userId = :userId")
    fun deleteAllByUserId(userId: UUID)
}
