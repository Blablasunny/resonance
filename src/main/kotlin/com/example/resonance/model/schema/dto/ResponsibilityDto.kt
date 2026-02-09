package com.example.resonance.model.schema.dto

import java.time.LocalDateTime
import java.util.UUID

data class ResponsibilityDto(
    val id: UUID?,
    val responsibilityName: String,
    val description: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val vacancies: List<VacancyDto>
)
