package com.example.resonance.model.schema.request

import jakarta.validation.constraints.NotBlank
import java.time.LocalDate

data class UpsertExperienceRq(
    @field:NotBlank(message = "Name is required")
    val companyName: String,
    @field:NotBlank(message = "Position is required")
    val position: String,
    val startDate: LocalDate,
    val endDate: LocalDate?
)
