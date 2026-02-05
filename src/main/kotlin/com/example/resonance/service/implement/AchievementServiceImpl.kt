package com.example.resonance.service.implement

import com.example.resonance.database.dao.AchievementDao
import com.example.resonance.database.entity.Achievement
import com.example.resonance.model.mapper.toDto
import com.example.resonance.model.mapper.toEntity
import com.example.resonance.model.mapper.update
import com.example.resonance.model.schema.dto.AchievementDto
import com.example.resonance.model.schema.request.UpsertAchievementRq
import com.example.resonance.service.AchievementService
import com.example.resonance.service.StudentService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import kotlin.jvm.optionals.getOrElse

@Transactional
@Service
class AchievementServiceImpl(
    private val achievementDao: AchievementDao,
    private val studentService: StudentService,
): AchievementService {
    override fun getAchievementByStudentId(studentId: UUID): List<AchievementDto> =
        achievementDao.findAchievementsByStudents(studentService.getStudent(studentId)).map { it.toDto() }

    override fun getAchievement(id: UUID): Achievement =
        achievementDao.findById(id).getOrElse { throw RuntimeException("Achievement with id $id not found") }

    override fun getAchievements(): List<AchievementDto> = achievementDao.findAll().map { it.toDto() }

    override fun addAchievement(
        studentId: UUID,
        rq: UpsertAchievementRq
    ): AchievementDto {
        val achievement = rq.toEntity()
        for (ach in achievementDao.findAll()) {
            if (achievement == ach) {
                return updateAchievement(ach.id!!, rq, studentId)
            }
        }
        return createAchievement(rq, studentId)
    }

    override fun changeAchievement(
        id: UUID,
        rq: UpsertAchievementRq,
        studentId: UUID
    ): AchievementDto {
        val achievement = rq.toEntity()
        deleteAchievement(id, studentId)
        for (ach in achievementDao.findAll()) {
            if (achievement == ach) {
                return updateAchievement(ach.id!!, rq, studentId)
            }
        }
        return createAchievement(rq, studentId)
    }

    override fun deleteAchievement(id: UUID, studentId: UUID) {
        if (!getAchievement(id).students.map { it.id }.contains(studentId)) {
            throw RuntimeException("Student with id $studentId doesn't have achievement with id $id")
        }
        val achievement = getAchievement(id)
        val student = studentService.getStudent(studentId)
        student.achievements.remove(achievement)
        achievement.students.remove(student)
    }

    private fun createAchievement(rq: UpsertAchievementRq, studentId: UUID): AchievementDto {
        val achievement = rq.toEntity()
        return achievementDao.save(modifyAchievement(achievement, studentId)).toDto()
    }

    private fun updateAchievement(
        id: UUID,
        rq: UpsertAchievementRq,
        studentId: UUID
    ): AchievementDto {
        val achievement = getAchievement(id).update(rq)
        return achievementDao.save(modifyAchievement(achievement, studentId)).toDto()
    }

    private fun modifyAchievement(achievement: Achievement, studentId: UUID): Achievement {
        if (studentId !in achievement.students.map { it.id }) {
            val student = studentService.getStudent(studentId)
            achievement.students.add(student)
            student.achievements.add(achievement)
        }
        return achievement
    }
}