package com.example.resonance.database.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(
    name = "token_blacklist",
    indexes = [
        Index(name = "idx_token_hash", columnList = "tokenHash"),
        Index(name = "idx_expires_at", columnList = "expiresAt")
    ]
)
data class TokenBlacklistEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(nullable = false, unique = true, length = 64)
    val tokenHash: String,

    @Column(nullable = false)
    val userId: UUID,

    @Column(nullable = false)
    val expiresAt: LocalDateTime,

    @Column(nullable = false)
    val blacklistedAt: LocalDateTime = LocalDateTime.now(),

    @Column(length = 50)
    val reason: String? = "USER_LOGOUT"
)
