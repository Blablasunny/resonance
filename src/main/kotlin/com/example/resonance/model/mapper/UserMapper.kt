package com.example.resonance.model.mapper

import com.example.resonance.database.entity.UserEntity
import com.example.resonance.model.dto.rq.UpsertUserRq
import com.example.resonance.model.dto.rs.UserDto

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
    password = password,
)