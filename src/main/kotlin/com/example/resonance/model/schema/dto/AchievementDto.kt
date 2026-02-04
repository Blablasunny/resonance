package com.example.resonance.model.schema.dto

import com.example.resonance.database.entity.AchievementType
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class AchievementDto(
    val id: UUID?,
    val achievementType: AchievementType,
    val title: String,
    val description: String?,
    val results: String,
    val confirmation: Boolean = false,
    val date: LocalDate,
    val createdAt : LocalDateTime,
    val updatedAt : LocalDateTime,
    val students: List<StudentDto>
)
