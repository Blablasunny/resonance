package com.example.resonance.service.implement

import com.example.resonance.database.dao.EducationDao
import com.example.resonance.database.dao.StudentDao
import com.example.resonance.database.dao.UserDao
import com.example.resonance.database.entity.Education
import com.example.resonance.model.mapper.toDto
import com.example.resonance.model.mapper.toEntity
import com.example.resonance.model.mapper.update
import com.example.resonance.model.schema.dto.EducationDto
import com.example.resonance.model.schema.request.UpsertEducationRq
import com.example.resonance.service.EducationService
import com.example.resonance.service.StudentService
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID
import kotlin.jvm.optionals.getOrElse

@Transactional
@Service
class EducationServiceImpl(
    private val educationDao: EducationDao,
    private val studentDao: StudentDao,
    private val studentService: StudentService,
): EducationService {
    @Transactional
    override fun createEducation(rq: UpsertEducationRq): EducationDto {
        val education = rq.toEntity()
        return educationDao.save(modifyEducation(rq, education)).toDto()
    }

    override fun getEducationsByStudentId(studentId: UUID): List<EducationDto> =
        educationDao.findEducationsByStudents(studentDao.findById(studentId)).map { it.toDto() }

    override fun updateEducation(
        id: UUID,
        rq: UpsertEducationRq
    ): EducationDto {
        val education = educationDao.findById(id).getOrElse { throw RuntimeException("Education with id $id not found") }.update(rq)
        education.updatedAt = LocalDateTime.now()
        return educationDao.save(modifyEducation(rq, education)).toDto()
    }

    override fun deleteEducation(id: UUID) {
        if (!educationDao.existsById(id)) {
            throw RuntimeException("Education with id $id not found")
        }
        educationDao.deleteById(id)
    }

    private fun modifyEducation(rq: UpsertEducationRq, education: Education): Education {
        for (id in rq.studentIds) {
            val student = studentService.getStudent(id)
            education.students.add(student)
            student.educations.add(education)
            //studentDao.save(student)
        }
        return education
    }
}