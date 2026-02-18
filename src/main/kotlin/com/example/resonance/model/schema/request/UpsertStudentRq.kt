package com.example.resonance.model.schema.request

import com.example.resonance.database.entity.Gender
import com.example.resonance.database.entity.ProfessionGrade
import jakarta.validation.constraints.NotBlank
import java.time.LocalDate

data class UpsertStudentRq(
    @field:NotBlank(message = "Не указано имя")
    val firstName: String,

    @field:NotBlank(message = "Не указана фамилия")
    val lastName: String,

    val middleName: String?,

    val gender: Gender,

    val birthDate: LocalDate,

    @field:NotBlank(message = "Не указано гражданство")
    val citizenship: String,

    val professionGrade: ProfessionGrade,
)
