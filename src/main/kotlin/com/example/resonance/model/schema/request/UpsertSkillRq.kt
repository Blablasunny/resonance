package com.example.resonance.model.schema.request

import jakarta.validation.constraints.NotBlank

data class UpsertSkillRq(
    @field:NotBlank(message = "Name is required")
    val skillName: String,
    @field:NotBlank(message = "Category is required")
    val skillCategory: String,
)
