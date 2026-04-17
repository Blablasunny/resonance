package com.example.resonance.model.schema.dto

import com.example.resonance.database.entity.UserType
import java.util.*

data class AuthResponseDto(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String = "Bearer",
    val expiresIn: Long,
    val userId: UUID,
    val userType: UserType,
    val email: String
)

data class RefreshTokenResponseDto(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String = "Bearer",
    val expiresIn: Long
)

data class LogoutResponseDto(
    val message: String = "Successfully logged out",
    val timestamp: Long = System.currentTimeMillis()
)
