package com.example.resonance.model.schema.dto

import java.time.LocalDateTime
import java.util.UUID

data class SocialProfileDto(
    val id: UUID?,
    val platformName: String,
    val platformLink: String,
    val description: String?,
    val createdAt : LocalDateTime,
    val updatedAt : LocalDateTime,
    val students: List<StudentDto>,
    val companies: List<CompanyDto>
)
