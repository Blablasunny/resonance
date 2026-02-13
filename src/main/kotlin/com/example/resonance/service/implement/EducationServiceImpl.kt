package com.example.resonance.service.implement

import com.example.resonance.database.dao.EducationDao
import com.example.resonance.database.dao.StudentDao
import com.example.resonance.database.entity.Education
import com.example.resonance.model.mapper.toDto
import com.example.resonance.model.mapper.toEntity
import com.example.resonance.model.mapper.update
import com.example.resonance.model.schema.dto.EducationDto
import com.example.resonance.model.schema.request.UpsertEducationRq
import com.example.resonance.service.EducationService
import com.example.resonance.service.StudentService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import kotlin.jvm.optionals.getOrElse

@Transactional
@Service
class EducationServiceImpl(
    private val educationDao: EducationDao,
    private val studentService: StudentService,
): EducationService {
    override fun getEducationsByStudentId(studentId: UUID): List<EducationDto> =
        educationDao.findEducationsByStudents(studentService.getStudent(studentId)).map { it.toDto() }

    override fun getEducation(id: UUID) =
        educationDao.findById(id).getOrElse { throw RuntimeException("Education with id $id not found") }

    override fun getEducations(): List<EducationDto> = educationDao.findAll().map { it.toDto() }

    override fun addEducation(
        studentId: UUID,
        rq: UpsertEducationRq
    ): EducationDto {
        val education = rq.toEntity()
        for (ed in educationDao.findAll()) {
            if (education == ed) {
                return updateEducation(ed.id!!, rq, studentId)
            }
        }
        return createEducation(rq, studentId)
    }

    override fun changeEducation(
        id: UUID,
        rq: UpsertEducationRq,
        studentId: UUID
    ): EducationDto {
        val education = rq.toEntity()
        if (education == getEducation(id)) return getEducation(id).toDto()
        deleteEducation(id, studentId)
        for (ed in educationDao.findAll()) {
            if (education == ed) {
                return updateEducation(ed.id!!, rq, studentId)
            }
        }
        return createEducation(rq, studentId)
    }

    override fun deleteEducation(id: UUID, studentId: UUID) {
        if (!getEducation(id).students.map { it.id }.contains(studentId)) {
            throw RuntimeException("Student with id $studentId doesn't have education with id $id")
        }
        val education = getEducation(id)
        val student = studentService.getStudent(studentId)
        student.educations.remove(education)
        education.students.remove(student)
        if (education.students.isEmpty()) educationDao.delete(education)
    }

    private fun createEducation(rq: UpsertEducationRq, studentId: UUID): EducationDto {
        val education = rq.toEntity()
        return educationDao.save(modifyEducation(education, studentId)).toDto()
    }

    private fun updateEducation(
        id: UUID,
        rq: UpsertEducationRq,
        studentId: UUID
    ): EducationDto {
        val education = getEducation(id).update(rq)
        return educationDao.save(modifyEducation(education, studentId)).toDto()
    }

    private fun modifyEducation(education: Education, studentId: UUID): Education {
        if (studentId !in education.students.map { it.id }) {
            val student = studentService.getStudent(studentId)
            education.students.add(student)
            student.educations.add(education)
        }
        return education
    }
}