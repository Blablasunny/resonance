package com.example.resonance.service

import com.example.resonance.database.entity.Skill
import com.example.resonance.model.schema.dto.SkillDto
import com.example.resonance.model.schema.request.UpsertSkillRq
import java.util.UUID

interface SkillService {
    fun getSkillByStudentId(studentId: UUID): List<SkillDto>
    fun getSkill(id: UUID): Skill
    fun getSkills(): List<SkillDto>
    fun addSkill(studentId: UUID, rq: UpsertSkillRq): SkillDto
    fun changeSkill(id: UUID, rq: UpsertSkillRq, studentId: UUID): SkillDto
    fun deleteSkill(id: UUID, studentId: UUID)
}