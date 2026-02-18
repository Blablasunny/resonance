package com.example.resonance.service.implement

import com.example.resonance.database.dao.StudentDao
import com.example.resonance.database.dao.UserDao
import com.example.resonance.database.entity.ProfessionGrade
import com.example.resonance.database.entity.Student
import com.example.resonance.errors.AlreadyExistsException
import com.example.resonance.errors.NotFountException
import com.example.resonance.model.schema.request.UpsertStudentRq
import com.example.resonance.model.schema.dto.StudentDto
import com.example.resonance.model.mapper.toDto
import com.example.resonance.model.mapper.toEntity
import com.example.resonance.model.mapper.update
import com.example.resonance.model.schema.request.GradesRq
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
        studentDao.findById(id).getOrElse { throw NotFountException("Студент", id) }

    override fun getStudentById(id: UUID): StudentDto = getStudent(id).toDto()

    override fun getStudentsByGrades(rq: GradesRq): List<StudentDto> {
        val students: MutableList<StudentDto> = arrayListOf()
        for (student in getStudents()) {
            if (student.professionGrade in rq.grades) students.add(student)
        }
        return students
    }

    override fun getGrades(): List<ProfessionGrade> =
        ProfessionGrade.entries

    override fun createStudent(rq: UpsertStudentRq): StudentDto =
        studentDao.save(rq.toEntity()).toDto()

    override fun updateStudent(
        id: UUID,
        rq: UpsertStudentRq
    ): StudentDto {
        val student = rq.toEntity()
        val oldStudent = getStudent(id)
        if (oldStudent == student) {
            return oldStudent.toDto()
        }
        if (student in studentDao.findAll()) {
            throw AlreadyExistsException(rq)
        }
        return studentDao.save(getStudent(id).update(rq)).toDto()
    }

    override fun deleteStudent(id: UUID) {
        val userId = userDao.findById(id).getOrElse { throw NotFountException("Пользователь", id) }.id
        studentDao.deleteById(id)
        userDao.deleteById(userId!!)
    }
}