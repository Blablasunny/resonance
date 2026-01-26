package com.example.resonance.service

import com.example.resonance.model.schema.dto.EducationDto
import com.example.resonance.model.schema.request.UpsertEducationRq
import java.util.UUID

interface EducationService {
    fun createEducation(rq: UpsertEducationRq): EducationDto
    fun getEducationsByStudentId(studentId: UUID): List<EducationDto>
    fun updateEducation(id: UUID, rq: UpsertEducationRq): EducationDto
    fun deleteEducation(id: UUID)
}