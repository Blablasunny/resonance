package com.example.resonance.model.schema.request

import jakarta.validation.constraints.NotBlank

data class UpsertVacancyRq(
    @field:NotBlank(message = "Title is required")
    val title: String,
    @field:NotBlank(message = "Description is required")
    val description: String,
    @field:NotBlank(message = "Requirements are required")
    val requirements: String,
    @field:NotBlank(message = "Experience is required")
    val experience: String,
    val isOpen: Boolean = true,
)
