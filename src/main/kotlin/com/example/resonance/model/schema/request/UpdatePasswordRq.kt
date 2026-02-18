package com.example.resonance.model.schema.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UpdatePasswordRq(
    @field:NotBlank(message = "Не указан старый пароль")
    val oldPassword: String,

    @field:NotBlank(message = "Не указан пароль")
    @field:Size(min = 8, message = "Пароль должен содержать не менее 8 символов")
    val password: String,

    @field:NotBlank(message = "Не указан подтверждение пароля")
    val confirmPassword: String,
)
