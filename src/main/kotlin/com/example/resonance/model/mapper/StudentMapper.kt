package com.example.resonance.model.mapper

import com.example.resonance.database.entity.Student
import com.example.resonance.model.schema.request.UpsertStudentRq
import com.example.resonance.model.schema.dto.StudentDto

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