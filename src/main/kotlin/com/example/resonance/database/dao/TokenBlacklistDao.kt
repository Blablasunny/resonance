package com.example.resonance.database.dao

import com.example.resonance.database.entity.TokenBlacklistEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

@Repository
interface TokenBlacklistDao : JpaRepository<TokenBlacklistEntity, UUID> {
    
    fun existsByTokenHash(tokenHash: String): Boolean
    
    @Modifying
    @Query("DELETE FROM TokenBlacklistEntity t WHERE t.expiresAt < :now")
    fun deleteExpiredTokens(now: LocalDateTime = LocalDateTime.now())
}
