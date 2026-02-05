package com.example.resonance.model.schema.dto

import java.time.LocalDateTime
import java.util.UUID

data class SubjectDto(
    val id: UUID?,
    val subjectName: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val students: List<StudentDto>
)
