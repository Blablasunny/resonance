package com.example.resonance.model.schema.dto

import com.example.resonance.database.entity.ActivityType
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class ActivityDto(
    val id: UUID?,
    val title: String,
    val activityType: ActivityType,
    val format: String,
    val description: String,
    val spheres: String,
    val date: LocalDate,
    val createdAt : LocalDateTime,
    val updatedAt : LocalDateTime,
    val companies: List<CompanyDto>
)
