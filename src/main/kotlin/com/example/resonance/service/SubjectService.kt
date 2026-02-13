package com.example.resonance.service

import com.example.resonance.database.entity.Subject
import com.example.resonance.model.schema.dto.SubjectDto
import com.example.resonance.model.schema.request.IdsRq
import com.example.resonance.model.schema.request.UpsertSubjectRq
import java.util.UUID

interface SubjectService {
    fun getSubjectByStudentId(studentId: UUID): List<SubjectDto>
    fun getSubject(id: UUID): Subject
    fun getSubjectById(id: UUID): SubjectDto
    fun getSubjects(): List<SubjectDto>
    fun getSubjectsByIds(rq: IdsRq): List<SubjectDto>
    fun addSubject(studentId: UUID, rq: UpsertSubjectRq): SubjectDto
    fun changeSubject(id: UUID, rq: UpsertSubjectRq, studentId: UUID): SubjectDto
    fun deleteSubject(id: UUID, studentId: UUID)
}