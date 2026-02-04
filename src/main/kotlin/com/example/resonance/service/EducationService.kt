package com.example.resonance.service

import com.example.resonance.database.entity.Education
import com.example.resonance.model.schema.dto.EducationDto
import com.example.resonance.model.schema.request.UpsertEducationRq
import java.util.UUID

interface EducationService {
    fun getEducationsByStudentId(studentId: UUID): List<EducationDto>
    fun getEducation(id: UUID): Education
    fun getEducations(): List<EducationDto>
    fun addEducation(studentId: UUID, rq: UpsertEducationRq): EducationDto
    fun changeEducation(id: UUID, rq: UpsertEducationRq, studentId: UUID): EducationDto
    fun deleteEducation(id: UUID, studentId: UUID)
}