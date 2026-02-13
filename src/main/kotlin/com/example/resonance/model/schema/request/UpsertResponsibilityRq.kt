package com.example.resonance.model.schema.request

import jakarta.validation.constraints.NotBlank

data class UpsertResponsibilityRq(
    @field:NotBlank(message = "Name is required")
    val responsibilityName: String,
    @field:NotBlank(message = "Description is required")
    val description: String,
)
