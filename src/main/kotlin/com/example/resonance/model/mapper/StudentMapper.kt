package com.example.resonance.model.mapper

import com.example.resonance.database.entity.Student
import com.example.resonance.model.schema.request.UpsertStudentRq
import com.example.resonance.model.schema.dto.StudentDto
import java.time.LocalDateTime
import java.time.ZoneId

fun Student.toDto() = StudentDto(
    id = id,
    firstName = firstName,
    lastName = lastName,
    middleName = middleName,
    gender = gender,
    birthDate = birthDate,
    citizenship = citizenship,
    professionGrade = professionGrade,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun UpsertStudentRq.toEntity() = Student(
    firstName = firstName,
    lastName = lastName,
    middleName = middleName,
    gender = gender,
    birthDate = birthDate,
    citizenship = citizenship,
    professionGrade = professionGrade,
)

fun Student.update(rq: UpsertStudentRq): Student =
    this.apply {
        firstName = rq.firstName
        lastName = rq.lastName
        middleName = rq.middleName
        gender = rq.gender
        birthDate = rq.birthDate
        citizenship = rq.citizenship
        professionGrade = rq.professionGrade
        updatedAt = LocalDateTime.now()
    }