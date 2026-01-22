package com.example.resonance.model.schema.request

import com.example.resonance.database.entity.UserType
import java.util.UUID

data class UpsertUserRq(
    val userId: UUID,
    val userType: UserType,
    val isActive: Boolean = true,
    val email: String,
    val password: String? = null
) {
    fun getPasswordOrThrow(): String {
        return password ?: throw IllegalArgumentException("Password is required")
    }
}
