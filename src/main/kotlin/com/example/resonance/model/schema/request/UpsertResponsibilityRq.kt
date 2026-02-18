package com.example.resonance.model.schema.request

import jakarta.validation.constraints.NotBlank

data class UpsertResponsibilityRq(
    @field:NotBlank(message = "Не указана обязанность")
    val responsibilityName: String,

    @field:NotBlank(message = "Не указано описание")
    val description: String,
)
