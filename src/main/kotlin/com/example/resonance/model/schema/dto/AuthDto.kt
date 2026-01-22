package com.example.resonance.model.schema.dto

import com.example.resonance.database.entity.UserType
import java.util.UUID

data class AuthDto (
    val token: String,
    val userId: UUID,
    val userType: UserType,
    val email: String
)