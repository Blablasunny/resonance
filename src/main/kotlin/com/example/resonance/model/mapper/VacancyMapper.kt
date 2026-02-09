package com.example.resonance.model.mapper

import com.example.resonance.database.entity.Vacancy
import com.example.resonance.model.schema.dto.VacancyDto
import com.example.resonance.model.schema.request.UpsertVacancyRq
import java.time.LocalDateTime
import java.time.ZoneId

fun Vacancy.toDto() = VacancyDto(
    id = id,
    title = title,
    description = description,
    requirements = requirements,
    experience = experience,
    isOpen = isOpen,
    createdAt = createdAt,
    updatedAt = updatedAt,
    companies = companies.map { it.toDto() }
)

fun UpsertVacancyRq.toEntity() = Vacancy(
    title = title,
    description = description,
    requirements = requirements,
    experience = experience,
    isOpen = isOpen,
)

fun Vacancy.update(rq: UpsertVacancyRq): Vacancy =
    this.apply {
        title = rq.title
        description = rq.description
        requirements = rq.requirements
        experience = rq.experience
        isOpen = rq.isOpen
        updatedAt = LocalDateTime.now()
    }