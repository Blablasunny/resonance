package com.example.resonance.model.schema.request

import com.example.resonance.database.entity.EducationLevel
import jakarta.validation.constraints.NotBlank

data class UpsertEducationRq(
    @field:NotBlank(message = "Не указано название института")
    val institutionName : String,

    @field:NotBlank(message = "Не указан уровень образования")
    val educationLevel: EducationLevel,

    @field:NotBlank(message = "Не указана специальность")
    val speciality: String,

    val isFinished: Boolean = true,
)
