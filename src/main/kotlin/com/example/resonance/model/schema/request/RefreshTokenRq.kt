package com.example.resonance.model.schema.request

import jakarta.validation.constraints.NotBlank

data class RefreshTokenRq(
    @field:NotBlank(message = "Не указан refresh token")
    val refreshToken: String
)
