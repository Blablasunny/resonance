package com.example.resonance.model.mapper

import com.example.resonance.database.entity.Education
import com.example.resonance.model.schema.dto.EducationDto
import com.example.resonance.model.schema.request.UpsertEducationRq

fun Education.toDto() = EducationDto(
    id = id,
    institutionName = institutionName,
    educationLevel = educationLevel,
    speciality = speciality,
    status = status,
    createdAt = createdAt,
    updatedAt = updatedAt,
    students = students.map { it.toDto() }
)

fun UpsertEducationRq.toEntity() = Education(
    institutionName = institutionName,
    educationLevel = educationLevel,
    speciality = speciality,
    status = status
)

fun Education.update(rq: UpsertEducationRq): Education =
    this.apply {
        institutionName = rq.institutionName
        educationLevel = rq.educationLevel
        speciality = rq.speciality
        status = rq.status
    }