package com.example.resonance.model.schema.request

import jakarta.validation.constraints.NotBlank

data class UpsertOccupationOfInterestRq(
    @field:NotBlank(message = "Name is required")
    val occupationName: String,
)
