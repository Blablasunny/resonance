package com.example.resonance.model.mapper

import com.example.resonance.database.entity.Responsibility
import com.example.resonance.model.schema.dto.ResponsibilityDto
import com.example.resonance.model.schema.request.UpsertResponsibilityRq
import java.time.LocalDateTime

fun Responsibility.toDto() = ResponsibilityDto(
    id = id,
    responsibilityName = responsibilityName,
    description = description,
    createdAt = createdAt,
    updatedAt = updatedAt,
    vacancies = vacancies.map { it.toDto() }
)

fun UpsertResponsibilityRq.toEntity() = Responsibility(
    responsibilityName = responsibilityName,
    description = description,
)

fun Responsibility.update(rq: UpsertResponsibilityRq): Responsibility =
    this.apply {
        responsibilityName = rq.responsibilityName
        description = rq.description
        updatedAt = LocalDateTime.now()
    }