package com.example.resonance.model.schema.dto

import java.time.LocalDateTime
import java.util.UUID

data class SkillDto(
    val id: UUID?,
    val skillName: String,
    val skillCategory: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val students: List<StudentDto>,
    val vacancies: List<VacancyDto>
)
