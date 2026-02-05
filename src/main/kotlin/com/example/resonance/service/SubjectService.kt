package com.example.resonance.service

import com.example.resonance.database.entity.Subject
import com.example.resonance.model.schema.dto.SubjectDto
import com.example.resonance.model.schema.request.UpsertSubjectRq
import java.util.UUID

interface SubjectService {
    fun getSubjectByStudentId(studentId: UUID): List<SubjectDto>
    fun getSubject(id: UUID): Subject
    fun getSubjects(): List<SubjectDto>
    fun addSubject(studentId: UUID, rq: UpsertSubjectRq): SubjectDto
    fun changeSubject(id: UUID, rq: UpsertSubjectRq, studentId: UUID): SubjectDto
    fun deleteSubject(id: UUID, studentId: UUID)
}