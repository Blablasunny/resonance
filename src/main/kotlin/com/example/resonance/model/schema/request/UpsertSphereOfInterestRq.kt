package com.example.resonance.model.schema.request

import jakarta.validation.constraints.NotBlank

data class UpsertSphereOfInterestRq(
    @field:NotBlank(message = "Не указана сфера интересов")
    val sphereName: String,
)
