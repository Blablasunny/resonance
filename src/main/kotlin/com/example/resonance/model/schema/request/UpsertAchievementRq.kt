package com.example.resonance.model.schema.request

import com.example.resonance.database.entity.AchievementType
import jakarta.validation.constraints.NotBlank
import java.time.LocalDate

data class UpsertAchievementRq(
    val achievementType: AchievementType,

    @field:NotBlank(message = "Не указано название достижения")
    val title: String,

    val description: String?,

    @field:NotBlank(message = "Не указаны результаты")
    val results: String,

    val confirmation: Boolean = false,

    val date: LocalDate,
)
