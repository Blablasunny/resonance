package com.example.resonance.database.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "refresh_token")
data class RefreshTokenEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(nullable = false, unique = true, length = 1000)
    val token: String,

    @Column(nullable = false)
    val userId: UUID,

    @Column(nullable = false)
    val expiresAt: LocalDateTime,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var revoked: Boolean = false,

    @Column
    var revokedAt: LocalDateTime? = null,

    @Column(length = 50)
    val ipAddress: String? = null,

    @Column(length = 500)
    val userAgent: String? = null
)
