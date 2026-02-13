package com.example.resonance.model.schema.request

import com.example.resonance.database.entity.UserType
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.util.UUID

data class UpsertUserRq(
    val userId: UUID,
    val userType: UserType,
    val isActive: Boolean = true,
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Invalid email format")
    val email: String,
    @field:NotBlank(message = "Password is required")
    @field:Size(min = 8, message = "Password must be at least 8 characters")
    val password: String? = null
) {
    fun getPasswordOrThrow(): String {
        return password ?: throw IllegalArgumentException("Password is required")
    }
}
