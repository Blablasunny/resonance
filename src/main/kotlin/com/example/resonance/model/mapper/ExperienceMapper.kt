package com.example.resonance.model.mapper

import com.example.resonance.database.entity.Experience
import com.example.resonance.model.schema.dto.ExperienceDto
import com.example.resonance.model.schema.request.UpsertExperienceRq
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

fun Experience.toDto() = ExperienceDto(
    id = id,
    companyName = companyName,
    position = position,
    startDate = startDate,
    endDate = endDate,
    createdAt = createdAt,
    updatedAt = updatedAt,
    students = students.map { it.toDto() }
)

fun UpsertExperienceRq.toEntity() = Experience(
    companyName = companyName,
    position = position,
    startDate = startDate,
    endDate = endDate,
)

fun Experience.update(rq: UpsertExperienceRq) : Experience =
    this.apply {
        companyName = rq.companyName
        position = rq.position
        startDate = rq.startDate
        endDate = rq.endDate
        updatedAt = LocalDateTime.now()
    }