package com.example.resonance.service.implement

import com.example.resonance.database.dao.EducationDao
import com.example.resonance.database.dao.StudentDao
import com.example.resonance.database.dao.UserDao
import com.example.resonance.database.entity.Student
import com.example.resonance.model.schema.request.UpsertStudentRq
import com.example.resonance.model.schema.dto.StudentDto
import com.example.resonance.model.mapper.toDto
import com.example.resonance.model.mapper.toEntity
import com.example.resonance.model.mapper.update
import com.example.resonance.model.schema.dto.EducationDto
import com.example.resonance.model.schema.request.UpsertEducationRq
import com.example.resonance.service.StudentService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import kotlin.jvm.optionals.getOrElse

@Transactional
@Service
class StudentServiceImpl(
    private val studentDao: StudentDao,
    private val userDao: UserDao,
): StudentService {
    override fun getStudents(): List<StudentDto> =
        studentDao.findAll().map { it.toDto() }

    override fun getStudent(id: UUID): Student =
        studentDao.findById(id).getOrElse { throw RuntimeException("student not found") }

    override fun createStudent(rq: UpsertStudentRq): StudentDto =
        studentDao.save(rq.toEntity()).toDto()

    override fun updateStudent(
        id: UUID,
        rq: UpsertStudentRq
    ): StudentDto {
        val student = rq.toEntity()
        if (student in studentDao.findAll()) {
            throw RuntimeException("this student already exists")
        }
        return studentDao.save(getStudent(id).update(rq)).toDto()
    }

    override fun deleteStudent(id: UUID) {
        val student = studentDao.findById(id).getOrElse { throw RuntimeException("student not found") }
        val userId = userDao.findByUserId(id)!!.id
        student.educations.forEach { education -> education.students.remove(student) }
        student.educations.clear()
        studentDao.deleteById(id)
        userDao.deleteById(userId!!)
    }

}