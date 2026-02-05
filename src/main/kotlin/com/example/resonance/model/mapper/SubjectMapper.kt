package com.example.resonance.model.mapper

import com.example.resonance.database.entity.Subject
import com.example.resonance.model.schema.dto.SubjectDto
import com.example.resonance.model.schema.request.UpsertSubjectRq
import java.time.LocalDateTime
import java.time.ZoneId

fun Subject.toDto() = SubjectDto(
    id = id,
    subjectName = subjectName,
    createdAt = createdAt,
    updatedAt = updatedAt,
    students = students.map { it.toDto() },
)

fun UpsertSubjectRq.toEntity() = Subject(
    subjectName = subjectName,
)

fun Subject.update(rq: UpsertSubjectRq): Subject =
    this.apply {
        subjectName = rq.subjectName
        updatedAt = LocalDateTime.now()
    }