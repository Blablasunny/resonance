package com.example.resonance.model.schema.dto

import com.example.resonance.database.entity.EducationLevel
import java.time.LocalDateTime
import java.util.UUID

data class EducationDto(
    val id : UUID?,
    val institutionName : String,
    val educationLevel: EducationLevel,
    val speciality: String,
    val isFinished: Boolean = true,
    val createdAt : LocalDateTime,
    val updatedAt : LocalDateTime,
    val students: List<StudentDto>
)
