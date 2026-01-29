package com.example.resonance.model.mapper

import com.example.resonance.database.entity.Education
import com.example.resonance.model.schema.dto.EducationDto
import com.example.resonance.model.schema.request.UpsertEducationRq
import java.time.LocalDateTime

fun Education.toDto() = EducationDto(
    id = id,
    institutionName = institutionName,
    educationLevel = educationLevel,
    speciality = speciality,
    isFinished = isFinished,
    createdAt = createdAt,
    updatedAt = updatedAt,
    students = students.map { it.toDto() }
)

fun UpsertEducationRq.toEntity() = Education(
    institutionName = institutionName,
    educationLevel = educationLevel,
    speciality = speciality,
    isFinished = isFinished
)

fun Education.update(rq: UpsertEducationRq): Education =
    this.apply {
        institutionName = rq.institutionName
        educationLevel = rq.educationLevel
        speciality = rq.speciality
        isFinished = rq.isFinished
        updatedAt = LocalDateTime.now()
    }