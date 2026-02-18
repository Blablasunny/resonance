package com.example.resonance.model.schema.request

import jakarta.validation.constraints.NotBlank

data class UpsertVacancyRq(
    @field:NotBlank(message = "Не указано название вакансии")
    val title: String,

    @field:NotBlank(message = "Не указано описание")
    val description: String,

    @field:NotBlank(message = "Не указаны требования")
    val requirements: String,

    @field:NotBlank(message = "Не указан требуемый опыт")
    val experience: String,

    val isOpen: Boolean = true,
)
