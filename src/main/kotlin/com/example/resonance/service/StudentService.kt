package com.example.resonance.service

import com.example.resonance.database.entity.Student
import com.example.resonance.model.schema.dto.EducationDto
import com.example.resonance.model.schema.request.UpsertStudentRq
import com.example.resonance.model.schema.dto.StudentDto
import com.example.resonance.model.schema.request.UpsertEducationRq
import java.util.UUID

interface StudentService {
    fun getStudents() : List<StudentDto>
    fun createStudent(rq: UpsertStudentRq): StudentDto
    fun getStudent(id : UUID) : Student
}