package com.example.resonance.service

import com.example.resonance.database.entity.Experience
import com.example.resonance.model.schema.dto.ExperienceDto
import com.example.resonance.model.schema.request.UpsertExperienceRq
import java.util.UUID

interface ExperienceService {
    fun getExperienceByStudentId(studentId: UUID): List<ExperienceDto>
    fun getExperience(id: UUID): Experience
    fun getExperiences(): List<ExperienceDto>
    fun addExperience(studentId: UUID, rq: UpsertExperienceRq): ExperienceDto
    fun changeExperience(id: UUID, rq: UpsertExperienceRq, studentId: UUID): ExperienceDto
    fun deleteExperience(id: UUID, studentId: UUID)
}