package com.example.resonance.model.schema.dto

import java.time.LocalDateTime
import java.util.UUID

data class VacancyDto(
    val id: UUID?,
    val title: String,
    val description: String,
    val requirements: String,
    val experience: String,
    val isOpen: Boolean = true,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val companies: List<CompanyDto>,
)
