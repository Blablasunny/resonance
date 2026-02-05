package com.example.resonance.model.schema.dto

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class ExperienceDto(
    val id: UUID?,
    val companyName: String,
    val position: String,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val students: List<StudentDto>
)
