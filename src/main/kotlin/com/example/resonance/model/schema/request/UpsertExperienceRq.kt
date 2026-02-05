package com.example.resonance.model.schema.request

import java.time.LocalDate

data class UpsertExperienceRq(
    val companyName: String,
    val position: String,
    val startDate: LocalDate,
    val endDate: LocalDate?
)
