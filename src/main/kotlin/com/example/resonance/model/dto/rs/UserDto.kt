package com.example.resonance.model.dto.rs

import com.example.resonance.database.entity.UserType
import java.time.LocalDateTime
import java.util.UUID

data class UserDto (
    val id: UUID?,
    val userId: UUID,
    val userType: UserType,
    val isActive: Boolean = true,
    val email: String,
    val password: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)