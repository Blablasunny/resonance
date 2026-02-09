package com.example.resonance.service.implement

import com.example.resonance.database.dao.SkillDao
import com.example.resonance.database.entity.Skill
import com.example.resonance.model.mapper.toDto
import com.example.resonance.model.mapper.toEntity
import com.example.resonance.model.mapper.update
import com.example.resonance.model.schema.dto.SkillDto
import com.example.resonance.model.schema.request.UpsertSkillRq
import com.example.resonance.service.SkillOwner
import com.example.resonance.service.SkillService
import com.example.resonance.service.StudentService
import com.example.resonance.service.VacancyService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import kotlin.jvm.optionals.getOrElse

@Transactional
@Service
class SkillServiceImpl(
    private val skillDao: SkillDao,
    private val studentService: StudentService,
    private val vacancyService: VacancyService
): SkillService {
    override fun getSkillByStudentId(studentId: UUID): List<SkillDto> =
        skillDao.findSkillsByStudents(studentService.getStudent(studentId)).map { it.toDto() }

    override fun getSkillByVacancyId(vacancyId: UUID): List<SkillDto> =
        skillDao.findSkillsByVacancies(vacancyService.getVacancy(vacancyId)).map { it.toDto() }

    override fun getSkill(id: UUID): Skill =
        skillDao.findById(id).getOrElse { throw RuntimeException("Skill with id $id not found!") }

    override fun getSkills(): List<SkillDto> = skillDao.findAll().map { it.toDto() }

    override fun addSkill(
        ownerId: UUID,
        rq: UpsertSkillRq,
        skillOwner: SkillOwner
    ): SkillDto {
        val skill = rq.toEntity()
        for (sk in skillDao.findAll()) {
            if (skill == sk) {
                return updateSkill(sk.id!!, rq, ownerId, skillOwner)
            }
        }
        return createSkill(rq, ownerId, skillOwner)
    }

    override fun changeSkill(
        id: UUID,
        rq: UpsertSkillRq,
        ownerId: UUID,
        skillOwner: SkillOwner
    ): SkillDto {
        val skill = rq.toEntity()
        deleteSkill(id, ownerId, skillOwner)
        for (sk in skillDao.findAll()) {
            if (skill == sk) {
                return updateSkill(sk.id!!, rq, ownerId, skillOwner)
            }
        }
        return createSkill(rq, ownerId, skillOwner)
    }

    override fun deleteSkill(id: UUID, ownerId: UUID, skillOwner: SkillOwner) {
        val skill = getSkill(id)
        when(skillOwner) {
            SkillOwner.STUDENT -> {
                if (!getSkill(id).students.map { it.id }.contains(ownerId)) {
                    throw RuntimeException("Student with id $ownerId doesn't have skill with id $id")
                }
                val student = studentService.getStudent(ownerId)
                student.skills.remove(skill)
                skill.students.remove(student)
            }

            SkillOwner.VACANCY -> {
                if (!getSkill(id).vacancies.map { it.id }.contains(ownerId)) {
                    throw RuntimeException("Vacancy with id $ownerId doesn't have skill with id $id")
                }
                val vacancy = vacancyService.getVacancy(ownerId)
                vacancy.skills.remove(skill)
                skill.vacancies.remove(vacancy)
            }
        }
    }

    private fun createSkill(rq: UpsertSkillRq, ownerId: UUID, skillOwner: SkillOwner): SkillDto {
        val skill = rq.toEntity()
        return skillDao.save(modifySkill(skill, ownerId, skillOwner)).toDto()
    }

    private fun updateSkill(
        id: UUID,
        rq: UpsertSkillRq,
        ownerId: UUID,
        skillOwner: SkillOwner,
    ): SkillDto {
        val skill = getSkill(id).update(rq)
        return skillDao.save(modifySkill(skill, ownerId, skillOwner)).toDto()
    }

    private fun modifySkill(skill: Skill, ownerId: UUID, skillOwner: SkillOwner): Skill {
        when(skillOwner) {
            SkillOwner.STUDENT -> {
                if (ownerId !in skill.students.map { it.id }) {
                    val student = studentService.getStudent(ownerId)
                    skill.students.add(student)
                    student.skills.add(skill)
                }
            }

            SkillOwner.VACANCY -> {
                if (ownerId !in skill.vacancies.map { it.id }) {
                    val vacancy = vacancyService.getVacancy(ownerId)
                    skill.vacancies.add(vacancy)
                    vacancy.skills.add(skill)
                }
            }
        }
        return skill
    }
}