package com.example.resonance.model.mapper

import com.example.resonance.database.entity.OccupationOfInterest
import com.example.resonance.model.schema.dto.OccupationOfInterestDto
import com.example.resonance.model.schema.request.UpsertOccupationOfInterestRq
import java.time.LocalDateTime
import java.time.ZoneId

fun OccupationOfInterest.toDto() = OccupationOfInterestDto(
    id = id,
    occupationName = occupationName,
    createdAt = createdAt,
    updatedAt = updatedAt,
    students = students.map { it.toDto() }
)

fun UpsertOccupationOfInterestRq.toEntity() = OccupationOfInterest(
    occupationName = occupationName,
)

fun OccupationOfInterest.update(rq: UpsertOccupationOfInterestRq): OccupationOfInterest =
    this.apply {
        occupationName = rq.occupationName
        updatedAt = LocalDateTime.now()
    }