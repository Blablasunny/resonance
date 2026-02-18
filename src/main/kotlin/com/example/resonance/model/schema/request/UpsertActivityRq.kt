package com.example.resonance.model.schema.request

import com.example.resonance.database.entity.ActivityType
import jakarta.validation.constraints.NotBlank
import java.time.LocalDate

data class UpsertActivityRq(
    @field:NotBlank(message = "Не указано название мероприятия")
    val title: String,

    val activityType: ActivityType,

    @field:NotBlank(message = "Не указан формат мероприятия")
    val format: String,

    @field:NotBlank(message = "Не указано описание")
    val description: String,

    @field:NotBlank(message = "Не указаны сферы")
    val spheres: String,

    val date: LocalDate,
)
