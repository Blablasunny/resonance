package com.example.resonance.model.dto.rq

import com.example.resonance.database.entity.Gender
import com.example.resonance.database.entity.ProfessionGrade
import java.time.LocalDate

data class UpsertStudentRq(
    val firstName: String,
    val lastName: String,
    val middleName: String?,
    val gender: Gender,
    val birthDate: LocalDate,
    val citizenship: String,
    val professionGrade: ProfessionGrade,
)
