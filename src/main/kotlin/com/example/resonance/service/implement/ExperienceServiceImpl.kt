package com.example.resonance.service.implement

import com.example.resonance.database.dao.ExperienceDao
import com.example.resonance.database.dao.StudentDao
import com.example.resonance.database.entity.Experience
import com.example.resonance.model.mapper.toDto
import com.example.resonance.model.mapper.toEntity
import com.example.resonance.model.mapper.update
import com.example.resonance.model.schema.dto.ExperienceDto
import com.example.resonance.model.schema.request.UpsertExperienceRq
import com.example.resonance.service.ExperienceService
import com.example.resonance.service.StudentService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import kotlin.jvm.optionals.getOrElse

@Service
@Transactional
class ExperienceServiceImpl(
    private val experienceDao: ExperienceDao,
    private val studentService: StudentService,
): ExperienceService {
    override fun getExperienceByStudentId(studentId: UUID): List<ExperienceDto> =
        experienceDao.findExperiencesByStudents(studentService.getStudent(studentId)).map { it.toDto() }

    override fun getExperience(id: UUID): Experience =
        experienceDao.findById(id).getOrElse { throw RuntimeException("Experience with id $id not found") }

    override fun getExperiences(): List<ExperienceDto> = experienceDao.findAll().map { it.toDto() }

    override fun addExperience(
        studentId: UUID,
        rq: UpsertExperienceRq
    ): ExperienceDto {
        val experience = rq.toEntity()
        for (exp in experienceDao.findAll()) {
            if (experience == exp) {
                return updateExperience(exp.id!!, rq, studentId)
            }
        }
        return createExperience(studentId, rq)
    }

    override fun changeExperience(
        id: UUID,
        rq: UpsertExperienceRq,
        studentId: UUID
    ): ExperienceDto {
        val experience = rq.toEntity()
        if (experience == getExperience(studentId)) return getExperience(id).toDto()
        deleteExperience(id, studentId)
        for (exp in experienceDao.findAll()) {
            if (experience == exp) {
                return updateExperience(exp.id!!, rq, studentId)
            }
        }
        return createExperience(studentId, rq)
    }

    override fun deleteExperience(id: UUID, studentId: UUID) {
        if (!getExperience(id).students.map { it.id }.contains(studentId)) {
            throw RuntimeException("Student with id $studentId doesn't have experience with id $id")
        }
        val experience = getExperience(id)
        val student = studentService.getStudent(studentId)
        student.experiences.remove(experience)
        experience.students.remove(student)
        if (experience.students.isEmpty()) experienceDao.delete(experience)
    }

    private fun createExperience(studentId: UUID, rq: UpsertExperienceRq): ExperienceDto {
        val experience = rq.toEntity()
        return experienceDao.save(modifyExperience(experience, studentId)).toDto()
    }

    private fun updateExperience(
        id: UUID,
        rq: UpsertExperienceRq,
        studentId: UUID
    ): ExperienceDto {
        val experience = getExperience(id).update(rq)
        return experienceDao.save(modifyExperience(experience, studentId)).toDto()
    }

    private fun modifyExperience(experience: Experience, studentId: UUID): Experience {
        if (studentId !in experience.students.map { it.id }) {
            val student = studentService.getStudent(studentId)
            experience.students.add(student)
            student.experiences.add(experience)
        }
        return experience
    }
}