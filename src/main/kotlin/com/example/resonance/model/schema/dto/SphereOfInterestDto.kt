package com.example.resonance.model.schema.dto

import java.time.LocalDateTime
import java.util.UUID

data class SphereOfInterestDto(
    val id: UUID?,
    val sphereName: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val students: List<StudentDto>
)
