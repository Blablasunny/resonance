package com.example.resonance.model.schema.request

import com.example.resonance.database.entity.Gender
import com.example.resonance.database.entity.ProfessionGrade
import jakarta.validation.constraints.NotBlank
import java.time.LocalDate

data class UpsertStudentRq(
    @field:NotBlank(message = "First name is required")
    val firstName: String,
    @field:NotBlank(message = "Last name is required")
    val lastName: String,
    val middleName: String?,
    val gender: Gender,
    val birthDate: LocalDate,
    @field:NotBlank(message = "Citizenship is required")
    val citizenship: String,
    val professionGrade: ProfessionGrade,
)
