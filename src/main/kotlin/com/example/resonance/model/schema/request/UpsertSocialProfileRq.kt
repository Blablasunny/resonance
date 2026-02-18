package com.example.resonance.model.schema.request

import jakarta.validation.constraints.NotBlank

data class UpsertSocialProfileRq(
    @field:NotBlank(message = "Не указано название платформы")
    val platformName: String,

    @field:NotBlank(message = "Не указана ссылка на профиль")
    val platformLink: String,
)
