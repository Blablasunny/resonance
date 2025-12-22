package com.example.resonance.model.schema.dto

import com.example.resonance.database.entity.Gender
import com.example.resonance.database.entity.ProfessionGrade
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class StudentDto (
    val id : UUID?,
    val firstName : String,
    val lastName : String,
    val middleName : String?,
    val gender : Gender,
    val birthDate : LocalDate,
    val citizenship : String,
    val professionGrade: ProfessionGrade,
    val createdAt : LocalDateTime,
    val updatedAt : LocalDateTime,
)