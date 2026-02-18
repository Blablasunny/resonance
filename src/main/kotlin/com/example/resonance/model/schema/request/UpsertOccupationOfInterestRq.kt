package com.example.resonance.model.schema.request

import jakarta.validation.constraints.NotBlank

data class UpsertOccupationOfInterestRq(
    @field:NotBlank(message = "Не указан профессиональный интерес")
    val occupationName: String,
)
