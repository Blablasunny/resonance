package com.example.resonance.model.mapper

import com.example.resonance.database.entity.Achievement
import com.example.resonance.model.schema.dto.AchievementDto
import com.example.resonance.model.schema.request.UpsertAchievementRq
import java.time.LocalDateTime

fun Achievement.toDto() = AchievementDto(
    id = id,
    achievementType = achievementType,
    title = title,
    description = description,
    results = results,
    confirmation = confirmation,
    date = date,
    createdAt = createdAt,
    updatedAt = updatedAt,
    students = students.map { it.toDto() },
)

fun UpsertAchievementRq.toEntity() = Achievement(
    achievementType = achievementType,
    title = title,
    description = description,
    results = results,
    confirmation = confirmation,
    date = date,
)

fun Achievement.update(rq: UpsertAchievementRq) : Achievement =
    this.apply {
        achievementType = rq.achievementType
        title = rq.title
        description = rq.description
        results = rq.results
        confirmation = rq.confirmation
        date = rq.date
        updatedAt = LocalDateTime.now()
    }