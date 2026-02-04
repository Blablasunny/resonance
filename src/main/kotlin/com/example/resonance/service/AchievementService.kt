package com.example.resonance.service

import com.example.resonance.database.entity.Achievement
import com.example.resonance.model.schema.dto.AchievementDto
import com.example.resonance.model.schema.request.UpsertAchievementRq
import java.util.UUID

interface AchievementService {
    fun getAchievementByStudentId(studentId: UUID): List<AchievementDto>
    fun getAchievement(id: UUID): Achievement
    fun getAchievements(): List<AchievementDto>
    fun addAchievement(studentId: UUID, rq: UpsertAchievementRq): AchievementDto
    fun changeAchievement(id: UUID, rq: UpsertAchievementRq, studentId: UUID): AchievementDto
    fun deleteAchievement(id: UUID, studentId: UUID)
}