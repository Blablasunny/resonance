package com.example.resonance.model.mapper

import com.example.resonance.database.entity.Skill
import com.example.resonance.model.schema.dto.SkillDto
import com.example.resonance.model.schema.request.UpsertSkillRq
import java.time.LocalDateTime

fun Skill.toDto() = SkillDto(
    id = id,
    skillName = skillName,
    skillCategory = skillCategory,
    createdAt = createdAt,
    updatedAt = updatedAt,
    students = students.map { it.toDto() }
)

fun UpsertSkillRq.toEntity() = Skill(
    skillName = skillName,
    skillCategory = skillCategory,
)

fun Skill.update(rq: UpsertSkillRq): Skill =
    this.apply {
        skillName = rq.skillName
        skillCategory = rq.skillCategory
        updatedAt = LocalDateTime.now()
    }