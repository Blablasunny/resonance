package com.example.resonance.model.schema.request

import com.example.resonance.database.entity.AchievementType
import java.time.LocalDate

data class UpsertAchievementRq(
    val achievementType: AchievementType,
    val title: String,
    val description: String?,
    val results: String,
    val confirmation: Boolean = false,
    val date: LocalDate,
)
