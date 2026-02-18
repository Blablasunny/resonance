package com.example.resonance.model.schema.request

import jakarta.validation.constraints.NotBlank
import java.time.LocalDate

data class UpsertExperienceRq(
    @field:NotBlank(message = "Не указано название компании")
    val companyName: String,

    @field:NotBlank(message = "Не указана позиция")
    val position: String,

    val startDate: LocalDate,

    val endDate: LocalDate?
)
