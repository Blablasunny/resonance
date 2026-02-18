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

    @field:NotBlank(message = "Не указан email")
    @field:Email(message = "Неверный формат email")
    val email: String,

    @field:NotBlank(message = "Не указан пароль")
    @field:Size(min = 8, message = "Пароль должен содержать не менее 8 символов")
    val password: String
)
