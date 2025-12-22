package com.example.resonance.service.implement

import com.example.resonance.database.dao.StudentDao
import com.example.resonance.database.entity.Student
import com.example.resonance.model.schema.request.UpsertStudentRq
import com.example.resonance.model.schema.dto.StudentDto
import com.example.resonance.model.mapper.toDto
import com.example.resonance.model.mapper.toEntity
import com.example.resonance.service.StudentService
import org.springframework.stereotype.Service
import java.util.UUID
import kotlin.jvm.optionals.getOrElse

@Service
class StudentServiceImpl(
    private val studentDao : StudentDao
): StudentService {
    override fun getStudents(): List<StudentDto> =
        studentDao.findAll().map { it.toDto() }

    override fun createStudent(rq: UpsertStudentRq): StudentDto =
        studentDao.save(rq.toEntity()).toDto()

    override fun getStudent(id: UUID): Student =
        studentDao.findById(id).getOrElse { throw RuntimeException("student not found") }
}