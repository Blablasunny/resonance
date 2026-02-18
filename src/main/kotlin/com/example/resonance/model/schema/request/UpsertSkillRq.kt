package com.example.resonance.model.schema.request

import jakarta.validation.constraints.NotBlank

data class UpsertSkillRq(
    @field:NotBlank(message = "Не указан навык")
    val skillName: String,

    @field:NotBlank(message = "Не указана категория навыка")
    val skillCategory: String,
)
