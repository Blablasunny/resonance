package com.example.resonance.model.schema.request

data class UpsertVacancyRq(
    val title: String,
    val description: String,
    val requirements: String,
    val experience: String,
    val isOpen: Boolean = true,
)
