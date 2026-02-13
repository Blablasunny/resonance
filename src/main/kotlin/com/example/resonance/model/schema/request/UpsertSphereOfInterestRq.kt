package com.example.resonance.model.schema.request

import jakarta.validation.constraints.NotBlank

data class UpsertSphereOfInterestRq(
    @field:NotBlank(message = "Name is required")
    val sphereName: String,
)
