package com.example.resonance.model.schema.request

import com.example.resonance.database.entity.ActivityType
import jakarta.validation.constraints.NotBlank
import java.time.LocalDate

data class UpsertActivityRq(
    @field:NotBlank(message = "Title is required")
    val title: String,
    val activityType: ActivityType,
    @field:NotBlank(message = "Format is required")
    val format: String,
    @field:NotBlank(message = "Description is required")
    val description: String,
    @field:NotBlank(message = "Spheres are required")
    val spheres: String,
    val date: LocalDate,
)
