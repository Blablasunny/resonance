package com.example.resonance.service

import com.example.resonance.database.entity.Skill
import com.example.resonance.model.schema.dto.SkillDto
import com.example.resonance.model.schema.request.UpsertSkillRq
import java.util.UUID

interface SkillService {
    fun getSkillByStudentId(studentId: UUID): List<SkillDto>
    fun getSkillByVacancyId(vacancyId: UUID): List<SkillDto>
    fun getSkill(id: UUID): Skill
    fun getSkills(): List<SkillDto>
    fun addSkill(ownerId: UUID, rq: UpsertSkillRq, skillOwner: SkillOwner): SkillDto
    fun changeSkill(id: UUID, rq: UpsertSkillRq, ownerId: UUID, skillOwner: SkillOwner): SkillDto
    fun deleteSkill(id: UUID, ownerId: UUID, skillOwner: SkillOwner)
}

enum class SkillOwner {
    STUDENT, VACANCY
}