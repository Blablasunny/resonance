package com.example.resonance.model.schema.request

import com.example.resonance.database.entity.EducationLevel
import jakarta.validation.constraints.NotBlank

data class UpsertEducationRq(
    @field:NotBlank(message = "Name is required")
    val institutionName : String,
    @field:NotBlank(message = "Education level is required")
    val educationLevel: EducationLevel,
    @field:NotBlank(message = "Speciality is required")
    val speciality: String,
    val isFinished: Boolean = true,
)
