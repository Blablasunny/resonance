package com.example.resonance.model.schema.dto

import com.example.resonance.database.entity.UserType
import java.util.UUID

data class RegisterDto(
    val id: UUID,
    val email: String,
    val userType: UserType,
    val token: String,
    val message: String,
    val profileId: UUID,
    val profileData: Any? = null
)