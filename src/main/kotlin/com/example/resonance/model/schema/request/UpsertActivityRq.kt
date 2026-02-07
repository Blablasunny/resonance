package com.example.resonance.model.schema.request

import com.example.resonance.database.entity.ActivityType
import java.time.LocalDate

data class UpsertActivityRq(
    val title: String,
    val activityType: ActivityType,
    val format: String,
    val description: String,
    val spheres: String,
    val date: LocalDate,
)
