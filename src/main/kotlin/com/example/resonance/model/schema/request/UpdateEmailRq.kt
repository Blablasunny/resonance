package com.example.resonance.model.schema.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class UpdateEmailRq(
    @field:NotBlank(message = "Не указан email")
    @field:Email(message = "Неверный формат email")
    val email: String,
)
