package com.example.resonance.service.implement

import com.example.resonance.database.dao.SkillDao
import com.example.resonance.database.entity.Skill
import com.example.resonance.model.mapper.toDto
import com.example.resonance.model.mapper.toEntity
import com.example.resonance.model.mapper.update
import com.example.resonance.model.schema.dto.SkillDto
import com.example.resonance.model.schema.request.UpsertSkillRq
import com.example.resonance.service.SkillService
import com.example.resonance.service.StudentService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import kotlin.jvm.optionals.getOrElse

@Transactional
@Service
class SkillServiceImpl(
    private val skillDao: SkillDao,
    private val studentService: StudentService
): SkillService {
    override fun getSkillByStudentId(studentId: UUID): List<SkillDto> =
        skillDao.findSkillByStudents(studentService.getStudent(studentId)).map { it.toDto() }

    override fun getSkill(id: UUID): Skill =
        skillDao.findById(id).getOrElse { throw RuntimeException("Skill with id $id not found!") }

    override fun getSkills(): List<SkillDto> = skillDao.findAll().map { it.toDto() }

    override fun addSkill(
        studentId: UUID,
        rq: UpsertSkillRq
    ): SkillDto {
        val skill = rq.toEntity()
        for (sk in skillDao.findAll()) {
            if (skill == sk) {
                return updateSkill(sk.id!!, rq, studentId)
            }
        }
        return createSkill(rq, studentId)
    }

    override fun changeSkill(
        id: UUID,
        rq: UpsertSkillRq,
        studentId: UUID
    ): SkillDto {
        val skill = rq.toEntity()
        deleteSkill(id, studentId)
        for (sk in skillDao.findAll()) {
            if (skill == sk) {
                return updateSkill(sk.id!!, rq, studentId)
            }
        }
        return createSkill(rq, studentId)
    }

    override fun deleteSkill(id: UUID, studentId: UUID) {
        if (!getSkill(id).students.map { it.id }.contains(studentId)) {
            throw RuntimeException("Student with id $studentId doesn't have skill with id $id")
        }
        val skill = getSkill(id)
        val student = studentService.getStudent(studentId)
        student.skills.remove(skill)
        skill.students.remove(student)
    }

    private fun createSkill(rq: UpsertSkillRq, studentId: UUID): SkillDto {
        val skill = rq.toEntity()
        return skillDao.save(modifySkill(skill, studentId)).toDto()
    }

    private fun updateSkill(
        id: UUID,
        rq: UpsertSkillRq,
        studentId: UUID,
    ): SkillDto {
        val skill = getSkill(id).update(rq)
        return skillDao.save(modifySkill(skill, studentId)).toDto()
    }

    private fun modifySkill(skill: Skill, studentId: UUID): Skill {
        if (studentId !in skill.students.map { it.id }) {
            val student = studentService.getStudent(studentId)
            skill.students.add(student)
            student.skills.add(skill)
        }
        return skill
    }
}