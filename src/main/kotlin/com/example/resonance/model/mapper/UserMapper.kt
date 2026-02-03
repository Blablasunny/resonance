package com.example.resonance.model.mapper

import com.example.resonance.database.entity.UserEntity
import com.example.resonance.model.schema.request.UpsertUserRq
import com.example.resonance.model.schema.dto.UserDto
import java.time.LocalDateTime

fun UserEntity.toDto() = UserDto(
    id = id,
    userId = userId,
    userType = userType,
    isActive = isActive,
    email = email,
    password = password,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun UpsertUserRq.toEntity() = UserEntity(
    userId = userId,
    userType = userType,
    isActive = isActive,
    email = email,
    password = getPasswordOrThrow(),
)

fun UserEntity.updateEmail(newEmail: String): UserEntity =
    this.apply {
        email = newEmail
        updatedAt = LocalDateTime.now()
    }

fun UserEntity.updatePassword(newPassword: String): UserEntity =
    this.apply {
        password = newPassword
        updatedAt = LocalDateTime.now()
    }