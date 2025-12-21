package com.example.resonance.service

import com.example.resonance.model.dto.rq.UpsertStudentRq
import com.example.resonance.model.dto.rs.StudentDto

interface StudentService {
    fun getStudents() : List<StudentDto>
    fun createStudent(rq: UpsertStudentRq): StudentDto
}