package com.example.resonance.model.mapper

import com.example.resonance.database.entity.SphereOfInterest
import com.example.resonance.model.schema.dto.SphereOfInterestDto
import com.example.resonance.model.schema.request.UpsertSphereOfInterestRq
import java.time.LocalDateTime

fun SphereOfInterest.toDto() = SphereOfInterestDto(
    id = id,
    sphereName = sphereName,
    createdAt = createdAt,
    updatedAt = updatedAt,
    students = students.map { it.toDto() }
)

fun UpsertSphereOfInterestRq.toEntity() = SphereOfInterest(
    sphereName = sphereName
)

fun SphereOfInterest.update(rq: UpsertSphereOfInterestRq): SphereOfInterest =
    this.apply {
        sphereName = rq.sphereName
        updatedAt = LocalDateTime.now()
    }